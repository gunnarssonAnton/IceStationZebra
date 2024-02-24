package org.example.view;

import org.example.EditFileWindow;
import org.example.Utility.GuiUtil;
import org.example.Utility.IconTextListCellRenderer;
import org.example.Utility.IszHandler;
import org.example.files.FileIO;
import org.example.models.Event;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CompilationView extends JPanel {

    private final Set<String> codeBaseSet;
    private final Set<String> compilerNamesSet;
    private final Set<String> outputSet;
    private final JButton runAllCompilersBtn = new JButton();
    private final JButton runCompilerBtn = new JButton();
    private final JButton toExecutionBtn = new JButton();
    private final GuiUtil guiUtil = new GuiUtil();
    private JList codebasesJList;
    private JList compilerNamesJlist;
    private JList outputList;
    private final FileIO compilerNameFile;
    private final IszHandler iszHandler = new IszHandler();

    private Set<Event> eventSet;
    public CompilationView(){

        this.compilerNameFile = new FileIO(FileIO.getApplicationRootPath("settings"),"compiler_names.txt");
        this.compilerNamesSet = this.iszHandler.getGivenNames();
        this.eventSet = this.iszHandler.getEvents();
//        System.out.println(iszHandler.getEvents());
        this.codeBaseSet = new HashSet<>();
        this.outputSet = new HashSet<>();
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
    }

    public Set<String> getCompilerNamesSet(){
        return this.compilerNamesSet;
    }

    private JPanel compilerNamesPanel(){
        JPanel compilerNamesPanel = new JPanel(new BorderLayout());
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");
        JPanel buttonContainer = new JPanel();
        IconTextListCellRenderer iconTextListCellRenderer = new IconTextListCellRenderer(UIManager.getIcon("FileView.floppyDriveIcon"));
        this.compilerNamesJlist = new JList(this.compilerNamesSet.toArray());

        buttonContainer.add(addNameBtn);
        buttonContainer.add(removeNameBtn);


        this.compilerNamesJlist.setCellRenderer(iconTextListCellRenderer);
        this.compilerNamesJlist.setBackground(Color.WHITE);


        addNameBtn.addActionListener(e -> {
//            this.guiUtil.showInputDialog(this, "Enter Compiler Name", this::addCompiler);
            this.showAddEventDialog();
            this.guiUtil.updateJList(this.compilerNamesJlist, this.compilerNamesSet);
        });

        removeNameBtn.addActionListener(e -> {
            String selectedValue = this.compilerNamesJlist.getSelectedValue().toString();
            this.removeCompiler(selectedValue);
            this.guiUtil.updateJList(this.compilerNamesJlist, this.compilerNamesSet);
        });

        this.compilerNamesJlist.setPreferredSize(new Dimension(250,250));
        this.compilerNamesJlist.setBorder(new LineBorder(Color.BLACK));

        compilerNamesPanel.setPreferredSize(new Dimension(300,300));
        compilerNamesPanel.setBackground(Color.white);
        compilerNamesPanel.add(new JScrollPane(this.compilerNamesJlist),BorderLayout.CENTER);
        compilerNamesPanel.add(buttonContainer, BorderLayout.SOUTH);

        this.guiUtil.setDoubleClickOnJListItem(compilerNamesJlist, this::openEditInstallFileWindow);
        return compilerNamesPanel;
    }

    private JPanel listContainer(){
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(975, 310));
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.add(this.compilerNamesPanel());
        container.add(this.codebasePanel());
        container.add(this.outputPanel());

        return container;
    }

    private JPanel outputPanel(){
        JPanel container = new JPanel(new BorderLayout());
        JButton updateBtn = new JButton("Update");
        JButton clearBtn = new JButton("Clear");
        File[] outputFiles = new File(FileIO.getApplicationRootPath("output")).listFiles();

        JPanel buttonContainer = new JPanel();
        buttonContainer.add(updateBtn);
        buttonContainer.add(clearBtn);

        this.outputSet.addAll(Arrays.stream(outputFiles).map(File::getName).toList());
        this.outputList = new JList<>(this.outputSet.toArray());

        this.outputList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.fileIcon")));
        clearBtn.addActionListener(e -> {
            for (String s : this.outputSet) {
                this.outputSet.remove(s);
            }
            this.guiUtil.updateJList(this.outputList, this.outputSet);
        });

        updateBtn.addActionListener(e-> this.updateOutput());

        this.outputList.setPreferredSize(new Dimension(250,250));
        this.outputList.setBackground(Color.white);
        this.outputList.setBorder(new LineBorder(Color.BLACK));

        container.setPreferredSize(new Dimension(300,300));
        container.setBackground(Color.white);
        container.add(new JScrollPane(outputList),BorderLayout.CENTER);
        container.add(buttonContainer, BorderLayout.SOUTH);

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

        runCompilerBtn.setText("Run Current");
        runAllCompilersBtn.setText("Run All");
        toExecutionBtn.setText("execution >");

        runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));
        runCompilerBtn.addActionListener(e->System.out.println("RUN SELECTED"));
        toExecutionBtn.addActionListener(e->System.out.println("TO EXECUTION"));
        runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));


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


    public void openEditInstallFileWindow(String filename){
//        EditFileWindow editFileWindow = new EditFileWindow(filename);
//        editFileWindow.setVisible(true);
    }

