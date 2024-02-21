package org.example.Docker;

import io.reactivex.rxjava3.core.Observable;
import org.example.Utility.ProcessHandler;
import org.example.files.FileIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dockerfile {
    private List<String> volumes = new ArrayList<>();
    private List<String> envs = new ArrayList<>();
    private List<String> runs = new ArrayList<>();
    private List<String> cmds = new ArrayList<>();
    private String name;
    private String image;
    private String entryPoint = "/bin/bash";

    public Dockerfile(String image, String name){
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
    public void addCMD(String cmd){
        this.cmds.add(cmd);
    }
    public void setEntrypoint(String entryPoint){
        this.entryPoint = entryPoint;
    }
    public Observable<String> build(String path){
        if (path == null)
            path = ".";
        String[] cmd = new String[]{"docker", "build", "-t", this.name, "-f",path,"."};
        return ProcessHandler.getOutput(cmd);
    }
    public Observable<String> remove(){
        String[] cmd = new String[]{"docker","rmi",this.name};
        return ProcessHandler.getOutput(cmd);
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
        this.envs.forEach(env -> content.append("ENV ").append(env).append("=Dolk").append("\n"));
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
    public static Dockerfile getBasic(String image, String name){
        //"openjdk:11"
        //"testImage"
        Dockerfile dockerfile = new Dockerfile(image,name);
        dockerfile.addENV("COMPILER_NAME","");
        dockerfile.addENV("COMPILER_WHATEVER","");
        dockerfile.addVolume("/scripts");
        dockerfile.addVolume("/installs");
        dockerfile.addVolume("/compile_commands");
        dockerfile.addVolume("/codebase");
        dockerfile.addVolume("/output");
        dockerfile.addRUN("echo \"$(ls)\"\n");
        dockerfile.addCMD("echo \"$(ls)\"\n");

        return dockerfile;
    }
}
