package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Gui extends JFrame {
    JPanel cards = new JPanel(new CardLayout());
    CompilationView compilationView = new CompilationView();
    ExecutionView executionView = new ExecutionView();
    PublishSubject<String> subject = PublishSubject.create();

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

    public PublishSubject<String> getSubject() {
        return this.subject;
    }

    private TextArea outPutField(){
        TextArea textArea = new TextArea();
        textArea.setText(new Date().toString());
        textArea.setEditable(false);

        Disposable disposable = this.subject.subscribe(str -> {
            textArea.append(str);
        },throwable -> {

        },() -> {

        });

        return textArea;
    }
}
