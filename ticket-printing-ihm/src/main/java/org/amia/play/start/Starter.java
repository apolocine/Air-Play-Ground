package org.amia.play.start;

import javax.swing.SwingUtilities;

import org.hmd.angio.ApplicationGUI;
 

public class Starter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ApplicationGUI().setVisible(true);
        });
    }
    
}
