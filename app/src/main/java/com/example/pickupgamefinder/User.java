package com.example.pickupgamefinder;

public class User {
    public String username;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.pickupgamefinder.User.class)
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
