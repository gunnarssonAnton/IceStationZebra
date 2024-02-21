package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.files.FileIO;
import org.example.view.CompilationView;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;


public class CompilationViewController {
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.runAllOnClick(e -> {
            this.view.getCompilerNamesSet().forEach(name -> {
                Compilation compilation = new Compilation(subject,"ubuntu:latest");


                File directory = new File(FileIO.getApplicationRootPath("codebase"));

                File[] filesList = directory.listFiles();

                Arrays.stream(filesList).toList().stream()
                        .filter(File::isDirectory)
                        .forEach(folder -> {
                            File sub = new File(FileIO.getApplicationRootPath("codebase/" + folder.getName()));
                            String FILES = Arrays.stream(sub.listFiles()).toList().stream()
                                    .filter(File::isFile).map(File::getName) // Convert File to its name
                                    .collect(Collectors.joining(" "));
                            System.out.println(folder.getName());
                            String OUTPUT  = FileIO.getApplicationRootPath("output/" + folder.getName());
                            String compileCommand = new FileIO(FileIO.getApplicationRootPath("compile_commands"),name + "_compileCmd.sh").read();
                            compileCommand = compileCommand.replace("FILES",FILES).replace("OUTPUT",OUTPUT);
                            System.out.println(compileCommand);
                            System.out.println("");
                        });





                //compilation.go(name, "");
            });
            System.exit(99);
        });
    }


    public CompilationView getView(){
        return this.view;
    }
}
