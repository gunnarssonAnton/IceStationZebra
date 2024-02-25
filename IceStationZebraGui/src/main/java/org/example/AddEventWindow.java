package org.example;

import org.example.Utility.BashSyntaxHighlighting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEventWindow extends JFrame {
//    private final FileIO installFile;
//    private final FileIO compileFile;
    private final CardLayout cardLayout = new CardLayout();
    private JPanel cardConainer = new JPanel(cardLayout);
    private final JTextPane installTextPane = new JTextPane();
    private final JTextPane compilingTextPane = new JTextPane();
    private final JButton saveBtn = new JButton("Save");
    private final JMenuBar menuBar = new JMenuBar();
    private String compileCommand = "";
    private List<String> installationList = new ArrayList<>();
    private String givenName;

    public AddEventWindow(){
//        this.installFile = new FileIO(FileIO.getApplicationRootPath("installs"),filename+"_install.sh");
//        this.compileFile = new FileIO(FileIO.getApplicationRootPath("compile_commands"),filename+"_compileCmd.sh");
        this.setSize(500,500);
        this.setLayout(new BorderLayout());
        this.setTitle("Ice Station Zebra Editor");
        this.saveBtn.setPreferredSize(new Dimension(100,20));

        this.installTextPane.setDocument(new BashSyntaxHighlighting());

        JButton editInstallCommand = new JButton("Install cmd");
        editInstallCommand.setBackground(Color.white);
        editInstallCommand.setFocusPainted(false);

        JButton editCompilingCommand = new JButton("Compiling cmd");
        editCompilingCommand.setFocusPainted(false);
        editCompilingCommand.setBackground(Color.white);
        this.installTextPane.setText("#!/bin/bash\n");

        this.givenName = JOptionPane.showInputDialog(this,"Enter Compiler Name:","Ice Station Zebra", JOptionPane.PLAIN_MESSAGE);
//        this.add(inputField, BorderLayout.NORTH);

        this.menuBar.setBackground(Color.white);
        this.menuBar.add(editInstallCommand);
        this.menuBar.add(editCompilingCommand);

        this.setJMenuBar(this.menuBar);
        this.setLocationRelativeTo(null);
//        this.saveBtn.addActionListener(e -> saveToFile());

//        this.compilingTextPane.setText(this.compileFile.read());
        this.compilingTextPane.setSize(new Dimension(500,500));

//        this.installTextPane.setText(this.installFile.read());
        this.installTextPane.setSize(new Dimension(500,500));

        JPanel compileTextAreaContainer = new JPanel();
        JPanel installTextAreaContainer = new JPanel();


        this.styleJPanel(compileTextAreaContainer, this.compilingTextPane, "Compile File");
        this.styleJPanel(installTextAreaContainer, this.installTextPane,"Install File");

        this.cardConainer.add(installTextAreaContainer);


        editInstallCommand.addActionListener(e -> {
            this.cardConainer.add(installTextAreaContainer);
            this.cardConainer.remove(compileTextAreaContainer);
            editCompilingCommand.setEnabled(true);
            editInstallCommand.setEnabled(false);
        });
        editCompilingCommand.addActionListener(e -> {
            this.cardConainer.add(compileTextAreaContainer);
            this.cardConainer.remove(installTextAreaContainer);
            editInstallCommand.setEnabled(true);
            editCompilingCommand.setEnabled(false);
        });
        this.add(this.cardConainer, BorderLayout.CENTER);
        this.add(this.saveBtn,BorderLayout.SOUTH);
    }

    public void saveInfo(){
        this.compileCommand = this.compilingTextPane.getText();
        this.installationList = Arrays.stream(installTextPane.getText().split("\\r?\\n")).toList();
    }
    public void setListenerOnSaveBtn(ActionListener listener){
        this.saveBtn.addActionListener(listener);
    }
    private void styleJPanel(JPanel container, JTextPane textPane,String label){
        container.setLayout(new BorderLayout());
        container.add(textPane, BorderLayout.CENTER);
        container.add(new JLabel(label,SwingConstants.CENTER),BorderLayout.NORTH);
        container.setBackground(Color.lightGray);
    }
    public List<String> getInstallationList(){
        return this.installationList;
    }

    public String getCompileCommand() {
        return this.compileCommand;
    }

    public String getGivenName() {
        return this.givenName;
    }
}
