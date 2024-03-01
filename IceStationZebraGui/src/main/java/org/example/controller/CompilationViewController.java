package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.*;
import org.example.models.Event;
import org.example.view.CompilationView;

import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> terminalSubject;
    Map<String, DockerImage> images = new Hashtable<>();
    public CompilationViewController(PublishSubject<TerminalMessage> terminalSubject){
        this.terminalSubject = terminalSubject;
        this.view.setOnCompileAllClick(e -> {
            // Get events from Singleton
            IceHandler.getInstance().getEvents().forEach(this::compile);
        });
        this.view.setOnCompileClick(e -> {
            this.compile(this.view.selectedEvent);
        });
    }
    public Map<String, DockerImage> getImages(){
        return this.images;
    }
    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }

    public void compile(Event event){
        System.out.println("Compile:" + event.givenName());
        DockerImage.remove(event.givenName(),terminalSubject).setOnComplete(handler -> {

        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Image
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, event.givenName() + "_image");
        image.addRUN("mkdir -p /output");
        image.addVolume("/files");
        image.addVolume("/output");

        image.addCOPY("/codebase","/codebase");
        image.addCOPY("/scripts","/scripts");
        System.out.println("[toString]\n" + image.toString());
        image.addRUN("chmod +x /scripts/pre-execution.sh");
        image.addRUN("chmod +x /scripts/execution.sh");
        image.addRUN("chmod +x /scripts/post-execution.sh");
        image.addRUN("chmod +x /scripts/execution_entrypoint.sh");
        event.installation().forEach(image::addRUN);

        ProcessHandler handler = image.build(terminalSubject);
        handler.setOnComplete(handle -> {
            this.images.put(image.getName(),image);
            ISZTest.getTests().forEach(iszTest -> {
                System.out.println("iszTest:" + iszTest.getName());

                DockerContainer container = new DockerContainer(event.givenName() + "_" + iszTest.getName() + "_" + Generate.generateRandomString(4),image);
                //container.addARG(iszTest.constructCompileCommand(event));
                container.setVolume("/output","/output");
                ProcessHandler contHandler = container.run(terminalSubject);
                contHandler.setOnComplete(haeendle ->{
                    // Remove container
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ProcessHandler exec = container.exec(iszTest.constructCompileCommand(event).split(" "), terminalSubject);
                exec.setOnComplete(execHandler -> {
                    container.stop(this.terminalSubject).setOnComplete(stopHandler -> {
                        container.remove(this.terminalSubject).setOnComplete(rmHandler -> {
                            terminalSubject.onNext(new TerminalMessage("Removed:" + container.getName(),Color.GREEN));

                        });
                    });
                });
            });
        });
    }

    public CompilationView getView(){
        return this.view;
    }
}
