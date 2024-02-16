package org.example.Docker;

import io.reactivex.rxjava3.core.Observable;
import org.example.Utility.ProcessHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DockerContainer{
    private final String name;
    private final Dockerfile dockerFile;
    public DockerContainer(String name, Dockerfile dockerFile){
        this.name = name.toLowerCase();
        this.dockerFile = dockerFile;
    }
    public Observable<String> up(){
        //docker run -d --name container_name image_name
        String[] cmd = new String[]{"docker", "run", "--name", this.name, this.dockerFile.getName()};
        return ProcessHandler.getOutput(cmd);
    }
    public Observable<String> down(){
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