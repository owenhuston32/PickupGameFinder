package com.example.pickupgamefinder.Models;

import com.google.firebase.database.DataSnapshot;

import java.sql.DataTruncation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChat {

    public String id;
    public String name;
    public String creator;
    public List<User> joinedUsers;
    public List<Message> messages;


    public GroupChat(){}

    public GroupChat(String id, String name, String creator, List<User> joinedUsers, List<Message> messages)
    {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.joinedUsers = joinedUsers;
        this.messages = messages;
    }

    public void addInfoFromSnapshot(DataSnapshot snapshot)
    {
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
        if(snapshot.getValue() != null)
        {
            id = (String) map.get("id");
            name = (String) map.get("name");
            creator = (String) map.get("creator");
            joinedUsers = (List<User>) map.get("joinedUsers");
        }
    }

}
