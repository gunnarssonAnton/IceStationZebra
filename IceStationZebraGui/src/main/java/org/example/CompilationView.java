package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CompilationView extends JPanel {

    List<String> codeBases;
    List<String> compilerNamesArray;
    JButton runAllCompilersBtn = new JButton();
    CompilationView(){
        compilerNamesArray = extractDataFromFile("compiler_Names.txt");
        codeBases = extractDataFromFile("code_bases.txt");


        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
    }

    private JPanel compilerNamesPanel(){
//        List<String> compilerNamesArray = new ArrayList<>(List.of("oracle", "open", "eclipse"));
        JPanel compilerNamesPanel = new JPanel();
        JList compilerNamesJlist = new JList(this.compilerNamesArray.toArray());
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");

        addNameBtn.addActionListener(e -> System.out.println("Add"));
        removeNameBtn.addActionListener(e -> {
            var selectedIndex = compilerNamesJlist.getSelectedIndex();
            this.compilerNamesArray.remove(this.compilerNamesArray.get(selectedIndex));
            System.out.println(Arrays.toString(this.compilerNamesArray.toArray()));
            this.removeFromFile("compiler_Names.txt");


            compilerNamesJlist.setListData(this.compilerNamesArray.toArray());
            compilerNamesJlist.updateUI();
            compilerNamesJlist.revalidate();
            compilerNamesJlist.repaint();

        });


        compilerNamesJlist.setPreferredSize(new Dimension(300,250));
        compilerNamesPanel.setPreferredSize(new Dimension(300,300));
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


        container.setBackground(Color.yellow);


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

        container.setBackground(Color.blue);
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

    private java.util.List<String> extractDataFromFile(String fileName){
        List<String> stringList = new ArrayList<>();
        try {
            File file = new File(String.format("src/main/resources/%s", fileName));
            Scanner scanner = new Scanner(file);
            String line;

            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                System.out.println(line);
                stringList.add(line);
            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringList;
    }

    private void removeFromFile(String fileName){
        String str = "";
        for (String compilerName : compilerNamesArray) {
            str += compilerName+"\n";
        }
        System.out.println(str);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("src/main/resources/%s", fileName)));
            writer.write(str);
            writer.close();

            System.out.println("ALL good in the hood");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
