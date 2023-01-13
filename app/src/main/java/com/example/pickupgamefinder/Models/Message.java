package com.example.pickupgamefinder.Models;

import com.google.firebase.Timestamp;

public class Message {

    public String id = "";
    public String creator;
    public String message;
    public Timestamp timestamp;

    public Message(){}

    public Message(String id, String creator, String groupChatId, String message, Timestamp timestamp)
    {
        this.id = id;
        this.creator = creator;
        this.message = message;
        this.timestamp = timestamp;

    }

}
