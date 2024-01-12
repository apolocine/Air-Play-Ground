package org.amia.play.tools;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DigitPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage[] images = new BufferedImage[12]; // 10 chiffres + 1 pour le point + 1 pour la virgule
	private List<ImageToDraw> imagesToDraw = new ArrayList<>();
	private int width = 20;
	private  int height = 20;
	int compWidth = 250;
	int compHeight = 80;
	// Classe interne pour gérer les images à dessiner et leurs positions
	private class ImageToDraw {
		BufferedImage image;
		int xPosition;

		public ImageToDraw(BufferedImage img, int x) {
			this.image = img;
			this.xPosition = x;
		}
	}

	public DigitPanel(int width, int height, int compWidth, int compHeight) {
		super();
		this.width = width;
		this.height = height;
		this.compWidth = compWidth;
		this.compHeight = compHeight;

		initialisation();
	}

	public DigitPanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		initialisation();
	}

	public DigitPanel() {
		initialisation();
	}

	private void initialisation() {
		// Charger les images des chiffres et des symboles
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			
			for (int i = 0; i < 10; i++) {

//                String imagePath = "numbers/" + i + ".png"; // chemin relatif dans le JAR
//                images[i] = ImageIO.read(classLoader.getResource(imagePath));
//                
				String imagePath = "files/numbers/" + i + ".png"; // chemin relatif dans le JAR
				java.net.URL imageUrl = classLoader.getResource(imagePath);
				System.out.println(imageUrl);
				BufferedImage originalImage = ImageIO.read(imageUrl);/// ImageIO.read(new File(imagePath));

			
				images[i] = resizeImage(originalImage, width, height); // Redimensionner l'image
				// Charger l'image de fond
				// backgroundImage = Toolkit.getDefaultToolkit().getImage(imageUrl);

			}
			BufferedImage originalpointImage =ImageIO.read(classLoader.getResource("files/numbers/point.png"));// Image pour le point
					BufferedImage originalcommaImage =ImageIO.read(classLoader.getResource("files/numbers/comma.png")); // Image pour la virgule
			images[10] =   resizeImage(originalpointImage, width, height); // Redimensionner l'image
			images[11] =  resizeImage(originalcommaImage, width, height); // Redimensionner l'image

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		setPreferredSize(new Dimension(compWidth, compHeight)); // Taille de préférence

	 
	}

	// Méthode pour afficher une séquence de chiffres et de symboles
	public void displayNumber(String sequence) {
		imagesToDraw.clear(); // Efface les images précédentes
		char[] chars = sequence.toCharArray();
		int x = 0; // Position horizontale initiale

		for (char ch : chars) {
			BufferedImage img = null;
			if (ch >= '0' && ch <= '9') {
				img = images[ch - '0'];
			} else if (ch == '.') {
				img = images[10];
			} else if (ch == ',') {
				img = images[11];
			}

			if (img != null) {
				imagesToDraw.add(new ImageToDraw(img, x));
				x += img.getWidth(); // Incrémente x pour la prochaine image
			}
		}

		repaint(); // Demande de redessiner le panel
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (ImageToDraw imgToDraw : imagesToDraw) {
			g.drawImage(imgToDraw.image, imgToDraw.xPosition, 10, null); // Dessiner chaque image à sa position
		}
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(resultingImage, 0, 0, null);
		g2d.dispose();
		return outputImage;
	}
}
