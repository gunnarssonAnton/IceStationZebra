package org.example.models;

import org.json.JSONObject;

import java.util.List;

public record Config(
        int global,
        String user,
        List<Event> events
) {
    public JSONObject toIce(){
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("global", global);
        jsonConfig.put("user", user);
        jsonConfig.put("events",events);
        return jsonConfig;
    }
}



