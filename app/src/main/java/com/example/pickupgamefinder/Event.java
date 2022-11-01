package com.example.pickupgamefinder;

public class Event {

    public String eventName = "";
    public String caption = "";
    public int skillLevel = 0;
    public int currentPlayerCount = 0;
    public int maxPlayers = 0;
    public double latitude = 0;
    public double longitude = 0;

    public Event(String eventName, String caption, int skillLevel, int currentPlayerCount, int maxPlayers,
                 double latitude, double longitude)
    {
        this.eventName = eventName;
        this.caption = caption;
        this.skillLevel = skillLevel;
        this.currentPlayerCount = currentPlayerCount;
        this.maxPlayers = maxPlayers;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
