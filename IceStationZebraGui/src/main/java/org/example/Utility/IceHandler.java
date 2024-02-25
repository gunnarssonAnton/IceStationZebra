package org.example.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.files.FileIO;
import org.example.models.Config;
import org.example.models.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IceHandler {

    private static IceHandler INSTANCE = null;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().with(SerializationFeature.INDENT_OUTPUT);
    private final FileIO iceFile = new FileIO(FileIO.getApplicationRootPath("settings"),"config.ice");
    private final JSONArray iceArray = new JSONArray();
    private final Config config = new Config(99,"name", new ArrayList<>());

    private IceHandler(){
//        this.modifyEvent();
    }

    public static IceHandler getInstance(){
        if (INSTANCE == null){
            INSTANCE = new IceHandler();
        }
        return INSTANCE;
    }

    public FileIO getIceFile(){
        return this.iceFile;
    }
    public void addEvent(Event event){
//        iceArray.put(event.toIce());
        this.config.events().add(event);
        this.writeToIce();
    }


    public Set<String> getGivenNames(){
        Set<String> givenNameSet = new HashSet<>();
        JSONObject jsonObject = new JSONObject(this.iceFile.read());
        ((JSONArray) jsonObject.get("events")).forEach(obj->{
            JSONObject iceEvent = new JSONObject(obj.toString());

            givenNameSet.add(iceEvent.get("givenName").toString());
        });
        System.out.println(givenNameSet);
        return givenNameSet;
    }


    public Set<Event> getEvents(){
        return new HashSet<>(this.config.events());
    }

    public Event getSpecificEvent(String givenName){
        Event event = null;
        for (Event ev : this.getEvents()) {
            if (ev.givenName().equals(givenName)){
                event = ev;
            }
        }
        return event;
    }

    public void modifyEvent(String givenName){
        this.getSpecificEvent(givenName).compileCommand();
    }

    public void removeEvent(String givenName){
        this.config.events().remove(this.getSpecificEvent(givenName));
        this.writeToIce();
    }

    public String readIsz() {
        System.out.println(this.iceFile.read());
        return this.iceFile.read();
    }
    private void writeToIce(){
        try {
            this.iceFile.write(objectWriter.writeValueAsString(this.config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
