package org.example.view;

import org.example.AddEventWindow;
import org.example.EditIceEventWindow;
import org.example.Utility.GuiUtil;
import org.example.Utility.IceHandler;
import org.example.Utility.IconTextListCellRenderer;
import org.example.files.FileIO;
import org.example.models.Event;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CompilationView extends JPanel {

    private final Set<String> codeBaseSet;
    private final Set<String> eventNamesSet;
    private final Set<String> outputSet;
    private final JButton runAllCompilersBtn = new JButton();
    private final JButton runCompilerBtn = new JButton();
    private final JButton toExecutionBtn = new JButton();
    private final GuiUtil guiUtil = new GuiUtil();
    private JList codebasesJList;
    private JList eventNamesJlist;
    private JList outputList;
    public Event selectedEvent;
//    private final FileIO compilerNameFile;
    private final IceHandler iceHandler = IceHandler.getInstance();
    public CompilationView(){
        this.eventNamesSet = this.iceHandler.getEventNames();
        this.codeBaseSet = new HashSet<>();
        this.outputSet = new HashSet<>();

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(this.listContainer(),BorderLayout.NORTH);
        this.add(this.runBtnContainer(),BorderLayout.CENTER);
        this.eventNamesJlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String eventName = (String)((JList<?>)e.getSource()).getSelectedValue();
                selectedEvent = IceHandler.getInstance().getSpecificEvent(eventName);
            }
        });
    }

    public Set<String> getEventNamesSet(){
        return this.eventNamesSet;
    }

    private JPanel compilerNamesPanel(){
        JPanel compilerNamesPanel = new JPanel(new BorderLayout());
        JButton addNameBtn = new JButton("Add");
        JButton removeNameBtn = new JButton("Remove");
        JPanel buttonContainer = new JPanel();
        IconTextListCellRenderer iconTextListCellRenderer = new IconTextListCellRenderer(UIManager.getIcon("FileView.floppyDriveIcon"));
        this.eventNamesJlist = new JList(this.eventNamesSet.toArray());

        buttonContainer.add(addNameBtn);
        buttonContainer.add(removeNameBtn);


        this.eventNamesJlist.setCellRenderer(iconTextListCellRenderer);
        this.eventNamesJlist.setBackground(Color.WHITE);


        addNameBtn.addActionListener(e -> {
            this.showAddEventDialog();
            this.guiUtil.updateJList(this.eventNamesJlist, this.eventNamesSet);
        });

        removeNameBtn.addActionListener(e -> {
            String selectedValue = this.eventNamesJlist.getSelectedValue().toString();
            this.guiUtil.showConfrimDialog(this,selectedValue,this::removeCompiler);
            this.guiUtil.updateJList(this.eventNamesJlist, this.eventNamesSet);
        });

//        this.eventNamesJlist.setPreferredSize(new Dimension(250,250));
        this.eventNamesJlist.setBorder(new LineBorder(Color.BLACK));

        compilerNamesPanel.setPreferredSize(new Dimension(300,300));
        compilerNamesPanel.setBackground(Color.white);
        compilerNamesPanel.add(new JScrollPane(this.eventNamesJlist),BorderLayout.CENTER);
        compilerNamesPanel.add(buttonContainer, BorderLayout.SOUTH);

        this.guiUtil.setDoubleClickOnJListItem(eventNamesJlist, this::openEditIceEventWindow);
        return compilerNamesPanel;
    }

    /**
     * Container for all JLists containers
     * @return a container
     */
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

        clearBtn.addActionListener(e -> this.clearOutputDir());

        updateBtn.addActionListener(e-> this.updateOutputJList());

