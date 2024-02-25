package org.example;

import org.example.Utility.Generate;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        Generate.generateDependancies();
        SwingUtilities.invokeLater(Gui::new);

    }
}