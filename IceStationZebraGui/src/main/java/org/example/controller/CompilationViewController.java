package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.view.CompilationView;


public class CompilationViewController {
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.runAllOnClick(e -> {
            this.view.getCompilerNamesSet().forEach(name -> {
                Compilation compilation = new Compilation(subject,"ubuntu:latest");
                compilation.go(name, "");
            });

        });
    }


    public CompilationView getView(){
        return this.view;
    }
}
