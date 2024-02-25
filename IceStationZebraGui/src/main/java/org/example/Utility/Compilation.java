package org.example.Utility;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.files.FileIO;

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
    public Compilation(PublishSubject<TerminalMessage> subject, DockerContainer container, DockerImage image){
        this.subject = subject;
        this.container = container;
        this.image = image;
    }
    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }
    public void go(String compilerName){
        // Create basic docker file

        List<String> tests = getTests();
        tests.forEach(test -> {
            String compileCommand = constructCompileCommand(compilerName,test);
            // Create basic docker container
            DockerContainer container = DockerContainer.getBasic("container_" + Generate.generateRandomString(12), this.image);
            container.setEnv("COMPILER_NAME",compilerName);
            container.setEnv("COMPILER_COMMAND",compileCommand);
            //container.setEntrypointOverride("/scripts/test_entrypoint.sh");
            runDockerImage(container, this.image);
        });


    }
    private void runDockerImage(DockerContainer container, DockerImage dockerImage) {

        ProcessHandler handler = dockerImage.build();

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
                    runDockerContainer(container, dockerImage);
                },
                throwable -> subject.onNext(new TerminalMessage("Image Process failed: " + throwable.getMessage(),Color.RED))
        );
    }
    private void runDockerContainer(DockerContainer container, DockerImage dockerImage){

        ProcessHandler containerHandler = container.run(new String[0]);
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
    private List<String> getTests() {
        File directory = new File(FileIO.getApplicationRootPath("codebase"));
        File[] filesList = directory.listFiles();
        if (filesList != null) {
            return  Arrays.stream(filesList)
                    .filter(File::isDirectory)
                    .map(File::getName)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    private String getFilesStringForTest(String test){
        File sub = new File(FileIO.getApplicationRootPath("codebase/" + test));
        return Arrays.stream(sub.listFiles()).toList().stream()
                .filter(File::isFile).map(file ->"/codebase/" + test + "/" + file.getName()) // Convert File to its name
                .collect(Collectors.joining(" "));
    }
    private String constructCompileCommand(String compilerName, String testName){
        String OUTPUT  = "/output/" + compilerName + "_" + testName;//FileIO.getApplicationRootPath("output/" + testName);
        String compileCommand = new FileIO(FileIO.getApplicationRootPath("compile_commands"),compilerName + "_compileCmd.sh").read();
        String FILES = getFilesStringForTest(testName);
        compileCommand = compileCommand.replace("FILES",FILES).replace("OUTPUT",OUTPUT);
        System.out.println(compileCommand);
        return compileCommand;
    }
}
