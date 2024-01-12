package org.amia.play.ihm;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Première classe JFrame
public class MainJFrame extends JFrame {
    public MainJFrame() {
        // Configuration de JFrame
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton btnOpen = new JButton("Ouvrir SecondJFrame et Fermer MainJFrame");
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSecondFrame();
            }
        });
        this.add(btnOpen);
    }

    public void openSecondFrame() {
        SecondJFrame secondFrame = new SecondJFrame();
        secondFrame.setVisible(true); // Ouvrir la seconde JFrame
        this.dispose(); // Fermer la JFrame actuelle
    }

    public static void main(String[] args) {
        MainJFrame frame = new MainJFrame();
        frame.setVisible(true);
    }
}

// Seconde classe JFrame
class SecondJFrame extends JFrame {
    public SecondJFrame() {
    	
    	JButton btnOpen = new JButton("Ouvrir first et Fermer MainJFrame");
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	openFirsFrame();
            }
        });
        this.add(btnOpen);
        // Configuration de JFrame
        this.setSize(200, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void openFirsFrame() {
    	MainJFrame secondFrame = new MainJFrame();
        secondFrame.setVisible(true); // Ouvrir la seconde JFrame
        this.dispose(); // Fermer la JFrame actuelle
    }
}
