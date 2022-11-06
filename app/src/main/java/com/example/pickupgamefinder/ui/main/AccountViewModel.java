package com.example.pickupgamefinder.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends ViewModel {

    public MutableLiveData<List<String>> liveEventNameList = new MutableLiveData<List<String>>();
    public MutableLiveData<User> liveUser = new MutableLiveData<User>();

    public FirebaseDatabase database;
    public DatabaseReference dbUserRef;

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public void addUser(String username, String password, List<String> eventNames) {
        User user = new User(username, password, eventNames); // Create user class for firebase DB
        dbUserRef.child(username).setValue(user);  // database
        liveUser.setValue(user);

    }

    public void getUser(String username, ICallback callback) {

        dbUserRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    callback.onCallback(new User("", "", new ArrayList<String>()));
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() == null) {
                        callback.onCallback(new User("", "", new ArrayList<String>()));
                        Log.d("firebase", "user not found in database");
                    } else {

                        List<String> eventNames = new ArrayList<String>();
                        for(DataSnapshot snapshot : task.getResult().child("events").getChildren())
                        {
                            eventNames.add(snapshot.getValue().toString());
                        }

                        callback.onCallback(
                                new User(username
                                        ,String.valueOf(task.getResult().child("password").getValue())
                                        ,eventNames));
                        Log.d("firebase", "successfully found user");
                    }
                }
            }
        });
    }

    public void UpdatePassword(String username, String newPassword) {
        dbUserRef.child(username).child("password").setValue(newPassword);
        liveUser.setValue(new User(username, newPassword, liveEventNameList.getValue()));
    }

    public void DeleteUser(String username) {
        liveUser.setValue(new User("", "", new ArrayList<String>()));
        dbUserRef.child(username).removeValue();
    }

    public void AddToUserEventList(String eventName) {
        List list = liveEventNameList.getValue();
        if (list == null) {
            list = new ArrayList();
        }
        list.add(eventName);

        liveEventNameList.setValue(list);
        dbUserRef.child(liveUser.getValue().username).child("events").setValue(list);
    }

    public void RemoveFromUserEventList(String eventName) {
        dbUserRef.child(liveUser.getValue().username).child("events").child(eventName).removeValue();
        liveEventNameList.getValue().remove(eventName);
    }

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
}
