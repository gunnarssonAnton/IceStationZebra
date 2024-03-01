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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IceHandler {

    private static IceHandler INSTANCE = null;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().with(SerializationFeature.INDENT_OUTPUT);
    private final FileIO iceFile = new FileIO(FileIO.getApplicationRootPath("settings"),"config.ice");
    private final Config config = new Config(99,"name", new ArrayList<>());

    private IceHandler(){
        this.getListFromFile();
    }

    public static IceHandler getInstance(){
        if (INSTANCE == null){
            INSTANCE = new IceHandler();
        }
        return INSTANCE;
    }

    /**
     * populates the config object with events from the ice file
     */
    private void getListFromFile(){
        JSONObject jsonObject = new JSONObject(this.iceFile.read());
        ((JSONArray) jsonObject.get("events")).forEach(obj->{
            JSONObject iceEvent = new JSONObject(obj.toString());
            this.config.events().add(
                    new Event(Event.DOCKERIMAGE,
                            iceEvent.getString("compileCommand").toString(),
                            iceEvent.getString("givenName").toString(),
                            this.convertToObjectList(iceEvent.getJSONArray("installation").toList())
                            ));


        });
    }
    private List<String> convertToObjectList(List<Object>objectsList){
        return objectsList.stream().map(Object::toString).collect(Collectors.toList());

    }

    /**
     * adds a new event to Ice file
     * @param event the new event
     */
    public void addEvent(Event event){
        this.config.events().add(event);
        this.writeToIce();
    }


    /**
     * used to get all the name of the events
     * @return a set of event names
     */
    public Set<String> getEventNames(){
        return this.config.events().stream().map(Event::givenName).collect(Collectors.toSet());
    }


    /**
     * getter
     * @return all events as a sets
     */
    public Set<Event> getEvents(){
        return new HashSet<>(this.config.events());
    }

    /**
     * getter
     * @param eventName name of event you want to get
     * @return a specific event
     */
    public Event getSpecificEvent(String eventName){
        Event event = null;
        for (Event ev : this.getEvents()) {

            if (ev.givenName().equals(eventName)){
                event = ev;
            }
        }
        return event;
    }

    /**
     * replaces an old event with a modified version
     * @param nameOfOldEvent the name of the old event
     * @param modifiedEvent the modified event
     */
    public void modifyEvent(String nameOfOldEvent, Event modifiedEvent){
        this.removeEvent(nameOfOldEvent);
        this.addEvent(modifiedEvent);
    }


    /**
     * removes an event from the Ice file
     * @param givenName name of the event you want to remove
     */
    public void removeEvent(String givenName){
        this.config.events().remove(this.getSpecificEvent(givenName));
        this.writeToIce();
    }




    /**
     * used to reed Ice config
     * @return the Ice file
     */
    public String readIce() {
        return this.iceFile.read();
    }

    /**
     * convert the config object to Json and write it to the Ice file
     */
    private void writeToIce(){
        try {
            this.iceFile.write(objectWriter.writeValueAsString(this.config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
