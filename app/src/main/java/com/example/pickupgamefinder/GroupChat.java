package com.example.pickupgamefinder;

import java.util.List;

public class GroupChat {

    public String name;
    public String creator;
    public List<User> joinedUsers;
    public List<Message> messages;

    public GroupChat(){}

    public GroupChat(String name, String creator, List<User> joinedUsers, List<Message> messages)
    {
        this.name = name;
        this.creator = creator;
        this.joinedUsers = joinedUsers;
        this.messages = messages;
    }
}
