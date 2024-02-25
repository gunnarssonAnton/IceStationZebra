package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.models.Event;
import org.example.view.CompilationView;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.setOnCompileAllClick(e -> {
//                Compilation compilation = new Compilation(subject,"ubuntu:latest");
//                compilation.setTerminalInput(this.terminalInput);
//                compilation.go(name);
        });
        this.view.setOnCompileClick(e -> {

        });
    }
    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }

    public void compile(Event event){
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, event.givenName() + "_" + Generate.generateRandomString(8));
        DockerContainer container = new DockerContainer(event.givenName(), image);
        Compilation compilation = new Compilation(this.subject, container, image);
    }
    public void compileAll(){

    }

    public CompilationView getView(){
        return this.view;
    }
}
