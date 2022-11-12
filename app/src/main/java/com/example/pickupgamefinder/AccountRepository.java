package com.example.pickupgamefinder;

import android.util.Log;

import com.example.pickupgamefinder.ui.main.AccountViewModel;
import com.example.pickupgamefinder.ui.main.EventsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class AccountRepository {

    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public AccountRepository(EventsViewModel eventsViewModel, AccountViewModel accountViewModel
            , FirebaseDatabase database, DatabaseReference dbRef)
    {
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.database = database;
        this.dbRef = dbRef;
    }

    public void addUser(User user, ICallback callback) {
        dbRef.child("users").child(user.username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    accountViewModel.liveUser.setValue(user);
                    callback.onCallback("success");
                }
                else
                {
                    callback.onCallback("fail");
                }
            }
        });
    }
    public void getUser(String username, ICallback callback) {

        dbRef.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getValue() != null) {

                    Log.d("Account Repository", "" + task.getResult().getValue(User.class));

                    accountViewModel.liveUser.setValue(task.getResult().getValue(User.class));
                    callback.onCallback("success");
                } else {
                    callback.onCallback("fail");
                    Log.e("firebase", "failed to getUser");
                }
            }
        });
    }

    public void addToCreatedEvents(String eventName, ICallback callback) {

        List list = accountViewModel.liveUser.getValue().createdEventNames;
        if (list == null) {
            list = new ArrayList();
        }
        list.add(eventName);
        dbRef.child("users").child(accountViewModel.liveUser.getValue().username)
                .child("events").setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            User user = accountViewModel.liveUser.getValue();
                            user.createdEventNames.add(eventName);
                            accountViewModel.liveUser.setValue(user);
                            callback.onCallback("success");
                        } else {
                            callback.onCallback("fail");
                        }
                    }
                });
    }
    public void removeFromUserEventList(String eventName, ICallback callback) {
        dbRef.child("users").child(accountViewModel.liveUser.getValue().username)
                .child("events").child(eventName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onCallback("success");
                        } else {
                            callback.onCallback("fail");
                        }
                    }
                });
    }
    public void loadUserEvents(ICallback callback) {
        dbRef.child("users").child(accountViewModel.liveUser.getValue().username).child("events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().getValue() != null) {
                        List<String> list = new ArrayList<String>();
                        for (String s : (List<String>) task.getResult().getValue()) {
                            list.add(s);
                        }
                        User user = accountViewModel.liveUser.getValue();
                        user.createdEventNames = list;
                        accountViewModel.liveUser.setValue(user);

                        callback.onCallback("success");
                    }
                    else
                    {
                        Log.e("Account repository", "load user event list is null");
                    }
                    callback.onCallback("success");
                } else {
                    Log.e("account repository", "Error loading user events");
                }
            }
        });
    }
}
