/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static ch.maystre.gilbert.imageutils.PictureCumulator.START_CHILDREN_NUMBER;
import static ch.maystre.gilbert.imageutils.PictureCumulator.START_SWITCH_PROBABILITY;
import static ch.maystre.gilbert.imageutils.PictureCumulator.START_SWITCH_RANGE;

public class CumulativeSelection {

    public static final Random RANDOM = new Random();

    public static final Color BACKGROUND_COLOR = new Color(217, 239, 184);

    private final BufferedImage picture;

    // region widgets

    private JPanel mainPanel;

    private PictureHolder pictureHolder;

    private JButton startPauseButton;

    private JButton clearButton;

    private JSlider childrenNumberSlider;

    private JSlider switchProbabilitySlider;

    private JSlider switchRangeSlider;

    private JLabel childrenNumberLabel;

    private JLabel switchProbabilityLabel;

    private JLabel switchRangeLabel;

    // endregion

    public CumulativeSelection(String filePath) throws IOException {
        if(filePath == null || !(new File(filePath).exists())){
            picture = randomPicture();
        }
        else{
            picture = ImageIO.read(new File(filePath));
        }
    }

    private BufferedImage randomPicture() throws IOException {
        String[] names = {"golconda.png", "lembellie.png", "lacordesensible.png", "ladialectiqueappliquee.png"};
        ClassLoader classLoader = getClass().getClassLoader();
        return ImageIO.read(classLoader.getResourceAsStream("images/" + names[RANDOM.nextInt(names.length)]));
    }

    public void createAndShowGUI(){
        createWidgets();
        setupWidgets();
        placeWidgets();

        //Create and set up the window.
        JFrame frame = new JFrame("@CumulativeSelection");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createWidgets(){
        mainPanel = new JPanel(new BorderLayout());
        pictureHolder = new PictureHolder(picture);
        startPauseButton = new JButton("Start");
        clearButton = new JButton("Clear");
        childrenNumberSlider = new JSlider(1, 16);
        switchProbabilitySlider = new JSlider(1, 6);
        switchRangeSlider = new JSlider(1, 7);
        childrenNumberLabel = new JLabel();
        switchProbabilityLabel = new JLabel();
        switchRangeLabel = new JLabel();
    }

    private void setupWidgets(){
        startPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean showingStart = startPauseButton.getText().equals("Start");
                if(showingStart){
                    startPauseButton.setText("Pause");
                    pictureHolder.start();
                    clearButton.setEnabled(false);
                }
                else {
                    startPauseButton.setText("Start");
                    pictureHolder.pause();
                    clearButton.setEnabled(true);
                }
            }
        });

        clearButton.setEnabled(false);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearButton.setEnabled(false);
                pictureHolder.clear();
            }
        });

        childrenNumberSlider.setMajorTickSpacing(1);
        childrenNumberSlider.setSnapToTicks(true);
        childrenNumberSlider.setValue(START_CHILDREN_NUMBER);
        childrenNumberSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                changeChildrenNumber(childrenNumberSlider.getValue());
            }
        });

        switchProbabilitySlider.setMajorTickSpacing(1);
        switchProbabilitySlider.setSnapToTicks(true);
        switchProbabilitySlider.setValue(START_SWITCH_PROBABILITY);
        switchProbabilitySlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                changeSwitchProbability(switchProbabilitySlider.getValue());
            }
        });

        switchRangeSlider.setMajorTickSpacing(1);
        switchRangeSlider.setSnapToTicks(true);
        switchRangeSlider.setValue(START_SWITCH_RANGE);
        switchRangeSlider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                changeSwitchRange(switchRangeSlider.getValue());
            }
        });

        changeChildrenNumber(START_CHILDREN_NUMBER);
        changeSwitchProbability(START_SWITCH_PROBABILITY);
        changeSwitchRange(START_SWITCH_RANGE);
    }

    private void placeWidgets(){
        // place parameters
        JPanel parameterLayout = new JPanel(new GridLayout(0, 2));
        parameterLayout.add(childrenNumberSlider);
        parameterLayout.add(childrenNumberLabel);
        parameterLayout.add(switchProbabilitySlider);
        parameterLayout.add(switchProbabilityLabel);
        parameterLayout.add(switchRangeSlider);
        parameterLayout.add(switchRangeLabel);
        parameterLayout.setBorder(BorderFactory.createTitledBorder("Parameters"));
        parameterLayout.setMaximumSize(parameterLayout.getPreferredSize());
        parameterLayout.setBackground(BACKGROUND_COLOR);

        //place buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(3));
        buttonPanel.add(startPauseButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.setBackground(BACKGROUND_COLOR);

        //build the control panel
        JPanel controlHolderPanel = new JPanel();
        controlHolderPanel.setLayout(new BoxLayout(controlHolderPanel, BoxLayout.X_AXIS));
        controlHolderPanel.add(Box.createHorizontalStrut(10));
        controlHolderPanel.add(parameterLayout);
        controlHolderPanel.add(Box.createHorizontalStrut(10));
        controlHolderPanel.add(buttonPanel);
        controlHolderPanel.add(Box.createHorizontalStrut(10));
        controlHolderPanel.add(Box.createHorizontalGlue());
        controlHolderPanel.setBackground(BACKGROUND_COLOR);

        //build the signature panel
        JPanel signaturePanel = new JPanel();
        signaturePanel.setLayout(new BoxLayout(signaturePanel, BoxLayout.X_AXIS));

        signaturePanel.add(Box.createHorizontalStrut(10));
        JLabel signature = new JLabel("Gilbert Maystre 2018");
        signature.setForeground(Color.GRAY);
        signaturePanel.add(signature);
        signaturePanel.add(Box.createHorizontalStrut(10));
        signaturePanel.add(Box.createHorizontalGlue());
        signaturePanel.setBackground(BACKGROUND_COLOR);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(controlHolderPanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(signaturePanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.setMaximumSize(controlPanel.getPreferredSize());
        controlPanel.setBackground(BACKGROUND_COLOR);

        //build the main panel
        mainPanel.add(pictureHolder, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.PAGE_END);

    }

    private void changeChildrenNumber(int childrenNumber){
        childrenNumberLabel.setText("Number of children: " + childrenNumber);
        pictureHolder.setChildrenNumber(childrenNumber);
    }

    private void changeSwitchProbability(int exponent){
        double probability = Math.pow(10, -exponent);
        switchProbabilityLabel.setText("Probability of switching: 10E-" + exponent + "    " );
        pictureHolder.setSwitchProbability(probability);
    }

    private void changeSwitchRange(int range){
        switchRangeLabel.setText("Switch range: ±" + range);
        pictureHolder.setSwitchRange(range);
    }

}
