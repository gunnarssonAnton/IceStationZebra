package org.example.controller;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.Utility.Colorize;
import org.example.Utility.Generate;
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
        Dockerfile dockerfile = Dockerfile.getBasic("openjdk:11", "image_"+Generate.generateRandomString(8));
        FileIO file = new FileIO(FileIO.getApplicationRootPath(), "Dockerfile");
        file.write(dockerfile.toString());

        // Start building the Docker image
        dockerfile.build(file.getPath().toString())
                .observeOn(Schedulers.io()) // Perform build operation in IO scheduler
                .doOnNext(str -> SwingUtilities.invokeLater(() -> {
//                    this.subject.onNext(str);
                    System.out.println("Image:" + str);
                })) // Handle build output
                .doOnError(throwable -> SwingUtilities.invokeLater(() -> {
                    this.subject.onNext("Build error: " + throwable.getMessage());
                    throwable.printStackTrace();
                }))
                .ignoreElements() // Ignore all emissions and convert to Completable
                .observeOn(Schedulers.io()) // Ensure subsequent operations also run in an appropriate scheduler
                .andThen(Completable.defer(() -> {
                    // After successful build, start the container
                    DockerContainer container = DockerContainer.getBasic("container_" + Generate.generateRandomString(8), dockerfile);
                    return container.run(new String[0]).ignoreElements(); // Convert Observable to Completable if necessary
                }))
                .subscribe(
                        () -> SwingUtilities.invokeLater(() -> {
                            this.subject.onNext(Colorize.printInfo("Container started successfully"));

                        }),
                        throwable -> SwingUtilities.invokeLater(() -> {
                            this.subject.onNext("Run error: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
                );
    }

    public CompilationView getView(){
        return this.view;
    }
}
