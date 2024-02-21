package org.example;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.controller.CompilationViewController;
import org.example.controller.ExecutionViewController;
import org.example.controller.TerminalViewController;
import org.example.view.CompilationView;
import org.example.view.ExecutionView;
import org.example.view.TerminalView;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Gui extends JFrame {
    JPanel cards = new JPanel(new CardLayout());
    CompilationViewController compilationViewController = new CompilationViewController();
    ExecutionViewController executionViewController = new ExecutionViewController();
    TerminalViewController terminalViewController = new TerminalViewController();
    public Gui(){
        this.pack();
        this.setSize(1000,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Ice Station Zebra");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);

//        this.executionView.setClicker(e->{
//            CardLayout cardLayout = (CardLayout) cards.getLayout();
//            cardLayout.previous(cards);
//        });
//
//        this.compilationView.setOnClick(e-> {
//            CardLayout cardLayout = (CardLayout) this.cards.getLayout();
//            cardLayout.next(this.cards);
//            System.out.println("RUN");
//        });

        this.cards.add(compilationViewController.getView());
        this.cards.add(executionViewController.getView());
        this.add(this.cards);

        this.add(this.terminalViewController.getView(),BorderLayout.SOUTH);
        this.terminalViewController.getView().getSubject().onNext(new Date().toString());
    }




}
