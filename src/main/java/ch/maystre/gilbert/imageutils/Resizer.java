/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.imageutils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Resizer {

    private Resizer(){}

    public static BufferedImage fitInsideBox(BufferedImage toResize, double width, double height){
        double sx = width / toResize.getWidth();
        double sy = height/ toResize.getHeight();
        double scale = Math.min(sx, sy);

        double newWidth =  scale * toResize.getWidth();
        double newHeight = scale * toResize.getHeight();

        Image scaled = toResize.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_SMOOTH);

        BufferedImage toReturn = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = toReturn.createGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        return toReturn;
    }


}
