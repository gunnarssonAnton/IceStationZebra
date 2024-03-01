package org.example;

import org.example.Utility.Generate;
import org.example.files.FileIO;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        Generate.generateDependancies();
        System.out.println("Application root folder:" + FileIO.getApplicationRootPath());
        SwingUtilities.invokeLater(Gui::new);
    }
}