package com.example.pickupgamefinder.ui.main;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<String>> userNames;

    public MutableLiveData<List<String>> getUsers() {
        if (userNames == null) {
            userNames = new MutableLiveData<List<String>>();
            loadUsers();
        }
        return this.userNames;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

}