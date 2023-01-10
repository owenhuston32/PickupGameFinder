package com.example.pickupgamefinder;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String password;
    public List<String> createdEventIds;
    public List<String> joinedEventIds;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.pickupgamefinder.User.class)
    }

    public User(String username, String password, List<String> createdEventIds, List<String> joinedEventIds) {
        this.username = username;
        this.password = password;
        this.createdEventIds = createdEventIds;
        this.joinedEventIds = joinedEventIds;
    }

    public void addIdToList(String id, List<String> list)
    {
        if(list == null)
            list = new ArrayList<String>();

        list.add(id);
    }

}
