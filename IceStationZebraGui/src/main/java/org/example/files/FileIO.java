package org.example.files;

import org.example.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.List;

public class FileIO {
    private final Path path;
    public FileIO(String path, String fileName){
        this.path = Paths.get(path + "/" + fileName);
    }
    public Path getPath(){
        return this.path;
    }
    public void append(String data){
        try {
            Files.writeString(path, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(String data){
        try {
            Files.writeString(path, data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(){
        String content = "";
        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public static String getApplicationRootPath() {
        try {
            // Get the location of the class file that Java VM loads
            CodeSource codeSource = FileIO.class.getProtectionDomain().getCodeSource();
            File jarFile;

            if (codeSource.getLocation() != null) {
                jarFile = new File(codeSource.getLocation().toURI());
            } else {
                // Fallback to user directory (might not be reliable for all IDE setups)
                String path = System.getProperty("user.dir");
                return new File(path).getAbsolutePath();
            }

            if (jarFile.isFile()) {  // Run from JAR
                // Return the directory of the JAR file
                return jarFile.getParentFile().getAbsolutePath();
            } else { // Run from IDE or unpackaged
                // In case of IDE, this typically points to the target/classes directory
                // You might need to adjust it based on your project structure or IDE configuration
                String path = System.getProperty("user.dir");
                return new File(path).getAbsolutePath();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null; // Handle error as appropriate for your application
        }
    }
}
