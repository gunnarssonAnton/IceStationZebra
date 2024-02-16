package org.example.Utility;

import org.example.files.FileIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Generate {
    public static void generateDependancies(){
        generateFolders();
        generateScripts();
    }
    private static void generateFolders(){
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("scripts")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("installs")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("compile_commands")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("codebase")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("output")));
        FileIO.createFolderIf(Paths.get(FileIO.getApplicationRootPath("settings")));
    }
    private static void generateScripts(){
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/before.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/after.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/execution_entrypoint.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("scripts/compilation_entrypoint.sh")));
        FileIO.createFileIf(Paths.get(FileIO.getApplicationRootPath("settings/compiler_names.txt")));
    }
}
