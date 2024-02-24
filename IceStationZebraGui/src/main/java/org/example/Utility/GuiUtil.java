package org.example.Utility;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.function.Consumer;

public class GuiUtil {
    public static final String GUI_TITLE = "Ice Station Zebra";

    public void updateJList(JList list, Set<String> sets){
        list.setListData(sets.toArray());
        list.updateUI();
        list.revalidate();
        list.repaint();
    }

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

    public void showInputDialog(JComponent parentComponent, String message, Consumer<String> addMethod){
        String input = JOptionPane.showInputDialog(parentComponent,message, GUI_TITLE, JOptionPane.PLAIN_MESSAGE);
        if(input != null){
            addMethod.accept(input);
        }
    }
}
