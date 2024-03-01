package org.example.Utility;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.files.FileIO;
import org.example.models.Event;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Compilation {
    private PublishSubject<TerminalMessage> subject;
    //private final String containerName;
    private final String suffix = Generate.generateRandomString(12);
    private Observable<String> terminalInput = null;
    DockerContainer container;
    DockerImage image;
    Event event;

    public Compilation(Event event, PublishSubject<TerminalMessage> subject, DockerContainer container, DockerImage image){
        this.subject = subject;
        this.container = container;
        this.image = image;
        this.event = event;
    }

    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }

    public void go(){


        this.runDockerImage();
    }

    private void runDockerImage() {
        ProcessHandler handler = this.image.build(FileIO.getApplicationRootPath(), this.subject);
        handler.setOnComplete((ahandler) -> {

        });
//        //ProcessHandler handler = this.image.build();
//        // Image stdout
//        Disposable stdoutDisposable = handler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
//                    this.subject.onNext(new TerminalMessage(out, Color.WHITE));
//                }, Throwable::printStackTrace
//                , () -> System.out.println("Image stdout complete"));
//
//        // Image stderr
//        Disposable stderrDisposable = handler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
//                    this.subject.onNext(new TerminalMessage(err, Color.orange));
//                }, Throwable::printStackTrace
//                , () -> System.out.println("Image stderr complete"));
//
//        // Image completion
//        Disposable completionDisposable = handler.getCompletion().subscribeOn(Schedulers.io()).subscribe(
//                () -> {
//                    subject.onNext(new TerminalMessage("Image Process completed successfully",Color.GREEN));
//                    runDockerContainer();
//                },
//                throwable -> subject.onNext(new TerminalMessage("Image Process failed: " + throwable.getMessage(),Color.RED))
//        );
    }

    private void runDockerContainer(){
        ProcessHandler containerHandler = container.run(this.subject);
        //this.terminalInput.subscribeOn(Schedulers.io()).subscribe(System.out::println);
        this.terminalInput.subscribeOn(Schedulers.io()).subscribe(containerHandler::stdin);
        // Container stdout
        Disposable containerStdoutDisposable = containerHandler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
            this.subject.onNext(new TerminalMessage(out, Color.lightGray));

        });

        // Container stderr
        Disposable containerStderrDisposable = containerHandler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
            this.subject.onNext(new TerminalMessage(err, Color.orange));
        });

        // Container completion
        Disposable containerCompletionDisposable = containerHandler.getCompletion().subscribe(
                () -> {
                    this.subject.onNext(new TerminalMessage("Container completed",Color.green));
                },throwable -> subject.onNext(new TerminalMessage("Process failed: " + throwable.getMessage(),Color.red))
        );
    }
}
