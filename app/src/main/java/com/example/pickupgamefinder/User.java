package com.example.pickupgamefinder;

import java.util.ArrayList;
import java.util.List;

public class User {
    public Long id;
    public String username;
    public String password;
    public List<String> createdEventNames;
    public List<String> joinedEventNames;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.pickupgamefinder.User.class)
    }

    public User(Long id, String username, String password, List<String> createdEventNames, List<String> joinedEventNames) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdEventNames = createdEventNames;
        this.joinedEventNames = joinedEventNames;
    }

}
