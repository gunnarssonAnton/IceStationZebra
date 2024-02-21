package org.example.view;

import org.example.files.FileIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class CompilationView extends JPanel {

    private final Set<String> codeBases;
    private final Set<String> compilerNamesSet;
    private final JButton runAllCompilersBtn = new JButton();
    private final FileIO compilerNameFile;

    public CompilationView(){
        compilerNameFile = new FileIO(FileIO.getApplicationRootPath("settings"),"compiler_names.txt");
        compilerNamesSet = extractDataFromFile(compilerNameFile);
        codeBases = new HashSet<>();

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
    }

    private JPanel compilerNamesPanel(){
        JPanel compilerNamesPanel = new JPanel();
        JList compilerNamesJlist = new JList(this.compilerNamesSet.toArray());
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");

        addNameBtn.addActionListener(e -> {
            this.showCompilerInput();
            compilerNamesJlist.setListData(this.compilerNamesSet.toArray());
            compilerNamesJlist.updateUI();
        });
        removeNameBtn.addActionListener(e -> {
            var selectedValue = compilerNamesJlist.getSelectedValue().toString();
            System.out.println(selectedValue);
            this.removeCompiler(selectedValue);

            compilerNamesJlist.setListData(this.compilerNamesSet.toArray());
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

    private Set<String> extractDataFromFile(FileIO fileIO){
        return new HashSet<>(Arrays.asList(fileIO.read().split("\\r?\\n")));
    }

    private void updateList(){
        String str = "";
        for (String compilerName : this.compilerNamesSet) {
//            System.out.println(str);
            str += compilerName+"\n";
        }
        compilerNameFile.write("");
        compilerNameFile.write(str);
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

    private void addCompiler(String name){
        System.out.println("ADD: "+ name);
        FileIO fileIO = new FileIO(FileIO.getApplicationRootPath("installs"),name+"_install.sh");
        fileIO.write("");
        this.compilerNamesSet.add(name);
        this.updateList();
    }
    private void removeCompiler(String name){
        System.out.println("REMOVE: "+name);
        this.compilerNamesSet.remove(name);
        this.updateList();
    }

    private void showCompilerInput(){

         String input = JOptionPane.showInputDialog(this,"Enter Compiler name:", "Ice Station Zebra",JOptionPane.PLAIN_MESSAGE);
         this.addCompiler(input);
    }

}
