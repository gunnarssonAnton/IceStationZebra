package org.example;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {


    JPanel cards = new JPanel(new CardLayout());
    CompilationView compilationView = new CompilationView();
    ExecutionView executionView = new ExecutionView();

    Gui(){
        this.pack();
        this.setSize(1000,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Ice Station Zebra");
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        this.executionView.setClicker(e->{
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.previous(cards);
        });

        this.compilationView.setOnClick(e-> {
            CardLayout cardLayout = (CardLayout) this.cards.getLayout();
            cardLayout.next(this.cards);
            System.out.println("RUN");
        });



        this.cards.add(compilationView);
        this.cards.add(executionView);
        this.add(this.cards);

        this.add(this.outPutField(),BorderLayout.SOUTH);

    }

    private TextArea outPutField(){
        TextArea textArea = new TextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setText("BIBLE WHORE");
        textArea.setEditable(false);
        return textArea;
    }







}
