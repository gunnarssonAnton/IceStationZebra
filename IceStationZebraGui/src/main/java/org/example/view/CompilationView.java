package org.example.view;

import io.reactivex.rxjava3.core.Observable;
import org.example.EditFileWindow;
import org.example.Utility.IconTextListCellRenderer;
import org.example.files.FileIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompilationView extends JPanel {

    private final Set<String> codeBases;
    private final Set<String> compilerNamesSet;
    private final JButton runAllCompilersBtn = new JButton();
    private final JButton runCompilerBtn = new JButton();
    private final JButton toExecutionBtn = new JButton();
    private JList codebasesJList;
    private JList codebaseChildrenJlist;
    private JTextArea outputArea;
    private JList compilerNamesJlist;
    private final FileIO compilerNameFile;
    Observable EditObseravble;



    public CompilationView(){
        compilerNameFile = new FileIO(FileIO.getApplicationRootPath("settings"),"compiler_names.txt");
        compilerNamesSet = this.extractDataFromFile(compilerNameFile);
        codeBases = new HashSet<>();
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
    }

    public Set<String> getCompilerNamesSet(){
        return this.compilerNamesSet;
    }

    private JPanel compilerNamesPanel(){
        JPanel compilerNamesPanel = new JPanel();
        IconTextListCellRenderer iconTextListCellRenderer = new IconTextListCellRenderer(UIManager.getIcon("FileView.floppyDriveIcon"));
        var listModel = new DefaultListModel();
        listModel.addAll(compilerNamesSet);
        this.compilerNamesJlist = new JList(listModel);
        this.compilerNamesJlist.setCellRenderer(iconTextListCellRenderer);
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");
        compilerNamesJlist.setBackground(Color.WHITE);


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

        this.setDubbleClickOnItem(compilerNamesJlist, this::openEditInstallFileWindow);
        return compilerNamesPanel;
    }

    private JPanel listContainer(){
        JPanel container = new JPanel();
        JPanel codeBasePanel = new JPanel();
        JPanel outputPanel = new JPanel();

//        JList codeBase = new JList(this.codeBases.toArray());
        TextArea output = new TextArea();

        container.setBackground(Color.lightGray);

//        codeBase.setPreferredSize(new Dimension(300,300));
        output.setPreferredSize(new Dimension(300,300));

        output.setText("Output");

//        codeBasePanel.add(codeBase);
        outputPanel.add(output);

        container.add(new JScrollPane(this.compilerNamesPanel()));
        container.add(this.codebasePanel());
        container.add(this.outputPanel());

        return container;
    }

    private JPanel outputPanel(){
        JPanel container = new JPanel();
        JButton clearBtn = new JButton("Clear");
        this.outputArea= new JTextArea();
        this.outputArea.setEditable(false);
        this.outputArea.setPreferredSize(new Dimension(300,300));
        container.setLayout(new BorderLayout());
        clearBtn.setPreferredSize(new Dimension(50,50));
        container.add(this.outputArea,BorderLayout.CENTER);
        container.add(clearBtn,BorderLayout.SOUTH);
        clearBtn.addActionListener(e -> this.outputArea.setText(""));
        return container;
    }

    private JPanel runBtnContainer(){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container.setBorder(new EmptyBorder(50,50,50,50));
        container.setSize(50,50);

        runAllCompilersBtn.setPreferredSize(new Dimension(150,80));
        runCompilerBtn.setPreferredSize(new Dimension(150,80));
        toExecutionBtn.setPreferredSize(new Dimension(150,80));

        runCompilerBtn.addActionListener(e -> {
            String content = compilerNamesJlist.getSelectedValue() + "\n" + codebasesJList.getSelectedValue()+"\n";
            this.outputArea.setText(content);


        });
        runCompilerBtn.setText("Run Current");
        runAllCompilersBtn.setText("Run All");
        toExecutionBtn.setText("execution >");

        runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));
        runCompilerBtn.addActionListener(e->System.out.println("RUN SELECTED"));
        toExecutionBtn.addActionListener(e->System.out.println("TO EXECUTION"));
//        runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));


        container.add(runCompilerBtn);
        container.add(runAllCompilersBtn);
        container.add(toExecutionBtn);

        return container;
    }

    public void setOnClick(ActionListener l){
        this.toExecutionBtn.addActionListener(l);
    }
    public void runAllOnClick(ActionListener l){
        this.runAllCompilersBtn.addActionListener(l);
    }

    public void setDubbleClickOnItem(JList jList, Consumer<String> callback){
            jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    var selectedValue = jList.getSelectedValue().toString();
                    callback.accept(selectedValue);
                }
            }
        });

    }

    public void openEditInstallFileWindow(String filename){
        EditFileWindow editFileWindow = new EditFileWindow(filename);
        editFileWindow.setVisible(true);
    }
    private Set<String> extractDataFromFile(FileIO fileIO){
        return new HashSet<>(Arrays.asList(fileIO.read().split("\\r?\\n")));
    }

    private void updateList(){
        String str = "";
        for (String compilerName : this.compilerNamesSet) {
            str += compilerName+"\n";
        }
        compilerNameFile.write("");
        compilerNameFile.write(str);
    }

    private void addCompiler(String name){
        System.out.println("ADD: "+ name);
        FileIO fileIO = new FileIO(FileIO.getApplicationRootPath("installs"),name+"_install.sh");
        fileIO.write("#!/bin/bash\n");
        this.compilerNamesSet.add(name);
        this.updateList();
    }
    private void removeCompiler(String name){
        System.out.println("REMOVE: "+name);
        this.compilerNamesSet.remove(name);
        this.updateList();
    }

    private void showCompilerInput(){
         String input = JOptionPane.showInputDialog(this,"Enter Compiler name:", "Ice Station Zebra", JOptionPane.PLAIN_MESSAGE);
         this.addCompiler(input);
    }


    private JPanel codebasePanel(){
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        File folder = new File(FileIO.getApplicationRootPath("codebase"));
        File[] files = folder.listFiles();
        var listModel = new DefaultListModel();
        for (File file : files) {
            listModel.addElement(file.getName());
        }

        this.codebasesJList = new JList<>(listModel);
        this.codebasesJList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.directoryIcon")));
        this.codebasesJList.setPreferredSize(new Dimension(300,300));
        container.setPreferredSize(new Dimension(300,300));

        DefaultListModel childListModel = new DefaultListModel<>();
        this.codebaseChildrenJlist = new JList();
        this.setDubbleClickOnItem(this.codebasesJList, parentFolder -> {
            System.out.println(parentFolder);
            File[] childFolders = new File(FileIO.getApplicationRootPath("codebase/"+parentFolder)).listFiles();
//            assert childFolders != null;
            for (File childFile : childFolders) {
                childListModel.addElement(childFile.getName());
            }

            this.codebaseChildrenJlist.setModel(childListModel);
            cardLayout.next(container);
        });

        this.codebaseChildrenJlist.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.fileIcon")));
        container.setBackground(Color.white);

        container.add(this.codebasesJList);
        container.add(this.codebaseChildrenJlist);
        return container;
    }

    private void testFrame(String filename){
        JFrame jFrame = new JFrame();
        jFrame.add(new JLabel(filename));
        jFrame.setSize(new Dimension(300,300));
        jFrame.setVisible(true);
    }
}
