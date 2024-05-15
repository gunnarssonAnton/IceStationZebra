
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Instarape {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        SendNowhere sn = new SendNowhere();
        Thread serverThread = new Thread(() -> {
            try {
                sn.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Open image
//        BufferedImage image = ImageIO.read(new URL(args[1]));

//        BufferedImage image = ImageIO.read(new File("/Users/rubenmadsen/Desktop/remove/image4k.jpg"));
        BufferedImage image = ImageIO.read(new File("/files/image4k.jpg"));
        //        processImage("http://example.com/image.png", "output.png", 0.5, 1.1f);
        System.out.println("Image width = " + image.getWidth());
        // Open textfile
        String text = TextUnFuckifier.readLinesFromFile("/files/text.txt").toString();

        Packet packet = new Packet(image,text);
        sn.send(packet);
        serverThread.join();

        Runnable fileReaderRunnable = new Runnable() {
            @Override
            public void run() {
                String processedText = TextUnFuckifier.unFuckify(text);
                System.out.println("text: " + processedText);
            }
        };
        Runnable imageProcessorRunnable = new Runnable() {
            @Override
            public void run() {
                ImageProcessor.processImage(image,"/output/image_processes4k.jpg",0.8,1.5f);
            }
        };
        // Create a new thread and start it
        Thread thread2 = new Thread(fileReaderRunnable);
        Thread thread1 = new Thread(imageProcessorRunnable);
        thread2.start();
        thread1.start();
        thread1.join();
        thread2.join();
//        System.exit(99);
    }
}