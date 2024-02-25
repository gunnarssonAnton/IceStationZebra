package org.example.models;

import org.json.JSONObject;

public record Config(
        int global,
        String user,
        java.util.List<Object> events
) {
    public JSONObject toIce(){
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("global", global);
        jsonConfig.put("user", user);
        jsonConfig.put("events",events);
        return jsonConfig;
    }
}



