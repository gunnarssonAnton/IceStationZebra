package org.example;

import org.example.Utility.BashSyntaxHighlighting;
import org.example.Utility.IceHandler;
import org.example.models.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEventWindow extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private JPanel cardConainer = new JPanel(cardLayout);
    private final JTextPane installTextPane = new JTextPane();
    private final JTextPane compilingTextPane = new JTextPane();
    private final JButton saveBtn = new JButton("Save");
    private final JMenuBar menuBar = new JMenuBar();
    private String givenName;

    private final IceHandler iceHandler = IceHandler.getInstance();

    public AddEventWindow(){
        JButton editInstallCommand = new JButton("Install cmd");
        JButton editCompilingCommand = new JButton("Compiling cmd");
        this.setSize(500,500);
        this.setLayout(new BorderLayout());
        this.setTitle("Ice Station Zebra Editor");
        this.saveBtn.setPreferredSize(new Dimension(100,20));
        this.installTextPane.setDocument(new BashSyntaxHighlighting());

        this.givenName = JOptionPane.showInputDialog(this,"Enter Compiler Name:","Ice Station Zebra", JOptionPane.PLAIN_MESSAGE);

        this.saveBtn.addActionListener(e->this.saveEvent());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveEvent();
            }
        });

        editInstallCommand.setBackground(Color.white);
        editInstallCommand.setFocusPainted(false);

        editCompilingCommand.setFocusPainted(false);
        editCompilingCommand.setBackground(Color.white);

        this.menuBar.setBackground(Color.white);
        this.menuBar.add(editInstallCommand);
        this.menuBar.add(editCompilingCommand);

        this.setJMenuBar(this.menuBar);
        this.setLocationRelativeTo(null);


        this.compilingTextPane.setSize(new Dimension(500,500));
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

    public void saveEvent(){
        String compileCommand = this.compilingTextPane.getText();
        List<String> installationList = Arrays.stream(installTextPane.getText().split("\\r?\\n")).toList();
        Event newEvent = new Event(
                Event.DOCKERIMAGE,
                   compileCommand,
                this.givenName,
                installationList,
                new ArrayList<>()
            );
        this.iceHandler.addEvent(newEvent);
        this.dispose();
    }
    private void styleJPanel(JPanel container, JTextPane textPane,String label){
        container.setLayout(new BorderLayout());
        container.add(textPane, BorderLayout.CENTER);
        container.add(new JLabel(label,SwingConstants.CENTER),BorderLayout.NORTH);
        container.setBackground(Color.lightGray);
    }


    public String getGivenName() {
        return givenName;
    }
}
