package org.example;

import org.example.files.FileIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompilationView extends JPanel {

    List<String> codeBases;
    List<String> compilerNamesArray;
    JButton runAllCompilersBtn = new JButton();
    CompilationView(){
        compilerNamesArray = extractDataFromFile("compiler_names.txt");
        codeBases = new ArrayList<>();


        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
    }

    private JPanel compilerNamesPanel(){
        JPanel compilerNamesPanel = new JPanel();
        JList compilerNamesJlist = new JList(this.compilerNamesArray.toArray());
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");

        addNameBtn.addActionListener(e -> System.out.println("Add"));
        removeNameBtn.addActionListener(e -> {
            var selectedIndex = compilerNamesJlist.getSelectedIndex();
            this.compilerNamesArray.remove(this.compilerNamesArray.get(selectedIndex));
            this.removeFromFile("compiler_names.txt");


            compilerNamesJlist.setListData(this.compilerNamesArray.toArray());
            compilerNamesJlist.updateUI();
            compilerNamesJlist.revalidate();
            compilerNamesJlist.repaint();

        });


        compilerNamesJlist.setPreferredSize(new Dimension(300,250));
        compilerNamesPanel.setPreferredSize(new Dimension(300,300));
        compilerNamesPanel.setBackground(Color.white);
        compilerNamesPanel.add(compilerNamesJlist);
        compilerNamesPanel.add(addNameBtn);
        compilerNamesPanel.add(removeNameBtn);

        return compilerNamesPanel;
    }

    private JPanel listContainer(){
        JPanel container = new JPanel();
        JPanel codeBasePanel = new JPanel();
        JPanel outputPanel = new JPanel();

        JList codeBase = new JList(this.codeBases.toArray());
        TextArea output = new TextArea();

        container.setBackground(Color.lightGray);

        codeBase.setPreferredSize(new Dimension(300,300));
        output.setPreferredSize(new Dimension(300,300));

        output.setText("Output");

        codeBasePanel.add(codeBase);
        outputPanel.add(output);

        container.add(this.compilerNamesPanel());
        container.add(codeBasePanel);
        container.add(outputPanel);

        return container;
    }

    private JPanel runBtnContainer(){
        JPanel container = new JPanel();
        JButton runCompilerBtn = new JButton();

        container.setLayout(new FlowLayout(FlowLayout.RIGHT));

        container.setBorder(new EmptyBorder(50,50,50,50));
        container.setSize(50,50);

        runAllCompilersBtn.setPreferredSize(new Dimension(150,80));
        runCompilerBtn.setPreferredSize(new Dimension(150,80));


        runCompilerBtn.setText("Run Current");
        runAllCompilersBtn.setText("Run All");


        runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));


        container.add(runCompilerBtn);
        container.add(runAllCompilersBtn);

        return container;
    }

    protected void setOnClick(ActionListener l){
        this.runAllCompilersBtn.addActionListener(l);
    }

    private List<String> extractDataFromFile(String filename){
        FileIO fileIO = new FileIO(FileIO.getApplicationRootPath("settings"),filename);
        return List.of(fileIO.read().split("\n"));
    }

    private void removeFromFile(String filename){
        String str = "";
        for (String compilerName : compilerNamesArray) {
            str += compilerName+"\n";
        }
        writeToOutputStream(filename, str);
        System.out.printf("Item: has been removed from %s%n", filename);
    }
    private InputStream getFileAsInputStream(String filename){
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(filename);

        if (ioStream == null){
            throw  new IllegalArgumentException(filename+ " is not found");
        }
        return ioStream;
    }

    void writeToOutputStream(String filename, String value){
        try {

            OutputStream fileOutputStream = new FileOutputStream(filename);
            fileOutputStream.write(value.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
