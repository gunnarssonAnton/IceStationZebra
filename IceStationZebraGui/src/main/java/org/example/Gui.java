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
    TerminalViewController terminalViewController = new TerminalViewController();
    CompilationViewController compilationViewController = new CompilationViewController(this.terminalViewController.getSubject());
    ExecutionViewController executionViewController = new ExecutionViewController(this.terminalViewController.getSubject());
    public Gui(){
        this.pack();
        this.setSize(1000,1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Ice Station Zebra");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(-80,-1200);


        this.cards.add(compilationViewController.getView());
        this.cards.add(executionViewController.getView());
        this.add(this.cards);

        this.add(this.terminalViewController.getView(),BorderLayout.SOUTH);
        this.terminalViewController.getSubject().onNext(new Date().toString());
    }




}
