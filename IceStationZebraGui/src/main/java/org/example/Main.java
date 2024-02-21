package org.example;

import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.Utility.Generate;
import org.example.files.FileIO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Generate.generateDependancies();
        SwingUtilities.invokeLater(Gui::new);
//        Dockerfile dockerfile = Dockerfile.getBasic("openjdk:11","testImage");
//        FileIO file = new FileIO(FileIO.getApplicationRootPath(),"Dockerfile");
//        file.write(dockerfile.toString());
//        dockerfile.build(file.getPath().toString()).subscribe(System.out::println);
//        DockerContainer container = DockerContainer.getBasic("testcontainer",dockerfile);
//        container.run(new String[0]).subscribe(System.out::println);
    }
}