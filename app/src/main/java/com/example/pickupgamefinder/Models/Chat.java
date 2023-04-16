package com.example.pickupgamefinder.Models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat implements Serializable {

    public String id;
    public String name;
    public String creatorID;
    public List<User> joinedUserIDS;
    public List<Message> messages;


    public Chat(){}

    public Chat(String id, String name, String creatorID, List<User> joinedUserIDS, List<Message> messages)
    {
        this.id = id;
        this.name = name;
        this.creatorID = creatorID;
        this.joinedUserIDS = joinedUserIDS;
        this.messages = messages;
    }

    public void addInfoFromSnapshot(DataSnapshot snapshot)
    {
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
        if(snapshot.getValue() != null)
        {
            id = (String) map.get("eventID");
            name = (String) map.get("chatName");
            creatorID = (String) map.get("creatorID");
            joinedUserIDS = (List<User>) map.get("joinedUserIDS");
        }
    }

}
