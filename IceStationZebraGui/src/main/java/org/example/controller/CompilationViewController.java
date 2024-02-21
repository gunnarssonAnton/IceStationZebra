package org.example.controller;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.Utility.Colorize;
import org.example.Utility.Compilation;
import org.example.Utility.Generate;
import org.example.Utility.ProcessHandler;
import org.example.files.FileIO;
import org.example.view.CompilationView;

import javax.swing.*;
import java.awt.*;

public class CompilationViewController {
    CompilationView view = new CompilationView();
    PublishSubject<String> subject;
    public CompilationViewController(PublishSubject<String> subject){
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
            this.subject.onNext(Colorize.printInfo(out));
        }, Throwable::printStackTrace
        , () -> System.out.println("Image stdout complete"));
        Disposable stderrDisposable = handler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
            this.subject.onNext(Colorize.printAlert(err));
        }, Throwable::printStackTrace
                , () -> System.out.println("Image stderr complete"));
        Disposable completionDisposable = handler.getCompletion().subscribe(
                () -> {
                    System.out.println("Image Process completed successfully");
                    runContainer(dockerfile);
                },
                throwable -> System.out.println("Image Process failed: " + throwable.getMessage())
        );

    }
    public void runContainer(Dockerfile dockerfile){
        DockerContainer container = DockerContainer.getBasic("container_" + Generate.generateRandomString(8), dockerfile);
        ProcessHandler containerHandler = container.run(new String[0]);
        Disposable containerStdoutDisposable = containerHandler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
            this.subject.onNext(Colorize.printInfo("                               "+out));

        });
        Disposable containerStderrDisposable = containerHandler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
            this.subject.onNext(Colorize.printAlert("                               "+err));
        });
        Disposable containerCompletionDisposable = containerHandler.getCompletion().subscribe(
                () -> {
                    this.subject.onNext(Colorize.printAlert("Container completed"));
                },throwable -> System.out.println("Process failed: " + throwable.getMessage())
        );
    }
    public CompilationView getView(){
        return this.view;
    }
}
