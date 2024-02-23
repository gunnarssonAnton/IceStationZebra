package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.files.FileIO;
import org.example.view.CompilationView;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.runAllOnClick(e -> {
            this.view.getCompilerNamesSet().forEach(name -> {
                Compilation compilation = new Compilation(subject,"ubuntu:latest");
                compilation.setTerminalInput(this.terminalInput);
                compilation.go(name);
            });
        });
    }
    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }


    public CompilationView getView(){
        return this.view;
    }
}
