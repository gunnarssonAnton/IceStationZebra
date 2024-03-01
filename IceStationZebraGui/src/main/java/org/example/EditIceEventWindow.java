package org.example;

import org.example.Utility.BashSyntaxHighlighting;
import org.example.Utility.IceHandler;
import org.example.models.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditIceEventWindow extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel textPaneContainer = new JPanel(cardLayout);
    private final JTextPane installTextPane = new JTextPane();
    private final JTextPane compilingTextPane = new JTextPane();
    private final JButton saveBtn = new JButton("Save");
    private final JMenuBar menuBar = new JMenuBar();
    private final IceHandler iceHandler = IceHandler.getInstance();
    private final String oldEventName;
    private String compileTextLabel = "Compile File";
    private String installTextLabel = "Install File";
    public EditIceEventWindow(Event event) {
        this.oldEventName = event.givenName();
        this.configureTextPanes(event);
        this.setTitle("Edit: "+this.oldEventName);
        this.setSize(500,500);
        this.setLayout(new BorderLayout());
        this.saveBtn.setPreferredSize(new Dimension(100,20));
        this.setListener();

        JButton editInstallCommand = new JButton("Install cmd");
        editInstallCommand.setBackground(Color.white);
        editInstallCommand.setFocusPainted(false);

        JButton editCompilingCommand = new JButton("Compiling cmd");
        editCompilingCommand.setFocusPainted(false);
        editCompilingCommand.setBackground(Color.white);

        JPanel compileTextAreaContainer = new JPanel();
        JPanel installTextAreaContainer = new JPanel();


        this.styleJPanel(compileTextAreaContainer, this.compilingTextPane, this.compileTextLabel);
        this.styleJPanel(installTextAreaContainer, this.installTextPane,this.installTextLabel);

        this.textPaneContainer.add(installTextAreaContainer);

        //Disables change the content in to container and disables the button you just clicked on
        editInstallCommand.addActionListener(e -> {
            this.textPaneContainer.add(installTextAreaContainer);
            this.textPaneContainer.remove(compileTextAreaContainer);
            editCompilingCommand.setEnabled(true);
            editInstallCommand.setEnabled(false);
        });
        //Disables change the content in to container and disables the button you just clicked on
        editCompilingCommand.addActionListener(e -> {
            this.textPaneContainer.add(compileTextAreaContainer);
            this.textPaneContainer.remove(installTextAreaContainer);
            editInstallCommand.setEnabled(true);
            editCompilingCommand.setEnabled(false);
        });

        this.menuBar.setBackground(Color.white);
        this.menuBar.add(editInstallCommand);
        this.menuBar.add(editCompilingCommand);

        this.setJMenuBar(this.menuBar);
        this.setLocationRelativeTo(null);

        this.add(this.textPaneContainer, BorderLayout.CENTER);
        this.add(this.saveBtn,BorderLayout.SOUTH);
    }

    /**
     * Styles the JPanels and adds a text pane to the JPanel
     * @param container the JPanel
     * @param textPane the text pane
     * @param label the label displayed in the JPanel
     */
    private void styleJPanel(JPanel container, JTextPane textPane,String label){
        container.setLayout(new BorderLayout());
        container.add(textPane, BorderLayout.CENTER);
        container.add(new JLabel(label,SwingConstants.CENTER),BorderLayout.NORTH);
        container.setBackground(Color.lightGray);
    }

    /**
     * used to Configure the Text panes
     * @param event a parameter
     */
    private void configureTextPanes(Event event){
        String installationContent = "";
        for (Object o : event.installation()) {
            installationContent += o.toString() + "\n";
        }

        this.installTextPane.setSize(new Dimension(500,500));
        this.installTextPane.setDocument(new BashSyntaxHighlighting());
        this.installTextPane.setText(installationContent);


        this.compilingTextPane.setSize(new Dimension(500,500));
        this.compilingTextPane.setText(event.compileCommand());
    }

    private void saveToIce(){
        List<String> installationList = Arrays.stream(installTextPane.getText().split("\\r?\\n")).toList();
        Event modifiedEvent = new Event(
                Event.DOCKERIMAGE,
                this.compilingTextPane.getText(),
                this.oldEventName,
                installationList,
                new ArrayList<>());
        this.iceHandler.modifyEvent(this.oldEventName, modifiedEvent);
        this.dispose();
    }

    private void setListener(){
        this.saveBtn.addActionListener(e -> this.saveToIce());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToIce();
            }
        });

        this.installTextPane.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                setTitle("Edit: "+ oldEventName +"*");
            }
        });
    }

}
