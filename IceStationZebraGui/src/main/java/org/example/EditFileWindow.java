package org.example;

import org.example.Utility.BashSyntaxHighlighting;
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
    private final JTextPane installTextPane = new JTextPane();
    private final JTextPane compilingTextPane = new JTextPane();
    private final JButton saveBtn = new JButton("Save");
    private final JMenuBar menuBar = new JMenuBar();

    public EditFileWindow(String filename){
        this.installFile = new FileIO(FileIO.getApplicationRootPath("installs"),filename+"_install.sh");
        this.compileFile = new FileIO(FileIO.getApplicationRootPath("compile_commands"),filename+"_compileCmd.sh");
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


        this.menuBar.setBackground(Color.white);
        this.menuBar.add(editInstallCommand);
        this.menuBar.add(editCompilingCommand);


        this.setJMenuBar(this.menuBar);
        this.setLocationRelativeTo(null);
        this.saveBtn.addActionListener(e -> saveToFile());

        this.compilingTextPane.setText(this.compileFile.read());
        this.compilingTextPane.setSize(new Dimension(500,500));

        this.installTextPane.setText(this.installFile.read());
        this.installTextPane.setSize(new Dimension(500,500));

        JPanel compileTextAreaContainer = new JPanel();
        JPanel installTextAreaContainer = new JPanel();

        compileTextAreaContainer.setLayout(new BorderLayout());
        compileTextAreaContainer.add(this.compilingTextPane,BorderLayout.CENTER);
        compileTextAreaContainer.add(new JLabel("Compile File",SwingConstants.CENTER),BorderLayout.NORTH);
        compileTextAreaContainer.setBackground(Color.lightGray);

        installTextAreaContainer.setLayout(new BorderLayout());
        installTextAreaContainer.add(this.installTextPane, BorderLayout.CENTER);
        installTextAreaContainer.add(new JLabel("Install File",SwingConstants.CENTER),BorderLayout.NORTH);
        installTextAreaContainer.setBackground(Color.lightGray);


        this.cardConainer.add(installTextAreaContainer);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile();
            }

        });

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

    private void saveToFile(){
        String installContent = installTextPane.getText();
        installFile.write(installContent);

        String compileContent = compilingTextPane.getText();
        compileFile.write(compileContent);

        this.dispose();
    }

}
