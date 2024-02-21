package org.example.controller;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.EditFileWindow;
import org.example.files.FileIO;
import org.example.view.CompilationView;

import javax.swing.*;
import java.awt.*;

public class CompilationViewController {
    CompilationView view = new CompilationView();
    PublishSubject<String> subject;
    public CompilationViewController(PublishSubject<String> subject){
        this.subject = subject;
//                this.view.setClicker(e->{
//            CardLayout cardLayout = (CardLayout) cards.getLayout();
//            cardLayout.previous(cards);
//        });

//        this.view.setOnClick(e-> {
////            CardLayout cardLayout = (CardLayout) this.cards.getLayout();
////            cardLayout.next(this.cards);
//
//            Dockerfile dockerfile = Dockerfile.getBasic("openjdk:11","testImage");
//            FileIO file = new FileIO(FileIO.getApplicationRootPath(),"Dockerfile");
//            file.write(dockerfile.toString());
//            dockerfile.build(file.getPath().toString()).subscribe(str ->
//                    SwingUtilities.invokeLater(() -> this.subject.onNext(str)),
//                    throwable -> SwingUtilities.invokeLater(() -> {
//                        this.subject.onNext("Error: " + throwable.getMessage());
//                        throwable.printStackTrace();
//                    })
//            );
//
//            DockerContainer container = DockerContainer.getBasic("testcontainer",dockerfile);
//            container.run(new String[0]).subscribe(str ->
//                    SwingUtilities.invokeLater(() -> this.subject.onNext(str)),
//                    throwable -> SwingUtilities.invokeLater(() -> {
//                        this.subject.onNext("Error: " + throwable.getMessage());
//                        throwable.printStackTrace();
//                    })
//            );
//        });
        this.view.setOnClick(e -> {
            runDocker();
        });
    }
    public void runDocker() {
        Dockerfile dockerfile = Dockerfile.getBasic("openjdk:11", "testImage");
        FileIO file = new FileIO(FileIO.getApplicationRootPath(), "Dockerfile");
        file.write(dockerfile.toString());

        // Start building the Docker image
        dockerfile.build(file.getPath().toString())
                .observeOn(Schedulers.io()) // Perform build operation in IO scheduler
                .doOnNext(str -> SwingUtilities.invokeLater(() -> this.subject.onNext(str))) // Handle build output
                .doOnError(throwable -> SwingUtilities.invokeLater(() -> {
                    this.subject.onNext("Build error: " + throwable.getMessage());
                    throwable.printStackTrace();
                }))
                .ignoreElements() // Ignore all emissions and convert to Completable
                .observeOn(Schedulers.io()) // Ensure subsequent operations also run in an appropriate scheduler
                .andThen(Completable.defer(() -> {
                    // After successful build, start the container
                    DockerContainer container = DockerContainer.getBasic("testcontainer", dockerfile);
                    return container.run(new String[0]).ignoreElements(); // Convert Observable to Completable if necessary
                }))
                .subscribe(
                        () -> SwingUtilities.invokeLater(() -> this.subject.onNext("Container started successfully")),
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
