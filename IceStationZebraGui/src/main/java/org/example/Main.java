package org.example;

import org.example.Utility.Generate;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        Generate.generateDependancies();
        SwingUtilities.invokeLater(Gui::new);
//        IszHandler iszHandler = new IszHandler();
////        jsonHandler.addToList(new Event(Event.DOCKERIMAGE,"gcc FILES -o OUTPUT","gcc", new String[]{"apt install gcc", "gcc --version"}));
//        iszHandler.writeToIsz(new Event(Event.DOCKERIMAGE,"clang FILES -o OUTPUT","clang", new JSONArray(List.of("apt install clang", "clang --version" ))));
//        iszHandler.writeToIsz(new Event(Event.DOCKERIMAGE,"kock FILES -o OUTPUT","gcc", new JSONArray(List.of("apt install clang", "clang --version" ))));
//        iszHandler.writeToIsz(new Event(Event.DOCKERIMAGE,"dolk FILES -o OUTPUT","gimp", new JSONArray(List.of("apt install clang", "clang --version" ))));

//        iszHandler.readIsz();
    }
}