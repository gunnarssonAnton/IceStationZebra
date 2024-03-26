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
    DockerImage executionImage = null;
    public ExecutionViewController(PublishSubject<TerminalMessage> subject, CompilationViewController compilationViewController){
        this.terminalSubject = subject;
        this.compilationViewController = compilationViewController;
        this.view.setPrepOnClick(e -> {
            this.prepare();
        });
        this.view.setGenerateExecutionImageOnClick(e -> {
            this.generateExecutionImage();
        });
    }
    public ExecutionView getView(){
        return this.view;
    }
    public void generateExecutionImage(){
//        String execName = this.view.getSelectedValue();
//        String name = this.view.getSelectedValue().split("_")[0] + "_image";
        String name = "execution_image";
        System.out.println("names:" + this.compilationViewController.getImages().keySet());
        this.terminalSubject.onNext(new TerminalMessage("Preparing " + name,Color.YELLOW));
        //
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, name);
        this.executionImage = image;
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
        image.addRUN("chmod +x /scripts/install_gpiod.sh");
        image.addRUN("apt-get update && apt-get upgrade -y");
        image.addRUN("apt-get install -y software-properties-common");
        image.addRUN("apt install gcc -y");
        image.addRUN("add-apt-repository ppa:openjdk-r/ppa");
        image.addRUN("apt-get install openjdk-17-jdk -y");
        image.addRUN("/scripts/install_gpiod.sh 1.6.3 /usr/");
//        image.addRUN("gcc /files/togglePin.c -lgpiod -o /files/togglePin");
//        image.addRUN("chmod +x /files/togglePin");
        ProcessHandler imageHandler = image.build(terminalSubject);
        imageHandler.setOnComplete(handle -> {
            terminalSubject.onNext(new TerminalMessage("Execution image ready",Color.green));
        });
    }
    public void prepare(){
        String execName = this.view.getSelectedValue();
        String name = "execution_image";
        DockerContainer container = new DockerContainer("execution_container",this.executionImage);
        container.setEntrypointOverride("/scripts/execution_entrypoint.sh");
        container.setVolume("/output","/output");
        container.setVolume("/files","/files");
        //container.addENV("ROUND","0");
        container.addARG("java -cp /output/" + execName + " " + (execName.split("_")[1].charAt(0) + "").toUpperCase() + execName.split("_")[1].substring(1));
        container.addARG(this.view.getAmountOfRounds());
        container.isPrivileged(true);
        container.run(this.terminalSubject).setOnComplete((ph) -> {
            terminalSubject.onNext(new TerminalMessage("Container " + container.getName() +  " is open", Color.pink));
            container.stop(terminalSubject).setOnComplete(dolk -> {
                dolk.printLogFiles(name);
                container.remove(terminalSubject).setOnComplete(dole -> {
                    terminalSubject.onNext(new TerminalMessage("Removed container",Color.GREEN));
                    dole.printLogFiles(name + "removal");
                });
            });
        });
    }
}
