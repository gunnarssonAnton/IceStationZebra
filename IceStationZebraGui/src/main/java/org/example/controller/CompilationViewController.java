package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.models.Event;
import org.example.view.CompilationView;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
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
        image.addVolume("/scripts");
        image.addVolume("/files");
        image.addVolume("/codebase");
        image.addVolume("/output");
        image.addENV("EVENT_NAME","");
        image.addENV("EVENT_INSTALL","");
        image.addENV("EVENT_COMPILE_COMMAND","");
        image.addCOPY("/scripts/compilation_entrypoint.sh","/compilation_entrypoint.sh");
        image.addRUN("chmod +x /compilation_entrypoint.sh");
        image.setEntrypoint("/compilation_entrypoint.sh");

        // Container
        ISZTest.getTests().forEach(iszTest -> {
            DockerContainer container = new DockerContainer(event.givenName() + "_" + iszTest.getName() + "_" + Generate.generateRandomString(8), image);
            container.setVolume("./scripts", "/scripts");
            container.setVolume("./files", "/files");
            container.setVolume("./codebase", "/codebase");
            container.setVolume("./output", "/output");
            container.addENV("EVENT_NAME", event.givenName());
            //System.out.println("cm cmdns:" + event.installation().stream().skip(0).map(s -> s.contains(";") ? s : s + ";").collect(Collectors.joining()).replaceAll(";$",""));
            container.addENV("EVENT_INSTALL", event.installation().stream().skip(0).map(s -> s.contains(";") ? s : s + ";").collect(Collectors.joining()).replaceAll(";$",""));
            container.addENV("EVENT_COMPILE_COMMAND", iszTest.constructCompileCommand(event));

            // Compilation
            Compilation compilation = new Compilation(event, this.subject, container, image);
            compilation.setTerminalInput(this.terminalInput);
            compilation.go();
        });
    }

    public CompilationView getView(){
        return this.view;
    }
}
