package org.example.Utility;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.function.Consumer;

public class GuiUtil {
    public static final String GUI_TITLE = "Ice Station Zebra";

    /**
     * Updates a given JList
     * @param list the JList you want to update
     * @param sets the data set you want to give the JList
     */
    public void updateJList(JList list, Set<String> sets){
        list.setListData(sets.toArray());
        list.updateUI();
        list.revalidate();
        list.repaint();
    }

    /**
     * Applys a double click listener on the given JList
     * @param jList the JList you want to have a doubleClickListener
     * @param method the method you want to be clicked when a JList item is double-clicked on
     */
    public void setDoubleClickOnJListItem(JList jList, Consumer<String> method){
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2 && e.getButton() == 1){
                    var selectedValue = jList.getSelectedValue().toString();
                    method.accept(selectedValue);
                }
            }
        });

    }

    /**
     * A helper method to make it easier to show an input dialog
     * @param parentComponent the parent component
     * @param message the message on the dialog
     * @param addMethod the method you want to be triggered when a input is submitted
     */
    public void showInputDialog(JComponent parentComponent, String message, Consumer<String> addMethod){
        String input = JOptionPane.showInputDialog(parentComponent,message, GUI_TITLE, JOptionPane.PLAIN_MESSAGE);
        if(input != null){
            addMethod.accept(input);
        }
    }
}
