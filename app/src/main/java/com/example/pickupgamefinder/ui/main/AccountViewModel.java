package com.example.pickupgamefinder.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.AccountRepository;
import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.EventRepository;
import com.example.pickupgamefinder.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends ViewModel {

    public MutableLiveData<User> liveUser = new MutableLiveData<User>();
    public EventRepository eventRepository = null;
    public AccountRepository accountRepository = null;


    public void addUser(User user, ICallback callback) {

        accountRepository.addUser(user, callback);

    }

    public void getUser(String username, ICallback callback) {
        accountRepository.getUser(username, callback);
    }

    public void addToCreatedEvents(String eventName, ICallback callback) {
        accountRepository.addToCreatedEvents(eventName, callback);
    }

    public void removeFromUserEventList(String eventName, ICallback callback) {
        accountRepository.removeFromUserEventList(eventName, callback);
    }

    public void loadUserEvents(ICallback callback)
    {
        accountRepository.loadUserEvents(callback);
    }
}
