/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.ui;

import java.io.IOException;

public class Launcher {

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String filePath = args.length > 0 ? args[0] : null;
                try {
                    new CumulativeSelection(filePath).createAndShowGUI();
                } catch (IOException e) {
                    System.out.println("The following file doesn't look like an image! \"" + filePath + "\"");
                    e.printStackTrace();
                }
            }
        });
    }


}
