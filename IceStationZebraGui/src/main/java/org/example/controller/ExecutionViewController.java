package org.example.controller;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Docker.DockerContainer;
import org.example.Docker.DockerImage;
import org.example.Utility.ISZTest;
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
    DockerImage baseExecutionImage = null;
    public ExecutionViewController(PublishSubject<TerminalMessage> subject, CompilationViewController compilationViewController){
        this.terminalSubject = subject;
        this.compilationViewController = compilationViewController;
        this.view.setPrepOnClick(e -> {
            this.prepare();
        });
        this.view.setGenerateExecutionImageOnClick(e -> {
            this.generateExecutionImage();
        });
        this.view.setGenerateBaseExecutionImageOnClick(e -> {
            this.generateBaseExecutionImage();
        });
        this.view.setGoOnClick(e -> {
            this.go();
        });
    }
    public ExecutionView getView(){
        return this.view;
    }
    public void generateBaseExecutionImage(){
        String name = "base_execution_image";
        this.terminalSubject.onNext(new TerminalMessage("Preparing " + name,Color.GREEN));

        //
        DockerImage image = new DockerImage(Event.DOCKERIMAGE, name);
        this.baseExecutionImage = image;
        System.out.println("[toString]\n" + image.toString());

        // Installs
        image.addRUN("apt update");
        image.addRUN("apt-get update && apt-get upgrade -y");
        image.addRUN("apt-get install -y software-properties-common");
        image.addRUN("apt install gcc -y");
        image.addRUN("add-apt-repository ppa:openjdk-r/ppa");
        image.addRUN("apt-get install openjdk-17-jdk -y");
        image.addRUN("apt install libgpiod-dev -y");
        image.addRUN("apt install python3-pip -y");
        image.addRUN("pip install zeroconf");
        image.addRUN("pip install psutil");
        image.addRUN("pip install pyvisa-py");
        image.addRUN("pip install pyvisa");
        image.addRUN("pip install matplotlib");
        image.addRUN("pip install pyRAPL");
        image.addRUN("apt-get install libusb-1.0-0-dev -y");
        image.addRUN("pip install pyusb");

        ProcessHandler imageHandler = image.build(terminalSubject);
        imageHandler.setOnComplete(handle -> {
            terminalSubject.onNext(new TerminalMessage("Execution image ready",Color.green));
        });
    }
    public void generateExecutionImage(){
//        String execName = this.view.getSelectedValue();
//        String name = this.view.getSelectedValue().split("_")[0] + "_image";
        String name = "execution_image";
        System.out.println("names:" + this.compilationViewController.getImages().keySet());
        this.terminalSubject.onNext(new TerminalMessage("Preparing " + name,Color.YELLOW));
        //
        DockerImage image = new DockerImage("base_execution_image", name);
        this.executionImage = image;

        image.addCOPY("/files","/files");
        image.addVolume("/output");
        image.addVolume("/codebase");
        image.addCOPY("/scripts","/scripts");
        image.addVolume("/RAPL");

        System.out.println("[toString]\n" + image.toString());
        image.addRUN("chmod +x /scripts/pre-execution.sh");
        image.addRUN("chmod +x /scripts/execution.sh");
        image.addRUN("chmod +x /scripts/post-execution.sh");
        image.addRUN("chmod +x /scripts/execution_entrypoint.sh");
        image.addCMD("make -C /RAPL/ Makefile");
//        image.addRUN("gcc /files/togglePin.c -lgpiod -o /files/togglePin");
//        image.addRUN("chmod +x /files/togglePin");

        ProcessHandler imageHandler = image.build(terminalSubject);
        imageHandler.setOnComplete(handle -> {
            terminalSubject.onNext(new TerminalMessage("Execution image ready",Color.green));
        });
    }
    public void prepare(){
        //String execName = this.view.getSelectedValue();
        String name = "execution_image";
        DockerContainer container = new DockerContainer("execution_container",new DockerImage("","execution_image"));
        container.addENV("ROUND","0");
        container.isPrivileged(true);
        //container.setEntrypointOverride("/scripts/execution_entrypoint.sh");
        container.setVolume("/output","/output");
        container.setVolume("/RAPL","/RAPL");
        //container.setVolume("/files","/files");
        container.setVolume("/codebase","/codebase");
        //container.setVolume("/scripts","/scripts");
        //container.addARG("java -cp /output/" + execName + " " + (execName.split("_")[1].charAt(0) + "").toUpperCase() + execName.split("_")[1].substring(1));
        //container.addARG(this.view.getAmountOfRounds());

        if (!container.isRunning()) {
            container.run(this.terminalSubject).setOnComplete((ph) -> {
                terminalSubject.onNext(new TerminalMessage("Container " + container.getName() + " is open", Color.pink));
                container.stop(terminalSubject).setOnComplete(dolk -> {
                    dolk.printLogFiles(name);
                    container.remove(terminalSubject).setOnComplete(dole -> {
                        terminalSubject.onNext(new TerminalMessage("Removed container", Color.GREEN));
                        dole.printLogFiles(name + "removal");
                    });
                });
            });
        }
        else {
            System.out.println("Execution container already up");;
        }
    }
    public void go(){
        DockerContainer container = new DockerContainer("execution_container",new DockerImage("base_execution_image","execution_image"));
        container.addENV("ROUNDS",this.getView().getAmountOfRounds() + "");
//        container.exec(new String[]{"-e","ROUNDS",this.getView().getAmountOfRounds() + ""},terminalSubject).setOnComplete(processHandler -> {
//            terminalSubject.onNext(new TerminalMessage("execution container updated",Color.GREEN));

            // Run executable in container
            String execName = this.view.getSelectedValue();
//            "java -cp /output/" + execName + " " + (execName.split("_")[1].charAt(0) + "").toUpperCase() + execName.split("_")[1].substring(1)
            String[] cmd = new String[]{"/scripts/execution_entrypoint.sh", this.getView().getAmountOfRounds() + "", "java", "-cp", "/output/" + execName + "/" , (execName.split("_")[1].charAt(0) + "").toUpperCase() + execName.split("_")[1].substring(1)};

            container.exec(cmd,terminalSubject);
//        });



//        DockerContainer container = new DockerContainer("execution_container",new DockerImage("base_execution_image","execution_image"));
//            Event event = IceHandler.getInstance().getSpecificEvent(this.getView().getSelectedValue());
//            String compileCommand = iszTest.constructCompileCommand(event);
//            ProcessHandler handler = container.exec(compileCommand.split(" "), this.terminalSubject); // container.setEntrypointOverride("/scripts/execution_entrypoint.sh");

        // docker exec -it my_container bash
    }
}
