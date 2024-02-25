package org.example.Docker;

import org.example.Utility.ProcessHandler;
import org.example.files.FileIO;

import java.util.ArrayList;
import java.util.List;

public class DockerImage {
    private List<String> volumes = new ArrayList<>();
    private List<String> envs = new ArrayList<>();
    private List<String> runs = new ArrayList<>();
    private List<String> copys = new ArrayList<>();
    private List<String> cmds = new ArrayList<>();
    private String name;
    private String image;
    private String entryPoint = "/bin/bash";
    private FileIO fileLocation;

    public DockerImage(String image, String name){
        this.image = image;
        this.name = name.toLowerCase();
    }
    public String getName(){
        return this.name;
    }
    public void addENV(String key,String value){
        this.envs.add(key + "=" + value);
    }
    public void addVolume(String volume){
        this.volumes.add(volume);
    }
    public void addRUN(String run){
        this.runs.add(run);
    }
    public void addCOPY(String from, String to){
        this.copys.add(from + " " + to);
    }
    public void addCMD(String cmd){
        this.cmds.add(cmd);
    }
    public void setEntrypoint(String entryPoint){
        this.entryPoint = entryPoint;
    }
    private void write(){
        this.fileLocation = new FileIO(FileIO.getApplicationRootPath(), "Dockerfile");
        this.fileLocation.write(this.toString());
    }
    public ProcessHandler build(){
        this.write();
        String[] cmd = new String[]{"docker", "build", "-t", this.name, "-f",this.fileLocation.getPath().toString(),"."};
        return ProcessHandler.construct(cmd);
    }
    public ProcessHandler remove(){
        String[] cmd = new String[]{"docker","rmi",this.name};
        return ProcessHandler.construct(cmd);
    }
    @Override
    public String toString(){
        StringBuilder content = new StringBuilder();
        content.append("FROM ").append(this.image).append("\n");
        content.append("\n");
        //
        this.volumes.forEach(volume -> content.append("VOLUME ").append(volume).append("\n"));
        content.append("\n");

        //
        this.envs.forEach(env -> content.append("ENV ").append(env).append("=unset").append("\n"));
        content.append("\n");
        //
        this.copys.forEach(cpy -> content.append("COPY ").append(cpy));
        content.append("\n");
        //
        this.runs.forEach(run -> content.append("RUN ").append(run).append("\n"));
        content.append("\n");

        //
        this.cmds.forEach(cmd -> content.append("CMD ").append(cmd).append("\n"));
        content.append("\n");

        //
        content.append("ENTRYPOINT ").append(this.entryPoint);
        return content.toString();
    }
    public static DockerImage getBasic(String image, String name){
        //"openjdk:11"
        //"testImage"
        DockerImage dockerImage = new DockerImage(image,name);
        dockerImage.addENV("EVENT_NAME","");
        dockerImage.addENV("EVENT_INSTALL","");
        dockerImage.addENV("EVENT_COMPILE_COMMAND","");
        dockerImage.addVolume("/scripts");
        dockerImage.addVolume("/installs");
        dockerImage.addVolume("/compile_commands");
        dockerImage.addVolume("/codebase");
        dockerImage.addVolume("/output");
   //     dockerImage.addCOPY("/scripts/compilation_entrypoint.sh","/compilation_entrypoint.sh");
     //   dockerImage.addRUN("chmod +x /compilation_entrypoint.sh");
//        dockerfile.addRUN("ls");
//        dockerfile.addCOPY("/scripts/execution_entrypoint.sh","/execution_entrypoint.sh");
//        dockerfile.addRUN("chmod +x /execution_entrypoint.sh");
        //dockerImage.addCMD("echo \"$(ls)\"\n");

        return dockerImage;
    }
}
