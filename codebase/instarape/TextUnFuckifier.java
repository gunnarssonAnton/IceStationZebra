package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


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
    public static String unFuckify(String text){
        List<String> prof = readLinesFromFile("./prof.txt");
        StringBuilder unfucked = new StringBuilder();
        Arrays.stream(text.split("\n")).toList().forEach(line -> {
            Arrays.stream(text.split(" ")).toList().forEach(word ->{
                if (!prof.contains(word))
                    unfucked.append(word).append(" ");
                else
                    unfucked.append("**** ");
            });
                unfucked.append("\n");
        });
        return unfucked.toString();
    }
}
