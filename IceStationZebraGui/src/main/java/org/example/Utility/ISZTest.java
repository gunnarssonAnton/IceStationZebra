package org.example.Utility;

import org.example.files.FileIO;
import org.example.models.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ISZTest {
    private String name;

    public ISZTest(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    private String getFilenamesForCompileCommand(){
        File sub;
        if(this.getName().equals("Janino"))
            sub = new File(Objects.requireNonNull(FileIO.getApplicationRootPath("codebase/" + this.name)));
        else
            sub = new File(Objects.requireNonNull(FileIO.getApplicationRootPath("codebase2/" + this.name)));

        return Arrays.stream(sub.listFiles()).toList().stream()
            .filter(File::isFile).map(file ->"/codebase/" + this.name + "/" + file.getName()) // Convert File to its name
            .collect(Collectors.joining(" "));
    }

    public String constructCompileCommand(Event event){
        String OUTPUT  = "/output/" + event.givenName() + "_" + this.name;
        String compileCommand = event.compileCommand();
        String FILES = getFilenamesForCompileCommand();
        compileCommand = compileCommand.replace("FILES",FILES).replace("OUTPUT",OUTPUT);
        System.out.println(compileCommand);
        return compileCommand;
    }

    public static List<ISZTest> getTests() {
        File directory = new File(FileIO.getApplicationRootPath("codebase"));
        File[] filesList = directory.listFiles();
        if (filesList != null) {
            return  Arrays.stream(filesList)
                    .filter(File::isDirectory)
                    .map(File::getName)
                    .map(name -> new ISZTest(name))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
