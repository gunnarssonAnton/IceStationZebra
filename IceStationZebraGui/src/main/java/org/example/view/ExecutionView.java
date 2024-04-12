package org.example.view;

import org.example.Utility.GuiUtil;
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
    private final JButton prepBtn = new JButton("Prep");
    private final JButton goBtn = new JButton("Go");
    private final JButton backBtn = new JButton("< Go Back");
    private final GuiUtil guiUtil = new GuiUtil();
    private String selectedValue;

    private final JTextField amountOfRoundInput = new JTextField();

    public ExecutionView(){

        this.extractSetFromFile();
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));


        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.buttonContainer(),BorderLayout.CENTER);

        this.executionJList.addListSelectionListener(e ->
                selectedValue = (String)((JList<?>)e.getSource()).getSelectedValue());

    }

    public void extractSetFromFile(){
        File outputDic = new File(FileIO.getApplicationRootPath("output"));
        File[] outputFiles = outputDic.listFiles();
        Set<String> outputFileNames = Arrays.stream(outputFiles).toList().stream()
                                                            .map(File::getName)
                                                            .collect(Collectors.toSet());
        this.executionSet.addAll(outputFileNames);
        this.guiUtil.updateJList(this.executionJList, this.executionSet);
    }
    /**
     * Container for all JLists containers
     * @return a container
     */
    private JPanel listContainer(){
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(975, 310));
        container.setLayout(new FlowLayout(FlowLayout.LEFT));
        container.add(this.getExecutionJListContainer());
        container.add(this.getAmountOfRoundsInputContainer());
        return container;
    }
    private JPanel getExecutionJListContainer(){
        JPanel container = new JPanel(new BorderLayout());
        this.executionJList.setListData(executionSet.toArray());
//        this.executionJList.setPreferredSize(new Dimension(250,500));
        this.executionJList.setBackground(Color.white);
        this.executionJList.setBorder(new LineBorder(Color.BLACK));
        this.executionJList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.fileIcon")));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(this.executionJList);
        executionJList.setLayoutOrientation(JList.VERTICAL);

        container.setPreferredSize(new Dimension(300,300));
        container.setBackground(Color.white);
        container.add(scrollPane,BorderLayout.CENTER);
        return container;
    }

    private JPanel buttonContainer(){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container.setBorder(new EmptyBorder(50,50,50,50));
        container.setSize(50,50);

        this.backBtn.setPreferredSize(new Dimension(150,80));
        this.backBtn.setFocusPainted(false);
        this.prepBtn.setPreferredSize(new Dimension(150,80));
        this.prepBtn.setFocusPainted(false);
        this.goBtn.setPreferredSize(new Dimension(150,80));
        this.goBtn.setFocusPainted(false);


        this.backBtn.addActionListener(e->System.out.println("Execute"));
        this.prepBtn.addActionListener(e->System.out.println("Prep"));
        this.goBtn.addActionListener(e->System.out.println("Go"));


        container.add(this.backBtn);
        container.add(this.prepBtn);
        container.add(this.goBtn);

        return container;
    }

    private JPanel getAmountOfRoundsInputContainer(){
        JPanel container = new JPanel();
        this.amountOfRoundInput.setPreferredSize(new Dimension(50,20));
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Enter amount of rounds"),BorderLayout.NORTH);
        container.add(this.amountOfRoundInput, BorderLayout.SOUTH);

        return container;
    }

    public String getSelectedValue() {
        return this.selectedValue;
    }

    public void setBackOnClick(ActionListener l){
        this.backBtn.addActionListener(l);
    }
    public void setPrepOnClick(ActionListener l){
        this.prepBtn.addActionListener(l);
    }

    public void setGoOnClick(ActionListener l){
        this.goBtn.addActionListener(l);
    }

    public String getAmountOfRounds(){
        var amountOfRound = this.amountOfRoundInput.getText();
        if (amountOfRound.length() == 0){
            JOptionPane.showMessageDialog(this,"NO amounts of round entered", "Error", JOptionPane.ERROR_MESSAGE);
            return "5";
        }
        else {
            return amountOfRound;
        }
    }
}
