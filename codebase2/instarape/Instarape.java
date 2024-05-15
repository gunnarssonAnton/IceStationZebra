package org.example;

import org.codehaus.janino.SimpleCompiler;
import org.codehaus.janino.util.ClassFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Instarape {
    public static void main(String[] args) throws Exception {
        SimpleCompiler compiler = new SimpleCompiler();

        String javaSourceCode =
                """
                        import javax.imageio.ImageIO;
                        import java.awt.image.BufferedImage;
                        import java.io.File;
                        import java.io.IOException;
                        import java.net.MalformedURLException;
                        import java.net.URL;
                        import java.util.List;
                        import javax.imageio.ImageIO;
                        import java.awt.*;
                        import java.awt.image.BufferedImage;
                        import java.io.*;
                        import java.net.URL;
                        import java.nio.Buffer; 
                        import java.io.*;
                        import java.net.ServerSocket;
                        import java.net.Socket;
                                              
                        import java.io.IOException;
                        import java.nio.file.Files;
                        import java.nio.file.Paths;
                        import java.util.Arrays;
                        import java.util.List;
                        
                         import javax.imageio.ImageIO;
                        import java.awt.image.BufferedImage;
                        import java.io.BufferedInputStream;
                        import java.io.ByteArrayOutputStream;
                        import java.io.IOException;
                        import java.io.Serializable;
                                                
                        public class Instarape {
                            public static void main(String[] args) throws InterruptedException, IOException {
                                System.setProperty("java.net.preferIPv4Stack", "true");
                                final SendNowhere sn = new SendNowhere();
                                
                                Thread serverThread = new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // Call the startServer() method of some object
                                                sn.startServer();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                );
                                serverThread.start();
                                              
                                // Open image
                        //        BufferedImage image = ImageIO.read(new URL(args[1]));
                                              
                        //        BufferedImage image = ImageIO.read(new File("/Users/rubenmadsen/Desktop/remove/image4k.jpg"));
                                final BufferedImage image = ImageIO.read(new File("/files/image4k.jpg"));
                                //        processImage("http://example.com/image.png", "output.png", 0.5, 1.1f);
                                System.out.println("Image width = " + image.getWidth());
                                // Open textfile
                                final String text = TextUnFuckifier.readLinesFromFile("/files/text.txt").toString();
                                              
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
                                                
                       
                        public class Packet implements Serializable {
                            public final byte[] image;
                            String text;
                            public Packet(BufferedImage bufferedImage, String text){;
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                try {
                        //            ImageIO.write(image, "JPEG", outputStream);
                                    ImageIO.write(bufferedImage,"JPEG",outputStream);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                image = outputStream.toByteArray();
                                this.text = text;
                            }
                        }
                                                
                                                
                        
                                                
                        public class SendNowhere {
                            ServerSocket ss;
                            Socket s;
                            int serverBytesReceived = 0;
                            int clientBytesReceived = 0;
                                                
                            public void startServer() throws IOException {
                            
                                    try{
                                        ServerSocket serverSocket = new ServerSocket(6000);
                                        System.out.println("Server listening on port " + serverSocket.getLocalPort());
                                                
                                        try{
                                           Socket clientSocket = serverSocket.accept();
                                            System.out.println("Client connected from " + clientSocket.getInetAddress());
                                                
                                            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                                            Packet receivedItem = (Packet) objectInputStream.readObject();
                                            System.out.println("Received: " + receivedItem);
                                                
                                        } catch (ClassNotFoundException e) {
                                            System.out.println("Class not found: " + e.getMessage());
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Server exception: " + e.getMessage());
                                    }
                            }
                                                
                                                
                            public <T extends Serializable> void send(T obj) throws IOException, InterruptedException {
                                // Small delay to ensure the server starts first
                                try {
                                    Thread.sleep(2000);
                                    try {
                                       Socket socket = new Socket("127.0.0.1", 6000);
                                        System.out.println("Connected to server at localhost:6000");
                                                
                                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                                
                                        objectOutputStream.writeObject(obj);
                                        System.out.println("Sent item to server: " + obj);
                                                
                                    } catch (IOException e) {
                                        System.out.println("Client error: " + e.getMessage());
                                    }
                                } catch (InterruptedException e) {
                                    System.out.println("Thread interrupted: " + e.getMessage());
                                }
                            }
                                                
                                                
                        }
                                                
                        
                                                
                                                
                        public class TextUnFuckifier {
                            public static List<String> readLinesFromFile(String filePath) {
                                List<String> lines = null;
                                try {
                                    // Reads all lines from a file as a Stream and converts it into a List
                                    lines = Files.readAllLines(Paths.get(filePath));
                                } catch (IOException e) {
                                    System.err.println("Unable to read the file: " + e.getMessage());
                                }
                                return lines;
                            }
                           public static String unFuckify(String text) {
                                 List<String> prof = readLinesFromFile("/files/prof.txt");
                                 StringBuilder unfucked = new StringBuilder();
                                 List<String> lines = Arrays.asList(text.split("\\n"));
                             
                                 for (String line : lines) {
                                     List<String> words = Arrays.asList(line.split(" "));
                                     for (String word : words) {
                                         if (!prof.contains(word))
                                             unfucked.append(word).append(" ");
                                         else
                                             unfucked.append("**** ");
                                     }
                                     unfucked.append("\\n");
                                 }
                             
                                 return unfucked.toString();
                             }
                        }
                                   
                        """;


        // Compile the Java source code
        compiler.cook(javaSourceCode);

        // Directory to save the compiled .class files
        Path outputDir = Paths.get("Janino-output");
        Files.createDirectories(outputDir);

        // Save the compiled .class files to the output directory
        for (ClassFile classFile : compiler.getClassFiles()) {
            saveClassFile(classFile, outputDir);
        }
    }

    private static void saveClassFile(ClassFile classFile, Path outputDir) throws IOException {
        Path filePath = outputDir.resolve(classFile.getThisClassName() + ".class");
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(classFile.toByteArray());
        }
    }


}
