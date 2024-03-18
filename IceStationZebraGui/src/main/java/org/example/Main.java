package org.example;

import org.example.Utility.Generate;
import org.example.files.FileIO;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        System.out.println("Application root folder:" + FileIO.getApplicationRootPath());
        Generate.generateDependancies();
        System.exit(99);
        SwingUtilities.invokeLater(Gui::new);
    }
}