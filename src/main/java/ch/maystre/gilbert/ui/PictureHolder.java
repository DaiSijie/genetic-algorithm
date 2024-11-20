/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.ui;

import ch.maystre.gilbert.computation.StringUtils;
import ch.maystre.gilbert.imageutils.BWPicture;
import ch.maystre.gilbert.imageutils.PictureCumulator;
import ch.maystre.gilbert.imageutils.Resizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureHolder extends JComponent {

    /**
     * MOST IMPORTANT FIELD! WILL DEFINE THE PICTURE "QUALITY"
     */
    private static final int BOUND_WIDTH = 200;

    /**
     * MOST IMPORTANT FIELD! WILL DEFINE THE PICTURE "QUALITY"
     */
    private static final int BOUND_HEIGHT = 200;

    private final BWPicture ideal;

    private BWPicture currentPicture;

    private PictureCumulator cumulator;

    private Thread looper;

    public PictureHolder(BufferedImage ideal){
        this.ideal = new BWPicture(Resizer.fitInsideBox(ideal, BOUND_WIDTH, BOUND_HEIGHT));
        cumulator = new PictureCumulator(this.ideal);
        currentPicture = this.ideal;
    }

    public void pause(){
        looper.interrupt();
    }

    public void start(){
        looper = getLooper();
        looper.start();
    }

    public void clear(){
        cumulator = new PictureCumulator(ideal);
        currentPicture = this.ideal;
        registerRepaint();
    }

    public void setSwitchProbability(double switchProbability){
        cumulator.setSwitchProbability(switchProbability);
    }

    public void setChildrenNumber(int childrenNumber){
        cumulator.setChildrenNumber(childrenNumber);
    }

    public void setSwitchRange(int range){
        cumulator.setSwitchRange(range);
    }

    public void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        double width = getBounds().getWidth();
        double height = getBounds().getHeight();

        // draw the background
        g.setColor(CumulativeSelection.BACKGROUND_COLOR);
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        BufferedImage scaled = Resizer.fitInsideBox(currentPicture.toBufferedImage(), width - 20 , height - 50);

        int startX = (int) (width - scaled.getWidth())/2;
        int startY = (int) (height - 30 - scaled.getHeight())/2;

        g.drawImage(scaled, startX, startY, null);

        //draw infos
        Rectangle2D.Double statisticsBounds = new Rectangle2D.Double(startX, startY + scaled.getHeight(), scaled.getWidth(), 30);
        paintStatistics(g, statisticsBounds);

    }

    public Dimension getPreferredSize(){
        double sx = 600d / ideal.getWidth();
        double sy = 600d / ideal.getHeight();
        double scale = Math.min(sx, sy);

        int picWidth = (int) (ideal.getWidth() * scale);
        int picHeight = (int) (ideal.getHeight() * scale);
        return new Dimension(picWidth + 20, picHeight + 50);
    }

    private void paintStatistics(Graphics2D g, Rectangle2D.Double bounds){
        g.setColor(new Color(239, 238, 186));
        g.fill(bounds);

        String closeness = "Closeness: " + StringUtils.formatCloseness(cumulator.getCloseness());
        String iteration = "Generation: " + cumulator.getGeneration();
        g.setColor(Color.BLACK);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(g.getFont().deriveFont(16f));

        Rectangle2D closenessBounds = g.getFontMetrics().getStringBounds(closeness, g);
        Rectangle2D iterationBounds = g.getFontMetrics().getStringBounds(iteration, g);


        int closenessY = (int) (bounds.y + (bounds.height + closenessBounds.getHeight()) / 2) - 4;
        int closenessX = (int) (bounds.x + 3);

        int iterationY = (int) (bounds.y + (bounds.height + iterationBounds.getHeight()) / 2) - 4;
        int iterationX = (int) (bounds.x + 3 + closenessBounds.getWidth() + 10);


        g.drawString(closeness, closenessX, closenessY);
        g.drawString(iteration, iterationX, iterationY);
    }

    private Thread getLooper(){
        return new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    newIteration();
                }
            }
        });
    }

    private void newIteration(){
        currentPicture = cumulator.next();
        registerRepaint();
    }

    private void registerRepaint(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                repaint();
            }
        });
    }
}
