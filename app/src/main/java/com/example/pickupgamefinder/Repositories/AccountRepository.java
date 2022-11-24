package com.example.pickupgamefinder.Repositories;

import android.util.Log;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class AccountRepository {

    private MainActivity mainActivity;
    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public AccountRepository(MainActivity mainActivity, EventsViewModel eventsViewModel, AccountViewModel accountViewModel
            , FirebaseDatabase database, DatabaseReference dbRef)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.database = database;
        this.dbRef = dbRef;
    }

    public void addUser(User user, ICallback callback) {
        dbRef.child("users").child(user.username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mainActivity.hideLoadingScreen();
                if(task.isSuccessful())
                {
                    accountViewModel.liveUser.setValue(user);
                    callback.onCallback(true);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }
    public void getUser(String username, ICallback callback) {

        dbRef.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful() && task.getResult().getValue() != null) {

                    Log.d("Account Repository", "" + task.getResult().getValue(User.class));

                    accountViewModel.liveUser.setValue(task.getResult().getValue(User.class));
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                    Log.e("firebase", "failed to getUser");
                }
            }
        });
    }

    public void loadUserEvents(ICallback callback) {

        dbRef.child("users").child(accountViewModel.liveUser.getValue().username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful()) {

                    User user = accountViewModel.liveUser.getValue();

                    if(task.getResult().hasChildren())
                    {
                        GenericTypeIndicator<HashMap<String, Integer>> t = new GenericTypeIndicator<HashMap<String, Integer>>() {};
                        HashMap<String, Integer> createdEventsMap = task.getResult().child("createdEvents").getValue(t);

                        List<String> createdEvents = null;

                        if(createdEventsMap != null)
                        {
                            createdEvents = new ArrayList<String>();
                            for (String s : createdEventsMap.keySet()) {
                                createdEvents.add(s);
                                Log.d("Account Repo", "created event: " + s);
                            }
                        }

                        HashMap<String, Integer> joinedEventsMap = task.getResult().child("joinedEvents").getValue(t);
                        List<String> joinedEvents = null;
                        if(joinedEventsMap != null)
                        {
                            joinedEvents = new ArrayList<String>();
                            for (String s : joinedEventsMap.keySet()) {
                                joinedEvents.add(s);
                                Log.d("Account Repo", "joined event: " + s);
                            }
                        }

                        user.createdEventNames = createdEvents;
                        user.joinedEventNames = joinedEvents;

                        Log.d("Account Repo", "" + user.createdEventNames);
                        Log.d("Account Repo", "" + user.joinedEventNames);

                        accountViewModel.liveUser.setValue(user);

                        callback.onCallback(true);
                    }
                    else
                    {
                        callback.onCallback(false);
                        Log.e("Account repository", "load user event list is null");
                    }
                } else {
                    callback.onCallback(false);
                    Log.e("account repository", "Error loading user events");
                }
            }
        });
    }
}
