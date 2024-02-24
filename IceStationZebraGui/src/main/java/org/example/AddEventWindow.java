package org.example;

import org.example.models.Event;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddEventWindow extends JFrame{
    private String givenName = "";
    private String compileCommand = "";
    private String dockerImage = Event.DOCKERIMAGE;
    private List<String> installation;

    public AddEventWindow(){
        EditFileWindow editFileWindow = new EditFileWindow();
        JOptionPane optionPane = new JOptionPane("Add compiler info");
        optionPane.setOptions(new String[]{"OK", "Cancel"});
        this.setLayout(new BorderLayout());



        this.setLocationRelativeTo(null);
        this.add(optionPane);
        this.setSize(500,500);
        this.setVisible(true);
    }

}
