package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.view.TerminalView;

public class TerminalViewController {
    PublishSubject<String> subject = PublishSubject.create();
    TerminalView view = new TerminalView(subject);

    public TerminalView getView() {
        return this.view;
    }
    public PublishSubject<String> getSubject() {
        return this.subject;
    }
}
