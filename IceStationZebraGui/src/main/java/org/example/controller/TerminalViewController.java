package org.example.controller;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.TerminalMessage;
import org.example.view.TerminalView;

public class TerminalViewController {
    PublishSubject<TerminalMessage> subject = PublishSubject.create();
    TerminalView view = new TerminalView();

    public TerminalViewController(){
        Disposable disposable = subject.subscribe(tm -> {
            this.view.addLine(tm);
        });
    }

    public TerminalView getView() {
        return this.view;
    }
    public PublishSubject<TerminalMessage> getSubject() {
        return this.subject;
    }
}
