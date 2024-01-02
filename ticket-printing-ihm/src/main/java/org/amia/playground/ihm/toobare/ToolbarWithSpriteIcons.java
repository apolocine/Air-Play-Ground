package org.amia.playground.ihm.toobare;
import javax.imageio.ImageIO;
import javax.swing.*;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ToolbarWithSpriteIcons extends JFrame {

    public ToolbarWithSpriteIcons() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setTitle("Toolbar avec Sprite Icons");
        
        
        ClassLoader classLoader = getClass().getClassLoader();
        String imagePath = "menutoolbare.png";
		java.net.URL  image = classLoader.getResource(imagePath);
		// Charger l'image complète
        ImageIcon  imageUrl= new ImageIcon(image);
        
        BufferedImage resizedImage = null;
		try {
			resizedImage = loadImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		int widthByPhotosCount = imageUrl.getIconWidth()/5;
		 
		ImageIcon  iconSheet = null;
		try {
			BufferedImage  iconSheet_= Thumbnails.of(resizedImage).width((int) widthByPhotosCount).keepAspectRatio(true)
					.asBufferedImage();
			iconSheet = new ImageIcon(iconSheet_);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        System.out.println(iconSheet.getIconHeight());
        System.out.println(iconSheet.getIconWidth());
        
        JToolBar toolBar = new ImageToolBar(imagePath);
        int decHoriizental=3;
        int decVertical=3;
        
        JButton bt01 =new JButton(createIconFromSheet(iconSheet.getImage(), 0,54, 30, 30,decHoriizental,decVertical));// getButton(iconSheet.getImage(), 0,0, 30, 30,decHoriizental,decVertical);
        JButton bt02 = new JButton(createIconFromSheet(iconSheet.getImage(), 0, 96, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 0,1, 30, 30,decHoriizental,decVertical);
        JButton bt03 =new JButton(createIconFromSheet(iconSheet.getImage(), 0, 142, 30, 30,decHoriizental,decVertical)) ;//getButton(iconSheet.getImage(), 0,2, 30, 30,decHoriizental,decVertical);
        JButton bt04 =new JButton(createIconFromSheet(iconSheet.getImage(), 0, 146, 30, 30,decHoriizental,decVertical)) ;//getButton(iconSheet.getImage(), 0,3, 30, 30,decHoriizental,decVertical);
           
        JButton bt05 = new JButton(createIconFromSheet(iconSheet.getImage(), 54,8, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 1,0, 30, 30,decHoriizental,decVertical);
        JButton bt06 = new JButton(createIconFromSheet(iconSheet.getImage(), 54,54, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 1,1, 30, 30,decHoriizental,decVertical);
        JButton bt07 = new JButton(createIconFromSheet(iconSheet.getImage(), 54, 96, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 2,2, 30, 30,decHoriizental,decVertical);
        JButton bt08 = new JButton(createIconFromSheet(iconSheet.getImage(), 54, 142, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 2,3, 30, 30,decHoriizental,decVertical);
         				
        // Découper et ajouter la première icône
        toolBar.add(bt01);
        toolBar.add(bt02);
        toolBar.add(bt03);
        toolBar.add(bt04);
        
        toolBar.add(bt05);
        toolBar.add(bt06);
        toolBar.add(bt07);
        toolBar.add(bt08);
        
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 0,54, 30, 30,decHoriizental,decVertical)));
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 0, 96, 30, 30,decHoriizental,decVertical)));
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 0, 142, 30, 30,decHoriizental,decVertical)));
//       toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 54,8, 30, 30,decHoriizental,decVertical)));
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 54,54, 30, 30,decHoriizental,decVertical)));
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 54, 96, 30, 30,decHoriizental,decVertical)));
//        toolBar.add(new JButton(createIconFromSheet(iconSheet.getImage(), 54, 142, 30, 30,decHoriizental,decVertical)));
    
        
     
        
        JComboBox c3 = new JComboBox(new String[] { "A", "B", "C" });
        c3.setPrototypeDisplayValue("XXXXXXXX"); // Set a desired width
        c3.setMaximumSize(c3.getMinimumSize());
        c3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                int selection = c3.getSelectedIndex();
                switch (selection){
                case 0:  break;
                case 1:  enableNormalModeFeatures(selection); break;
                case 2:  enableRevisionModeFeatures(selection); break;
                case 3:  enableTimerModeFeatures(selection); break;
                }
            }

			private void enableTimerModeFeatures(int selection) {
				// TODO Auto-generated method stub
				
			}

			private void enableRevisionModeFeatures(int selection) {
				// TODO Auto-generated method stub
				
			}

			private void enableNormalModeFeatures(int selection) {
				// TODO Auto-generated method stub
				
			}
        });
        toolBar.add(c3);
        
        
        add(toolBar, BorderLayout.NORTH);
    }

    
    
    
    
    private JButton getButton(Image  image, int countH, int countV, int width, int height, int decHoriizental,int decVertical) {
    	int initX=0; 
    	int initY=8; 
    	int x = initX+countH*42;
    	int y = initY+countV*42;
    	
    	
		return new JButton(createIconFromSheet(image, x,y, width, height,decHoriizental,decVertical));
	}

	private ImageIcon createIconFromSheet(Image source, int x, int y, int width, int height, int decHoriizental,int decVertical) {
        BufferedImage iconImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = iconImage.createGraphics();

        // Dessiner la portion de l'image source dans la nouvelle image (icône)
        g.drawImage(source, 0, 0, width, height, x, y, x + width+decHoriizental, y + height+decVertical, null);
        g.dispose();

        return new ImageIcon(iconImage);
    }
	/**
  	 * 
  	 * @param file
  	 * @return
  	 * @throws IOException
  	 */
    public static BufferedImage loadImage(URL file) throws IOException {
        return ImageIO.read(file);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ToolbarWithSpriteIcons().setVisible(true);
        });
    }
}
