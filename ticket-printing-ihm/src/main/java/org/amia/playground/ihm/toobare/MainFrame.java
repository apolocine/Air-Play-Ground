package org.amia.playground.ihm.toobare;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {
        // Initialisation de la fenêtre
        setTitle("Toolbar avec Image de Fond");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer une instance de votre barre d'outils personnalisée
        ImageToolBar toolbar = new ImageToolBar("menutoolbare.png");

        // Ajouter des boutons ou autres composants à la barre d'outils
        toolbar.add(new JButton( ));
        toolbar.add(new JButton( ));
        // ...

        // Ajouter la barre d'outils à la fenêtre
        add(toolbar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
