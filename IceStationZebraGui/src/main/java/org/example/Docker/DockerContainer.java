package org.example.Docker;

import org.example.Utility.ProcessHandler;
import org.example.files.FileIO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DockerContainer{
    private final String name;
    private final DockerImage image;
    private Map<String,String> volumes = new Hashtable<>();
    private Map<String,String> envs = new Hashtable<>();
    private List<String> args = new ArrayList<>();
    private String entrypoint = null;
    public DockerContainer(String name, DockerImage image){
        this.name = name.toLowerCase();
        this.image = image;
    }
    public void addENV(String key, String value){
        String theValue = value;
        if (theValue.contains(" "))
            theValue = "\"" + theValue + "\"";
        this.envs.put(key,theValue);
    }
    public void setVolume(String key, String value){
        this.volumes.put(key,value);
    }
    public void addARG(String arg){
        this.args.add(arg);
    }
    public void setEntrypointOverride(String entrypoint){
        this.entrypoint = entrypoint;
    }
    private String[] compileCMD(){
        List<String> base = new ArrayList<>(Arrays.asList("docker", "run", "-i", "--name", this.name));

        this.envs.forEach((key, value) -> {
            base.add("-e");
            base.add(key + "=" + value);
        });
        this.volumes.forEach((key, value) -> {
            if (key.startsWith("./"))
                key = key.substring(2);
            Path path = Paths.get(FileIO.getApplicationRootPath(key)).toAbsolutePath();
            base.add("-v");
            base.add(path + ":" + value);
        });
        if (this.entrypoint != null){
            base.add("--entrypoint");
            base.add(this.entrypoint);
        }
        base.add(this.image.getName());
        base.addAll(this.args);
        return base.toArray(new String[0]);
    }
    public ProcessHandler run(){
        //docker run -d --name container_name image_name
        String[] cmd = compileCMD();
        System.out.println("run:" + String.join(" ",cmd));
        return ProcessHandler.construct(cmd);
    }
    public ProcessHandler stop(){
        //docker stop my_nginx
        String[] cmd = new String[]{"docker", "stop", this.name};
        return ProcessHandler.construct(cmd);
    }
    public ProcessHandler remove(){
        String[] cmd = new String[]{"docker", "rm", this.name};
        return ProcessHandler.construct(cmd);
    }
    public void exec(String[] args){
        String[] base = new String[]{"docker", "exec", this.name};
        String[] cmd = Stream.concat(Arrays.stream(base), Arrays.stream(args))
                .toArray(String[]::new);
        ProcessHandler handler = ProcessHandler.construct(cmd);
    }
}
