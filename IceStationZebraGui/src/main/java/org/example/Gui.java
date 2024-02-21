package org.example;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.TerminalMessage;
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
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private final TerminalViewController terminalViewController = new TerminalViewController();
    private final CompilationViewController compilationViewController = new CompilationViewController(this.terminalViewController.getSubject());
    private final ExecutionViewController executionViewController = new ExecutionViewController(this.terminalViewController.getSubject());
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
        this.terminalViewController.getSubject().onNext(new TerminalMessage(new Date().toString(),Color.PINK));
        this.compilationViewController
                .getView()
                .setOnClick(e-> this.slideOut());
        this.executionViewController
                .getView()
                .setOnClick(e -> this.slideIn());
    }


    private void slideIn() {
        Rectangle bounds = cards.getBounds();
        bounds.x = 0;
        cards.setBounds(bounds);
        animateSlide(1000,false, 100);

    }

    private void slideOut() {
        Rectangle bounds = cards.getBounds();
        bounds.x = 0;
        cards.setBounds(bounds);
        animateSlide(-1000,true, -100);
    }
    private void animateSlide(int targetX, boolean isSlideOut, int dx) {
        Timer timer = new Timer(2, e -> {
            Rectangle bounds = cards.getBounds();
            if (bounds.x == targetX) {
                if(isSlideOut){
                    cardLayout.next(cards);
                }
                else{
                    cardLayout.previous(cards);
                }
                ((Timer) e.getSource()).stop();
            } else {
                bounds.x += dx;
                cards.setBounds(bounds);
            }
        });
        timer.start();
    }

}
