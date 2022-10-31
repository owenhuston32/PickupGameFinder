package com.example.pickupgamefinder.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    public MutableLiveData<List<Event>> liveEventList = new MutableLiveData<List<Event>>();
    public FirebaseDatabase database;
    public DatabaseReference eventsRef;

    public void addEvent(Event event)
    {
        List list = liveEventList.getValue();
        if(list == null)
        {
            list = new ArrayList();
        }
        list.add(event);

        liveEventList.setValue(list); // Create user class for firebase DB
        eventsRef.child(event.eventName).setValue(event);  // database
    }

    public void getEvent(Event event, ICallback callback) {

        eventsRef.child(event.eventName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    callback.onCallback(new Event("","",0,0,0));
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue() == null)
                    {
                        callback.onCallback(new Event("", "", 0, 0, 0));
                        Log.d("firebase", "user not found in database");
                    }
                    else
                    {
                        DataSnapshot snapshot = task.getResult();
                        callback.onCallback(CreateEventFromSnapshot(snapshot));
                        Log.d("firebase", "successfully found user");
                    }
                }
            }
        });
    }

    public void loadEvents(ICallback callback)
    {
        eventsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    callback.onCallback(liveEventList.getValue());
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    if(liveEventList.getValue() != null)
                        liveEventList.getValue().clear();

                    if(task.getResult() != null)
                    {
                        List<Event> list = new ArrayList<Event>();
                        for(DataSnapshot childrenSnapshot : task.getResult().getChildren())
                        {
                            list.add(CreateEventFromSnapshot(childrenSnapshot));
                            Log.d("tag", childrenSnapshot.toString());
                        }
                        liveEventList.setValue(list);

                        callback.onCallback(liveEventList.getValue());
                    }


                }

            }
        });


    }

    private Event CreateEventFromSnapshot(DataSnapshot snapshot)
    {

        return new Event(String.valueOf(snapshot.child("eventName").getValue()),
                String.valueOf(snapshot.child("caption").getValue()),
                Integer.parseInt(String.valueOf(snapshot.child("skillLevel").getValue())),
                Integer.parseInt(String.valueOf(snapshot.child("currentPlayerCount").getValue())),
                Integer.parseInt(String.valueOf(snapshot.child("maxPlayers").getValue())));

    }

    public void DeleteEvent(Event event) {
        eventsRef.child(event.eventName).removeValue();
    }
}