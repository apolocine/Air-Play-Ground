package org.amia.playground.ihm.toobare;
import javax.swing.*;
import java.awt.*;

public class ImageToolBar extends JToolBar {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image backgroundImage;

    public ImageToolBar(String imagePath) {
        super();
        // Load the image using the ClassLoader
        ClassLoader classLoader = getClass().getClassLoader();
        java.net.URL imageUrl = classLoader.getResource(imagePath);
        
        // Charger l'image de fond
        backgroundImage =  Toolkit.getDefaultToolkit().getImage(imageUrl);//new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner l'image de fond sur toute la surface de la barre d'outils
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