//    private Set<String> extractDataFromFile(FileIO fileIO){
//        return new HashSet<>(Arrays.asList(fileIO.read().split("\\r?\\n")));
//    }

    private void updateCompilerNameFile(){
        String str = "";
        for (String compilerName : this.compilerNamesSet) {
            str += compilerName+"\n";
        }
        compilerNameFile.write("");
        compilerNameFile.write(str);
    }

    private void updateOutput(){
        File[] outputFiles = new File(FileIO.getApplicationRootPath("output")).listFiles();
        this.outputSet.addAll(Arrays.stream(outputFiles).map(File::getName).toList());
        this.guiUtil.updateJList(this.outputList, this.outputSet);
    }

    private void addCompiler(String name){
        System.out.println("ADD: "+ name);
        FileIO fileIO = new FileIO(FileIO.getApplicationRootPath("installs"),name+"_install.sh");
        fileIO.write("#!/bin/bash\n");
        this.compilerNamesSet.add(name);
        this.updateCompilerNameFile();
    }

    private void removeCompiler(String name){
        System.out.println("REMOVE: "+name);
        this.compilerNamesSet.remove(name);
        this.updateCompilerNameFile();
    }



    private JPanel codebasePanel(){
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(new BorderLayout());
        JPanel cardContainer = new JPanel(cardLayout);
        JPanel buttonContainer = new JPanel();
        File folder = new File(FileIO.getApplicationRootPath("codebase"));
        File[] files = folder.listFiles();
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");
        this.codeBaseSet.addAll(Arrays.stream(files).map(File::getName).toList());
        this.codebasesJList = new JList<>(codeBaseSet.toArray());

        buttonContainer.add(addBtn);
        buttonContainer.add(removeBtn);
        addBtn.addActionListener(e -> {
            guiUtil.showInputDialog(this, "Enter test name:", this::addToCodebase);
            guiUtil.updateJList(this.codebasesJList,this.codeBaseSet);
        });


        this.codebasesJList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.directoryIcon")));
        this.codebasesJList.setPreferredSize(new Dimension(300,300));
        container.setPreferredSize(new Dimension(300,300));

        guiUtil.setDoubleClickOnJListItem(this.codebasesJList, this::openFileChooser);
        this.codebasesJList.setBorder(new LineBorder(Color.BLACK));
        container.setBackground(Color.white);
        cardContainer.add(this.codebasesJList);

        container.add(cardContainer,BorderLayout.CENTER);
        container.add(buttonContainer, BorderLayout.SOUTH);
        return container;
    }
    private void addToCodebase(String name){
        FileIO.createFolderIf(Path.of(FileIO.getApplicationRootPath("codebase/" + name)));
        this.codeBaseSet.add(name);
    }

    private void openFileChooser(String filename){
        JFileChooser jFileChooser = new JFileChooser(FileIO.getApplicationRootPath("codebase/"+filename));
        jFileChooser.setVisible(true);

        jFileChooser.showOpenDialog(this);
    }

    private void showAddEventDialog(){

        EditFileWindow editFileWindow = new EditFileWindow();
        editFileWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Event newEvent = new Event(
                        Event.DOCKERIMAGE,
                        editFileWindow.getCompileCommand(),
                        editFileWindow.getGivenName(),
                        new JSONArray(editFileWindow.getInstallationList())
                        );


                iszHandler.writeToIsz(newEvent);
                compilerNamesSet.add(newEvent.givenName());
                guiUtil.updateJList(compilerNamesJlist,compilerNamesSet);
            }
        });

        editFileWindow.setListenerOnSaveBtn(e ->{
                Event newEvent = new Event(
                        Event.DOCKERIMAGE,
                        editFileWindow.getCompileCommand(),
                        editFileWindow.getGivenName(),
                        new JSONArray(editFileWindow.getInstallationList())
                );

                iszHandler.writeToIsz(newEvent);
                editFileWindow.dispose();
                compilerNamesSet.add(newEvent.givenName());
                guiUtil.updateJList(compilerNamesJlist,compilerNamesSet);
                });

        editFileWindow.setVisible(true);
    }

}
