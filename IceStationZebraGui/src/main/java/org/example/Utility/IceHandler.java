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

import java.util.HashSet;
import java.util.Set;

public class IceHandler {

    private static IceHandler INSTANCE = null;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().with(SerializationFeature.INDENT_OUTPUT);
    private final FileIO iceFile = new FileIO(FileIO.getApplicationRootPath("settings"),"config.ice");
    private final JSONArray iceArray = new JSONArray();


    private IceHandler(){
        this.modifiEvent();
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
    public void writeToIce(Event event){
        iceArray.put(event.toIce());
        Config config = new Config(99,"name", iceArray.toList());
        try {
            this.iceFile.write(objectWriter.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeToIce(Set<Event> events){
        events.stream().map(Event::toIce).forEach(iceArray::put);
        Config config = new Config(99,"name", iceArray.toList());
        try {
            this.iceFile.write(objectWriter.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        Set<Event> eventSet = new HashSet<>();
        JSONObject startObj = new JSONObject(this.iceFile.read());
        ((JSONArray) startObj.get("events")).forEach(obj->{
            JSONObject iszEvent = new JSONObject(obj.toString());
            eventSet.add(
                    new Event(
                            iszEvent.getString("dockerImage"),
                            iszEvent.getString("compileCommand"),
                            iszEvent.getString("givenName"),
                            iszEvent.getJSONArray("installation")
                    ));
        });
        return eventSet;
    }

    public Event getSpecificEvent(String givenName){
        Event event = null;
        for (Event event1 : this.getEvents()) {
            if(event1.givenName().equals(givenName)){
                event = event1;
            }
        }
        return event;
    }

    public void modifiEvent(){
        for (Object o : this.iceArray) {
            System.out.println(o);
        }
//         this.iceArray.getJSONObject()
    }
    public String readIsz() {
        System.out.println(this.iceFile.read());
        return this.iceFile.read();
    }



}
