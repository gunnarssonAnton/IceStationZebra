package org.example.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ExecutionView extends JPanel {

    JButton tempBtn = new JButton("BACK");

    public ExecutionView(){
        this.add(new JLabel("HORE"));
        this.add(tempBtn);
    }

    public void setOnClick(ActionListener l){
        this.tempBtn.addActionListener(l);
    }
}
