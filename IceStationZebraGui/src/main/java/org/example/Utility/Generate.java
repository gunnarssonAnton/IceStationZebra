package org.example.Utility;

import org.example.files.FileIO;

import java.nio.file.Paths;
import java.util.Random;

public class Generate {
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
        generateScripts();
    }
    private static void generateFolders(){
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("scripts")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("codebase")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("output")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("files")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("settings")));
    }
    private static void generateScripts(){
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/before.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/after.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/execution_entrypoint.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/compilation_entrypoint.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("settings/config.ice")));
    }
}
