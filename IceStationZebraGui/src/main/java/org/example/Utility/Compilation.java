package org.example.Utility;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.Dockerfile;
import org.example.files.FileIO;

import java.awt.*;

public class Compilation {
    private PublishSubject<TerminalMessage> subject;
    private final String pullImage;
    private final String containerName;
    private final String suffix = Generate.generateRandomString(12);

    public Compilation(PublishSubject<TerminalMessage> subject, String pullImage){
        this.subject = subject;
        this.pullImage = pullImage;
        this.containerName = "container_" + this.suffix;
    }
    public void go(String compilerName, String codeFile){
        // Create docker file
        Dockerfile dockerfile = Dockerfile.getBasic(this.pullImage, "image_" + this.suffix);

        // Create docker container
        DockerContainer container = DockerContainer.getBasic(this.containerName, dockerfile);
        container.setEnv("COMPILER_NAME",compilerName);
        runDockerImage(container, dockerfile);
    }
    private void runDockerImage(DockerContainer container, Dockerfile dockerfile) {

        ProcessHandler handler = dockerfile.build();

        // Image stdout
        Disposable stdoutDisposable = handler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
                    this.subject.onNext(new TerminalMessage(out, Color.WHITE));
                }, Throwable::printStackTrace
                , () -> System.out.println("Image stdout complete"));

        // Image stderr
        Disposable stderrDisposable = handler.getStderr().subscribeOn(Schedulers.io()).subscribe(err -> {
                    //this.subject.onNext(new TerminalMessage(err, Color.orange));
                }, Throwable::printStackTrace
                , () -> System.out.println("Image stderr complete"));

        // Image completion
        Disposable completionDisposable = handler.getCompletion().subscribeOn(Schedulers.io()).subscribe(
                () -> {
                    subject.onNext(new TerminalMessage("Image Process completed successfully",Color.GREEN));
                    runDockerContainer(container, dockerfile);
                },
                throwable -> subject.onNext(new TerminalMessage("Image Process failed: " + throwable.getMessage(),Color.RED))
        );
    }
    private void runDockerContainer(DockerContainer container, Dockerfile dockerfile){

        ProcessHandler containerHandler = container.run(new String[0]);

        // Container stdout
        Disposable containerStdoutDisposable = containerHandler.getStdout().subscribeOn(Schedulers.io()).subscribe(out -> {
            this.subject.onNext(new TerminalMessage(out,Color.lightGray));

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
