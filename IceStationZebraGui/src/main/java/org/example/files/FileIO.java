package org.example.files;

import org.example.Main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
            Files.writeString(path, data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("wrote file:" + this.path.toString());
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
    public static String getResource(String resource){
        StringBuilder sb = new StringBuilder("");
        InputStream inputStream = FileIO.class.getClassLoader().getResourceAsStream(resource);
        if (inputStream != null) {
            // Wrap the InputStream with a BufferedReader to read it line by line
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    sb.append(line + "\n");
                }
            } catch (Exception e) {
                System.err.println("Error reading the resource: " + e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    System.err.println("Error closing the resource stream: " + e.getMessage());
                }
            }
        } else {
            System.err.println("Resource not found: " + resource);
        }
        return sb.toString();
    }
    public static String getApplicationRootPath(){return getApplicationRootPath("");}
    public static String getApplicationRootPath(String uri) {
        try {
            CodeSource codeSource = FileIO.class.getProtectionDomain().getCodeSource();
            File jarFile;

            if (codeSource.getLocation() != null) {
                jarFile = new File(codeSource.getLocation().toURI());
            } else {
                String path = System.getProperty("user.dir") + "/" + uri;
                return new File(path).getAbsolutePath();
            }

            if (jarFile.isFile()) {
                return jarFile.getParentFile().getAbsolutePath() + "/" + uri;
            } else {
                String path = System.getProperty("user.dir");
                return new File(path).getAbsolutePath() + "/" + uri;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void createFileIf(Path path){
        try{
            if (!Files.exists(path))
                Files.createFile(path);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void createFolderIf(Path path){
        try{
            if (!Files.exists(path))
                Files.createDirectory(path);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void copyFromJar(String from, Path to){
        String content = FileIO.getResource(from);
        FileIO file = new FileIO(to.getParent().toString(), to.getFileName().toString());
        file.write(content);
    }
}
