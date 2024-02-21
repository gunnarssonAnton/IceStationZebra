package org.example;

import org.example.files.FileIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditFileWindow extends JFrame {
    private final FileIO installFile;
    private final FileIO compileFile;
    private final CardLayout cardLayout = new CardLayout();
    private JPanel cardConainer = new JPanel(cardLayout);
    private final JTextArea installTextArea = new JTextArea();
    private final JTextArea compilingTextArea = new JTextArea();
    private final JButton saveBtn = new JButton("Save");
    private final JMenuBar menuBar = new JMenuBar();

    public EditFileWindow(String filename){
        this.installFile = new FileIO(FileIO.getApplicationRootPath("installs"),filename+"_install.sh");
        this.compileFile = new FileIO(FileIO.getApplicationRootPath("compile_commands"),filename+"_compileCmd.sh");

        this.setSize(500,500);
        this.setLayout(new BorderLayout());
        this.setTitle("Ice Station Zebra");

        JButton editInstallCommand = new JButton("Install cmd");
        editInstallCommand.setBackground(Color.white);
        editInstallCommand.setFocusPainted(false);

        JButton editCompilingCommand = new JButton("Compiling cmd");
        editCompilingCommand.setFocusPainted(false);
        editCompilingCommand.setBackground(Color.white);


        this.menuBar.setBackground(Color.white);
        this.menuBar.add(editInstallCommand);
        this.menuBar.add(editCompilingCommand);
        editInstallCommand.addActionListener(e -> this.changeCard());
        editCompilingCommand.addActionListener(e -> this.changeCard());

        this.setJMenuBar(this.menuBar);
        this.setLocationRelativeTo(null);
        this.saveBtn.addActionListener(e -> saveToFile());

        this.compilingTextArea.setText(this.compileFile.read());
        this.installTextArea.setText(this.installFile.read());
        
        this.cardConainer.add(this.installTextArea);
        this.cardConainer.add(this.compilingTextArea);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile();
            }

        });

        this.add(this.cardConainer, BorderLayout.CENTER);
        this.add(this.saveBtn,BorderLayout.SOUTH);
    }
    private void changeCard(){
        this.cardLayout.next(cardConainer);
    }

    private void saveToFile(){
        String installContent = installTextArea.getText();
        installFile.write(installContent);

        String compileContent = compilingTextArea.getText();
        compileFile.write("#!/bin/bash");
        compileFile.write(compileContent);

        this.dispose();
    }



}
