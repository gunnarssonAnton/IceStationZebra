package org.example.controller;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.Utility.*;
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
            runDocker();
        });
    }
    public void runDocker() {
        Dockerfile dockerfile = Dockerfile.getBasic("openjdk:11", "image_" + Generate.generateRandomString(8));
        FileIO file = new FileIO(FileIO.getApplicationRootPath(), "Dockerfile");
        file.write(dockerfile.toString());
        ProcessHandler handler = dockerfile.build(file.getPath().toString());
        Disposable stdoutDisposable = handler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
            this.subject.onNext(new TerminalMessage(out,Color.white));
        }, Throwable::printStackTrace
        , () -> System.out.println("Image stdout complete"));
        Disposable stderrDisposable = handler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
            this.subject.onNext(new TerminalMessage(err, Color.red));
        }, Throwable::printStackTrace
                , () -> System.out.println("Image stderr complete"));
        Disposable completionDisposable = handler.getCompletion().subscribe(
                () -> {
                    subject.onNext(new TerminalMessage("Image Process completed successfully",Color.GREEN));
                    runContainer(dockerfile);
                },
                throwable -> subject.onNext(new TerminalMessage("Image Process failed: " + throwable.getMessage(),Color.RED))
        );

    }
    public void runContainer(Dockerfile dockerfile){
        DockerContainer container = DockerContainer.getBasic("container_" + Generate.generateRandomString(8), dockerfile);
        ProcessHandler containerHandler = container.run(new String[0]);
        Disposable containerStdoutDisposable = containerHandler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
            this.subject.onNext(new TerminalMessage(out,Color.lightGray));

        });
        Disposable containerStderrDisposable = containerHandler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
            this.subject.onNext(new TerminalMessage(err, Color.red));
        });
        Disposable containerCompletionDisposable = containerHandler.getCompletion().subscribe(
                () -> {
                    this.subject.onNext(new TerminalMessage("Container completed",Color.green));
                },throwable -> subject.onNext(new TerminalMessage("Process failed: " + throwable.getMessage(),Color.red))
        );
    }
    public CompilationView getView(){
        return this.view;
    }
}
