package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.*;
import org.example.models.Event;
import org.example.view.CompilationView;

import java.awt.*;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> terminalSubject;
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

    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }

    public void compile(Event event){
        System.out.println("Compile:" + event.givenName());
        // Image
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, event.givenName() + "_" + Generate.generateRandomString(8));
        image.addRUN("mkdir -p /output");
//        image.addVolume("/scripts");
        image.addVolume("/files");
//        image.addVolume("/codebase");
        image.addVolume("/output");
        //image.addCOPY("/scripts/compilation_entrypoint.sh","/compilation_entrypoint.sh");
        //image.addRUN("chmod +x /compilation_entrypoint.sh");
        //image.addRUN(event.installation().stream().skip(0).map(s -> s.contains(";") ? s : s + ";").collect(Collectors.joining()).replaceAll(" && $",""));
        //image.setEntrypoint("/compilation_entrypoint.sh");
        image.addCOPY("/codebase","/codebase");
        event.installation().forEach(image::addRUN);

        ProcessHandler handler = image.build(terminalSubject);
        handler.setOnComplete(handle -> {
            ISZTest.getTests().forEach(iszTest -> {
                //image.addCOPY("/codebase/" + iszTest.getName(),"/codebase/" + iszTest.getName());
                System.out.println("iszTest:" + iszTest.getName());
//            image.addRUN(iszTest.constructCompileCommand(event));

                DockerContainer container = new DockerContainer(event.givenName() + "_" + iszTest.getName() + Generate.generateRandomString(4),image);
                //container.addARG(iszTest.constructCompileCommand(event));
                container.setVolume("/output","/output");
                ProcessHandler contHandler = container.run(terminalSubject);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ProcessHandler exec = container.exec(iszTest.constructCompileCommand(event).split(" "), terminalSubject);
            });
        });
    }

    public CompilationView getView(){
        return this.view;
    }
}
