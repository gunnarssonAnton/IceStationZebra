package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.IceHandler;
import org.example.Utility.ProcessHandler;
import org.example.Utility.TerminalMessage;
import org.example.models.Event;
import org.example.view.ExecutionView;

import java.awt.*;

public class ExecutionViewController {
    ExecutionView view = new ExecutionView();
    PublishSubject<TerminalMessage> terminalSubject;
    CompilationViewController compilationViewController;
    public ExecutionViewController(PublishSubject<TerminalMessage> subject, CompilationViewController compilationViewController){
        this.terminalSubject = subject;
        this.compilationViewController = compilationViewController;
        this.view.setPrepOnClick(e -> {
            this.prepare();
        });
    }
    public ExecutionView getView(){
        return this.view;
    }
    public void prepare(){
        String execName = this.view.getSelectedValue();
        String name = this.view.getSelectedValue().split("_")[0] + "_image";
        System.out.println("names:" + this.compilationViewController.getImages().keySet());
        this.terminalSubject.onNext(new TerminalMessage("Preparing " + name,Color.YELLOW));
        //
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, name);
        image.addRUN("mkdir -p /output");
        image.addVolume("/files");
        image.addVolume("/output");
        image.addENV("ROUND","0");
        image.addCOPY("/files","/files");
        image.addCOPY("/codebase","/codebase");
        image.addCOPY("/scripts","/scripts");
        System.out.println("[toString]\n" + image.toString());
        image.addRUN("chmod +x /scripts/pre-execution.sh");
        image.addRUN("chmod +x /scripts/execution.sh");
        image.addRUN("chmod +x /scripts/post-execution.sh");
        image.addRUN("chmod +x /scripts/execution_entrypoint.sh");
        image.addRUN("apt-get update");
        image.addRUN("apt-get install -y software-properties-common");
        image.addRUN("add-apt-repository ppa:openjdk-r/ppa");
        image.addRUN("apt-get update && apt-get upgrade -y");
        image.addRUN("apt-get install openjdk-17-jdk -y");
        image.addRUN("apt install gpiod -y");
        ProcessHandler imageHandler = image.build(terminalSubject);
        imageHandler.setOnComplete(handle -> {
            DockerContainer container = new DockerContainer(name + "_execution",image);
            container.setEntrypointOverride("/scripts/execution_entrypoint.sh");
            container.setVolume("/output","/output");
            container.setVolume("/files","/files");
            //container.addENV("ROUND","0");
            container.addARG("java -cp /output/" + execName + " " + (execName.split("_")[1].charAt(0) + "").toUpperCase() + execName.split("_")[1].substring(1));
            container.addARG(this.view.getAmountOfRounds());
            container.isPrivileged(true);
            container.run(this.terminalSubject).setOnComplete((ph) -> {
                terminalSubject.onNext(new TerminalMessage("Yaaaay", Color.pink));
                container.stop(terminalSubject).setOnComplete(dolk -> {
                    dolk.printLogFiles(name);
                    container.remove(terminalSubject).setOnComplete(dole -> {
                        terminalSubject.onNext(new TerminalMessage("Removed container",Color.GREEN));
                        dole.printLogFiles(name + "removal");
                    });
                });
            });
        });
    }
}
