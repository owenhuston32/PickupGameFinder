package com.example.pickupgamefinder.Models;

import java.util.List;

public class Event {

    public String id;
    public String eventName = "";
    public String caption = "";
    public int skillLevel = 0;
    public int maxPlayers = 0;
    public double latitude = 0;
    public double longitude = 0;
    public String creator;
    public List<String> joinedUsers;

    public Event(){};

    public Event(String id, String eventName, String caption, int skillLevel, int maxPlayers,
                 double latitude, double longitude, String creator, List<String> joinedUsers)
    {
        this.id = id;
        this.eventName = eventName;
        this.caption = caption;
        this.skillLevel = skillLevel;
        this.maxPlayers = maxPlayers;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.joinedUsers = joinedUsers;

    }
}
