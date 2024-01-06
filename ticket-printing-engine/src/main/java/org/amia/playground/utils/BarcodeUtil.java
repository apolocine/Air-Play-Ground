package org.amia.playground.utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

public class BarcodeUtil {

    public static String generateBarcode(String data) {
        int width = 300;   // width of the barcode image
        int height = 150;  // height of the barcode image
        String imageFormat = "png"; // file format

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height);
            Path path = FileSystems.getDefault().getPath("Barcode.png");
            MatrixToImageWriter.writeToPath(bitMatrix, imageFormat, path); // Save the barcode image to file
            
            // Convert to Base64 for easy display in various environments (optional)
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray(); 
            return Base64.getEncoder().encodeToString(pngData); // Return a base64 encoded string
        } catch (WriterException | IOException e) {
            // Handle exceptions related to writing/encoding barcode
        }
        return null; // Return null or throw an exception as per your error handling policy
    }
    
 // Method to generate barcode image
    public static BufferedImage generateBarcodeImage(String data) {
        try {
            BarcodeFormat format = BarcodeFormat.CODE_128;  // Choose the format here
            int width = 300;
            int height = 100;  // Adjust size as necessary
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, format, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    
}
