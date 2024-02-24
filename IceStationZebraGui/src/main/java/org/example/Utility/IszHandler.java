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

public class IszHandler {
    private final ObjectWriter objectWriter = new ObjectMapper().writer().with(SerializationFeature.INDENT_OUTPUT);
    private FileIO iszFile = new FileIO(FileIO.getApplicationRootPath("settings"),"config.isz");
    private final JSONArray iszArray = new JSONArray();


    public IszHandler(){
        this.getGivenNames();
    }

    public void writeToIsz(Event event){
        iszArray.put(event.toIsz());
        Config config = new Config(99,"name", iszArray.toList());
        try {
            this.iszFile.write(objectWriter.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeToIsz(Set<Event> events){
        events.stream().map(Event::toIsz).forEach(iszArray::put);
        Config config = new Config(99,"name", iszArray.toList());
        try {
            this.iszFile.write(objectWriter.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public Set<String> getGivenNames(){
        Set<String> givenNameSet = new HashSet<>();
        JSONObject jsonObject = new JSONObject(this.iszFile.read());
        ((JSONArray) jsonObject.get("events")).forEach(obj->{
            JSONObject iszEvent = new JSONObject(obj.toString());

            givenNameSet.add(iszEvent.get("givenName").toString());
        });
        System.out.println(givenNameSet);
        return givenNameSet;
    }

    public Set<Event> getEvents(){
        Set<Event> eventSet = new HashSet<>();
        JSONObject startObj = new JSONObject(this.iszFile.read());
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
    public String readIsz() {
        System.out.println(this.iszFile.read());
        return this.iszFile.read();
    }



}
