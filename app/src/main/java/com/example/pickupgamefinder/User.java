package com.example.pickupgamefinder;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String password;
    public List<String> eventNames;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.pickupgamefinder.User.class)
    }

    public User(String username, String password, List<String> eventNames) {
        this.username = username;
        this.password = password;
        this.eventNames = eventNames;
    }

}