//        this.outputList.setPreferredSize(new Dimension(250,250));
        this.outputList.setBackground(Color.white);
        this.outputList.setBorder(new LineBorder(Color.BLACK));

        container.setPreferredSize(new Dimension(300,300));
        container.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(this.outputList);
        this.outputList.setLayoutOrientation(JList.VERTICAL);

        container.add(scrollPane,BorderLayout.CENTER);
        container.add(buttonContainer, BorderLayout.SOUTH);

        return container;
    }

    private JPanel runBtnContainer(){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container.setBorder(new EmptyBorder(50,50,50,50));
        container.setSize(50,50);

        this.runAllCompilersBtn.setPreferredSize(new Dimension(150,80));
        this.runAllCompilersBtn.setFocusPainted(false);
        this.runCompilerBtn.setPreferredSize(new Dimension(150,80));
        this.runCompilerBtn.setFocusPainted(false);
        this.toExecutionBtn.setPreferredSize(new Dimension(150,80));
        this.toExecutionBtn.setFocusPainted(false);
        this.runCompilerBtn.setText("Run Current");
        this.runAllCompilersBtn.setText("Run All");
        this.toExecutionBtn.setText("Execution >");

        this.runAllCompilersBtn.addActionListener(e->System.out.println("RUN ALL"));
        this.runCompilerBtn.addActionListener(e->System.out.println("RUN SELECTED"));
        this.toExecutionBtn.addActionListener(e->System.out.println("TO EXECUTION"));


        container.add(this.runCompilerBtn);
        container.add(this.runAllCompilersBtn);
        container.add(this.toExecutionBtn);

        return container;
    }

    public void setOnClick(ActionListener l){
        this.toExecutionBtn.addActionListener(l);
    }
    public void setOnCompileClick(ActionListener l){
        this.runCompilerBtn.addActionListener(l);
    }
    public void setOnCompileAllClick(ActionListener l){
        this.runAllCompilersBtn.addActionListener(l);
    }


    public void openEditIceEventWindow(String eventName){
        EditIceEventWindow editIceEventWindow = new EditIceEventWindow(this.iceHandler.getSpecificEvent(eventName));
        editIceEventWindow.setVisible(true);
        System.out.println(this.iceHandler.getSpecificEvent(eventName));
    }


    public void updateOutputJList(){
        File[] outputFiles = new File(FileIO.getApplicationRootPath("output")).listFiles();
        this.outputSet.addAll(Arrays.stream(outputFiles).map(File::getName).toList());
        this.guiUtil.updateJList(this.outputList, this.outputSet);
    }

    private void clearOutputDir(){
        File[] outputFiles = new File(FileIO.getApplicationRootPath("output")).listFiles();
        for (File outputFile : outputFiles) {
            this.outputSet.remove(outputFile.getName());
            if (!outputFile.isDirectory()){
                outputFile.delete();
            }
        }
        this.guiUtil.updateJList(this.outputList, this.outputSet);
    }

    /**
     * Removes an event from the list and from the config
     * @param name name of event
     */
    private void removeCompiler(String name){
        this.eventNamesSet.remove(name);
        this.iceHandler.removeEvent(name);
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
            this.guiUtil.showInputDialog(this, "Enter test name:", this::addToCodebase);
            this.guiUtil.updateJList(this.codebasesJList,this.codeBaseSet);
        });

        removeBtn.addActionListener(e->{
            System.out.println(codebasesJList.getSelectedValue().toString());
            this.guiUtil.showConfrimDialog(this, codebasesJList.getSelectedValue().toString(),System.out::println);
            this.guiUtil.updateJList(this.eventNamesJlist, this.eventNamesSet);
        });


        this.codebasesJList.setCellRenderer(new IconTextListCellRenderer(UIManager.getIcon("FileView.directoryIcon")));
//        this.codebasesJList.setPreferredSize(new Dimension(300,300));
        container.setPreferredSize(new Dimension(300,300));

        this.guiUtil.setDoubleClickOnJListItem(this.codebasesJList, this::openFileChooser);
        this.codebasesJList.setBorder(new LineBorder(Color.BLACK));
        container.setBackground(Color.white);
        cardContainer.add(new JScrollPane(this.codebasesJList));

        container.add(cardContainer,BorderLayout.CENTER);
        container.add(buttonContainer, BorderLayout.SOUTH);
        return container;
    }

    /**
     * Add a folder to the codebase
     * @param name the name
     */
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
        AddEventWindow addEventWindow = new AddEventWindow();
        addEventWindow.setVisible(true);
        eventNamesSet.add(addEventWindow.getGivenName());
        this.guiUtil.updateJList(eventNamesJlist, eventNamesSet);
    }
    private void cleanUpEventSet(){
        for (String s : this.eventNamesSet) {
            if (this.iceHandler.getSpecificEvent(s) == null){
                System.out.println("dddddddddddddd: "+s);
                this.eventNamesSet.remove(s);
            }
        }
        this.guiUtil.updateJList(eventNamesJlist, eventNamesSet);
    }

}
