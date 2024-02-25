package org.example.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.*;
import org.example.Utility.Compilation;
import org.example.models.Event;
import org.example.view.CompilationView;

public class CompilationViewController {
    Observable<String> terminalInput;
    CompilationView view = new CompilationView();
    PublishSubject<TerminalMessage> subject;
    public CompilationViewController(PublishSubject<TerminalMessage> subject){
        this.subject = subject;
        this.view.setOnCompileAllClick(e -> {
            // Get events from Singleton
        });
        this.view.setOnCompileClick(e -> {
            this.compile(this.view.selectedEvent);
        });
    }
    public void setTerminalInput(Observable<String> terminalInput){
        this.terminalInput = terminalInput;
    }

    public void compile(Event event){
        // Image
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, event.givenName() + "_" + Generate.generateRandomString(8));
        image.addVolume("/scripts");
        image.addVolume("/codebase");
        image.addVolume("/output");
        image.addENV("EVENT_NAME","");
        image.addENV("EVENT_INSTALL","");
        image.addENV("EVENT_COMPILE_COMMAND","");
        image.setEntrypoint("/scripts/compilation_entrypoint.sh");

        // Container
        ISZTest.getTests().forEach(iszTest -> {
            DockerContainer container = new DockerContainer(event.givenName(), image);
            container.setVolume("./scripts", "/scripts");
            container.setVolume("./codebase", "/codebase");
            container.setVolume("./output", "/output");
            container.addENV("EVENT_NAME",event.givenName());
            container.addENV("EVENT_INSTALL",event.installation().join(";"));
            container.addENV("EVENT_COMPILE_COMMAND",iszTest.constructCompileCommand(event));

            // Compilation
            Compilation compilation = new Compilation(event, this.subject, container, image);
            compilation.setTerminalInput(this.terminalInput);
            compilation.go();
        });


    }
    public void compileAll(){

    }

    public CompilationView getView(){
        return this.view;
    }
}
