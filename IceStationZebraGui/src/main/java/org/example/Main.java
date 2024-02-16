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
        Dockerfile dockerfile = new Dockerfile("openjdk:11","testImage");
        dockerfile.addENV("COMPILER_NAME","");
        dockerfile.addENV("COMPILER_WHATEVER","");
        dockerfile.addVolume("/installs");
        dockerfile.addVolume("/output");
        dockerfile.addRUN("echo \"$(ls)\"\n");
        dockerfile.addCMD("echo \"$(ls)\"\n");
        FileIO file = new FileIO(FileIO.getApplicationRootPath(),"Dockerfile");
        file.write(dockerfile.toString());
        dockerfile.build(file.getPath().toString()).subscribe(System.out::println);
        //
        DockerContainer container = new DockerContainer("testcontainer",dockerfile);
        container.setVolume("./scripts", "/installs");
        container.setVolume("./installs", "/installs");
        container.setVolume("./compilecommands", "/compilecommands");
        container.setVolume("./codebase", "/codebase");
        container.setVolume("./output", "/output");

        container.setEnv("COMPILER_NAME","nazi penis");
        container.setEnv("COMPILER_WHATEVER","nazi anus");
        container.run(new String[0]).subscribe(System.out::println,System.out::println);

        container.stop();
        container.remove();
        //
        Thread.sleep(3000);
        System.out.println("Removing...");
        Thread.sleep(3000);
        dockerfile.remove().subscribe(System.out::println);
    }
}