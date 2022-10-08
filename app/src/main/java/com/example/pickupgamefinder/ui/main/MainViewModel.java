package com.example.pickupgamefinder.ui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Map<String,String>> users;

    public MutableLiveData<Map<String,String>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<Map<String,String>>();
            users.setValue(new HashMap<String,String>());
            loadUsers();
        }
        return this.users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public void addUser(String username, String password)
    {
        Map<String,String> temp = getUsers().getValue();

        temp.put(username, password);

        users.setValue(temp);
    }

}