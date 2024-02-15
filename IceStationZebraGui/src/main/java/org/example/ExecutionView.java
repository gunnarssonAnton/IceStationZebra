package org.example;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ExecutionView extends JPanel {

    JButton tempBtn = new JButton("BACK");

    ExecutionView(){
        this.add(new JLabel("HORE"));
        this.add(tempBtn);
    }

    protected void setClicker(ActionListener l){
        this.tempBtn.addActionListener(l);
    }
}
