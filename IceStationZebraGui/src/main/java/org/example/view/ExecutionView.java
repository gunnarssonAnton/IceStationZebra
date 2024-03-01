package org.example.view;

import org.example.Utility.IconTextListCellRenderer;
import org.example.files.FileIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ExecutionView extends JPanel {
    private final JList executionJList = new JList();
    private final Set<String> executionSet = new HashSet<>();
    private final JButton executeBtn = new JButton("Execute");
    JButton tempBtn = new JButton("BACK");

    public ExecutionView(){

        this.extractSetFromFile();
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.add(tempBtn);
        this.add(this.getExecutionJListContainer(),BorderLayout.NORTH);
        this.add(this.getExecutionBtnContainer(),BorderLayout.SOUTH);
    }

    private void extractSetFromFile(){
        File outputDic = new File(FileIO.getApplicationRootPath("output"));
        File[] outputFiles = outputDic.listFiles();
        Set<String> outputFileNames = Arrays.stream(outputFiles).toList().stream()
                                                            .map(File::getName)
                                                            .collect(Collectors.toSet());
        this.executionSet.addAll(outputFileNames);
    }
    private JPanel getExecutionBtnContainer(){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container.setBorder(new EmptyBorder(50,50,50,50));
        container.setSize(50,50);
        this.executeBtn.setPreferredSize(new Dimension(150,80));
        this.executeBtn.addActionListener(e-> System.out.println("Execute..."));
        container.add(this.executeBtn);
        return container;
    }

    private JPanel getExecutionJListContainer(){
        JPanel container = new JPanel(new BorderLayout());
        this.executionJList.setListData(executionSet.toArray());
        this.executionJList.setPreferredSize(new Dimension(250,250));
        this.executionJList.setBackground(Color.white);
        this.executionJList.setBorder(new LineBorder(Color.BLACK));
        this.executionJList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("")));
        container.setPreferredSize(new Dimension(300,300));
        container.setBackground(Color.white);
        container.add(new JScrollPane(this.executionJList),BorderLayout.CENTER);
        return container;
    }


    public void setOnClick(ActionListener l){
        this.tempBtn.addActionListener(l);
    }
}
