package com.example.lanayusuf.remimemo;

/**
 * Created by areally on 11/5/2016.
 */

public class EventRemimemo {
    private long eventId;
    private String eventName;
    private String eventDescription;
    private String location;
    private  String eventPriority;

    private String editTxtDate;

    private String editTxtTime;

    public EventRemimemo(){
        //Only allows initialization
    }

    public void setEventId(long id){
        //listener to get event id
        eventId = id;
    }

    public long getEventId(){
        return eventId;
    }

    public void setEventName(String event){
        //listener to get event name
        eventName = event;
    }

    public String getEventName(){
        return eventName;
    }

    public void setEventDescription(String event){
        //listener to get event name
        eventDescription = event;
    }

    public String getEventDescription(){
        return eventDescription;
    }

    public void setEventPriority(String priority){
        //listener to get event name
        eventPriority = priority;
    }

    public String getEventPriority(){
        return eventPriority;
    }

    public void setEventLocation(String event){
        //listener to get event name
        location = event;
    }

    public String getEventLocation(){
        return location;
    }

    public void setEditTxtDate(String editDate){
        editTxtDate = editDate;
    }

    public String getEditTxtDate(){
        return editTxtDate;
    }

    public void setEditTxtTime(String editTime){
        editTxtTime = editTime;
    }

    public String getEditTxtTime(){
        return editTxtTime;
    }

}
