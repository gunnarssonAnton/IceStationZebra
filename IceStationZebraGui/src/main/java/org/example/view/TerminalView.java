package org.example.view;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TerminalView extends JPanel {
    PublishSubject<String> subject = PublishSubject.create();

    public TerminalView(){
        this.add(this.outPutField());
    }

    private TextArea outPutField(){
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        Disposable disposable = this.subject.subscribe(str -> {
            textArea.append("isz › " + str);
        },throwable -> {

        },() -> {

        });
        return textArea;
    }
    public PublishSubject<String> getSubject() {
        return this.subject;
    }
}
