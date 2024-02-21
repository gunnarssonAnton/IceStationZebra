package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.TerminalMessage;
import org.example.view.CompilationView;
import org.example.view.ExecutionView;

public class ExecutionViewController {
    ExecutionView view = new ExecutionView();
    PublishSubject<TerminalMessage> subject;
    public ExecutionViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
    }
    public ExecutionView getView(){
        return this.view;
    }
}
