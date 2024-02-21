package org.example.controller;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.Utility.*;
import org.example.Utility.Colorize;
import org.example.Utility.Compilation;
import org.example.Utility.Generate;
import org.example.Utility.ProcessHandler;
import org.example.EditFileWindow;
import org.example.files.FileIO;
import org.example.view.CompilationView;

import javax.swing.*;
import java.awt.*;

public class CompilationViewController {
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.setOnClick(e -> {
            new Compilation(subject,"noname").runDocker();
        });
    }


    public CompilationView getView(){
        return this.view;
    }
}
