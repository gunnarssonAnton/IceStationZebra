package org.example;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.Buffer;

public class ImageProcessor {

    public static void processImage(BufferedImage image, String destinationPath, double scale, float colorMultiplier) {
            System.out.println("image opened");
        try {
            // Load the image from the URL
            //BufferedImage image = ImageIO.read(new URL(sourceUrl));
            // Calculate new dimensions and create a resized image
            int newWidth = (int) (image.getWidth() * scale);
            int newHeight = (int) (image.getHeight() * scale);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // Modify the color of each pixel
            for (int x = 0; x < newWidth; x++) {
                for (int y = 0; y < newHeight; y++) {
                    int rgba = resizedImage.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(
                            Math.min((int)(col.getRed() * colorMultiplier), 255),
                            Math.min((int)(col.getGreen() * colorMultiplier), 255),
                            Math.min((int)(col.getBlue() * colorMultiplier), 255),
                            col.getAlpha());
                    resizedImage.setRGB(x, y, col.getRGB());
                }
            }

            // Save the modified image to the destination path
            File outputFile = new File(destinationPath);
            boolean resutl = ImageIO.write(resizedImage, "png", outputFile);

            System.out.println("Image processed and saved successfully to " + outputFile.getAbsolutePath() + ". res:" + resutl);
        } catch (IOException e) {
            System.err.println("Error processing the image: " + e.getMessage());
        }
    }
}
