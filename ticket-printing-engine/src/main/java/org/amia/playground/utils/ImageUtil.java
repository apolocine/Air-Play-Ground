package org.amia.playground.utils;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class ImageUtil {

    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Convert the Image back into an ImageIcon
        return new ImageIcon(resizedImage);
    }

    // Alternative method using BufferedImage for more control over the scaling process
    public static ImageIcon resizeImageIconHighQuality(ImageIcon icon, int width, int height) {
        Image originalImage = icon.getImage();
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        return new ImageIcon(resizedImage);
    }
}
