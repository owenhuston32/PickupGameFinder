package com.example.pickupgamefinder.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

<<<<<<< Updated upstream
=======
import com.example.pickupgamefinder.AccountRepository;
import com.example.pickupgamefinder.Event;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

    public void LoadUserEvents(ICallback callback) {
        Log.d("AccountViewModel", "loading users");
        dbUserRef.child("events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    callback.onCallback(liveEventNameList.getValue());
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("AccountViewModel", "load user complete & successful");
                    if (liveEventNameList.getValue() != null)
                    {
                        liveEventNameList.getValue().clear();
                    }

                    if (task.getResult() != null) {
                        Log.d("AccountViewModel", "load user event list not null");
                        List<String> list = new ArrayList<String>();
                        for (String s : (List<String>) task.getResult().getValue()) {
                            Log.d("AccountViewModel", "loading event: " + s);
                            list.add(s);
                        }
                        liveEventNameList.setValue(list);

                        callback.onCallback(liveEventNameList.getValue());
                    }
                }
            }
        });
    }
=======
>>>>>>> Stashed changes
}
