package org.example.Utility;

import org.example.files.FileIO;

import java.nio.file.Paths;
import java.util.*;

public class Generate {
    public static List<String> standardFolders;
    public static Map<String, String> standardFiles;
    static {
        standardFolders = new ArrayList<>();
        standardFolders.add("scripts");
        standardFolders.add("logs");
        standardFolders.add("codebase");
        standardFolders.add("output");
        standardFolders.add("files");
        standardFolders.add("settings");
    }
    static {
        standardFiles = new Hashtable<>();
        standardFiles.put("config.ice","settings/config.ice");
        standardFiles.put("togglePin","files/togglePin");
        standardFiles.put("scripts/compilation_entrypoint.sh","scripts/compilation_entrypoint.sh");
        standardFiles.put("scripts/execution.sh","scripts/execution.sh");
        standardFiles.put("scripts/execution_entrypoint.sh","scripts/execution_entrypoint.sh");
        standardFiles.put("scripts/post-execution.sh","scripts/post-execution.sh");
        standardFiles.put("scripts/pre-execution.sh","scripts/pre-execution.sh");
    }
    public static String generateRandomString(int len){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return result.toString();
    }
    public static void generateDependancies(){
        generateFolders();
        generateFiles();
    }
    private static void generateFolders(){
        Generate.standardFolders.forEach(folder ->{
            //FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath()));
            FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath(folder)));
        });
    }
    private static void generateFiles(){
        Generate.standardFiles.entrySet().forEach(entry -> {
            FileIO.copyFromJar(entry.getKey(),Paths.get(FileIO.getApplicationRootPath(entry.getValue())));
        });
//        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/pre-execution.sh")));
//        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/post-execution.sh")));
//        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/execution_entrypoint.sh")));
//        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/compilation_entrypoint.sh")));
//        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("settings/config.ice")));
    }
}
