package org.example.models;

import org.json.JSONArray;
import org.json.JSONObject;

public record Event(
        String dockerImage,
        String compileCommand,
        String givenName,
        JSONArray installation) {
    public static final String DOCKERIMAGE = "ubuntu:latest";

    public JSONObject toIsz(){
        JSONObject iszObject = new JSONObject();
        iszObject.put("dockerImage", dockerImage);
        iszObject.put("givenName",givenName);
        iszObject.put("compileCommand", compileCommand);
        iszObject.put("installation",installation);
        return iszObject;
    }
}
