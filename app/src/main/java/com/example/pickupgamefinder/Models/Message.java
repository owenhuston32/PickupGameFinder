package com.example.pickupgamefinder.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Message implements Serializable {

    public String creator;
    public String messageText;

    public Message(){}

    public Message(String creator, String message)
    {
        this.creator = creator;
        this.messageText = message;

    }

}
