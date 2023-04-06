package com.example.pickupgamefinder.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String username;
    public List<String> createdEventIds;
    public List<String> joinedEventIds;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.pickupgamefinder.Models.User.class)
    }

    public User(String username, List<String> createdEventIds, List<String> joinedEventIds) {
        this.username = username;
        this.createdEventIds = createdEventIds;
        this.joinedEventIds = joinedEventIds;
    }

    public void addIDToList(String id, List<String> list)
    {
        if(list == null)
            list = new ArrayList<>();

        list.add(id);
    }

}
