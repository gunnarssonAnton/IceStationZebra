package org.example.view;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TerminalView extends JPanel {

    public TerminalView(PublishSubject<String> subject){
        this.setLayout(new BorderLayout());
        this.add(this.outPutField(subject),BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(900,500));

    }

    private TextArea outPutField(PublishSubject<String> subject){
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.darkGray);
        textArea.setForeground(Color.orange);
        //        Disposable disposable = subject.subscribe(str -> SwingUtilities.invokeLater(() -> {
//            textArea.append("isz › " + str + "\n");
//
//        }),throwable -> {
//
//        },() -> {
//
//        });
        Disposable disposable = subject.subscribe(str -> {
            textArea.append("isz › " + str + "\n");
        });
        return textArea;
    }

}
