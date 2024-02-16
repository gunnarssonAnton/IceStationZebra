package org.example.Docker;

import io.reactivex.rxjava3.core.Observable;
import org.example.Utility.ProcessHandler;
import org.example.files.FileIO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DockerContainer{
    private final String name;
    private final Dockerfile dockerFile;
    private Map<String,String> volumes = new Hashtable<>();
    private Map<String,String> envs = new Hashtable<>();
    public DockerContainer(String name, Dockerfile dockerFile){
        this.name = name.toLowerCase();
        this.dockerFile = dockerFile;
    }
    public void setEnv(String key, String value){
        this.envs.put(key,value);
    }
    public void setVolume(String key, String value){
        this.volumes.put(key,value);

    }
    private String[] compileCMD(String[] args){
        List<String> base = new ArrayList<>(Arrays.asList("docker", "run", "-i", "--name", this.name));

        this.envs.forEach((key, value) -> {
            base.add("-e" + key + "=\"" + value + "\"");
        });
        this.volumes.forEach((key, value) -> {
            if (key.startsWith("./"))
                key = key.substring(2);
            Path path = Paths.get(FileIO.getApplicationRootPath(key)).toAbsolutePath();
            base.add("-v" + path + ":" + value);
        });
        base.add(this.dockerFile.getName());
        Collections.addAll(base, args);
        return base.toArray(new String[0]);
    }
    public Observable<String> run(String[] args){
        //docker run -d --name container_name image_name
        String[] cmd = compileCMD(args);
        System.out.println("CMD ->" + String.join(",",cmd));
        return ProcessHandler.getOutput(cmd);
    }
    public Observable<String> stop(){
        //docker stop my_nginx
        String[] cmd = new String[]{"docker", "stop", this.name};
        return ProcessHandler.getOutput(cmd);
    }
    public Observable<String> remove(){
        String[] cmd = new String[]{"docker", "rm", this.name};
        return ProcessHandler.getOutput(cmd);
    }
}
/*
Dockerfile dockerfile = new Dockerfile("openjdk:11","testImage");
        dockerfile.addENV("COMPILER_NAME","");
        dockerfile.addENV("COMPILER_WHATEVER","");
        dockerfile.addVolume("/installs");
        dockerfile.addVolume("/output");
        dockerfile.addRUN("ls | echo");
        dockerfile.addCMD("ls | echo");
        FileIO file = new FileIO(FileIO.getApplicationRootPath(),"Dockerfile");
        file.write(dockerfile.toString());
        dockerfile.build(file.getPath().toString()).subscribe(System.out::println);

        Thread.sleep(3000);
        System.out.println("Removing...");
        Thread.sleep(3000);
        dockerfile.remove().subscribe(System.out::println);

 */