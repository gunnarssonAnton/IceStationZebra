package org.example.models;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public record Event(
        String dockerImage,
        String compileCommand,
        String givenName,
        List<String> installation
) {
    public static final String DOCKERIMAGE = "ubuntu:latest";

    public JSONObject toIce(){
        JSONObject iszObject = new JSONObject();
        iszObject.put("dockerImage", dockerImage);
        iszObject.put("givenName", givenName);
        iszObject.put("compileCommand", compileCommand);
        iszObject.put("installation", installation);
        return iszObject;
    }

   public String join(){
        return this.installation.stream().skip(0).map(s -> s.contains(";") ? s : s + ";").collect(Collectors.joining()).replaceAll(";$","");
   }
}
