package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.view.CompilationView;
import org.example.view.ExecutionView;

public class ExecutionViewController {
    ExecutionView view = new ExecutionView();
    PublishSubject<String> subject;
    public ExecutionViewController(PublishSubject<String> subject){
        this.subject = subject;
    }
    public ExecutionView getView(){
        return this.view;
    }
}
