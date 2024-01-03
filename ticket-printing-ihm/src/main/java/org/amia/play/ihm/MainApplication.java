package org.amia.play.ihm;
import javax.swing.*;

public class MainApplication extends JFrame {

    public MainApplication() {
        // Initialize and set up the main frame
        setTitle("Application Main Frame");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup the layout and add components
        // ...
add(new ConfigurationPanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplication::new);
    }
}
