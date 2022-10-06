package com.example.pickupgamefinder.ui.main;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<User>> users;

    public MutableLiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<User>>();
            loadUsers();
        }
        return this.users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

}