package com.example.pickupgamefinder.Repositories;

import android.util.Log;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class EventRepository {

    private MainActivity mainActivity;
    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public EventRepository(MainActivity mainActivity, EventsViewModel eventsViewModel, AccountViewModel accountViewModel
            , FirebaseDatabase database, DatabaseReference dbRef)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.database = database;
        this.dbRef = dbRef;
    }

    public void addEvent(Event event, ICallback callback) {
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/events/" + event.eventName, event);
        childUpdates.put("/users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + event.eventName, 0);

        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful()) {

                    addEventToEvents(event);

                    addEventToCreatedEvents(event);

                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    private void addEventToEvents(Event event)
    {
        List<Event> list = eventsViewModel.liveEventList.getValue();
        if(list == null)
            list = new ArrayList<Event>();

        list.add(event);
        eventsViewModel.liveEventList.setValue(list);
    }

    private void addEventToCreatedEvents(Event event)
    {
        List<String> nameList = accountViewModel.liveUser.getValue().createdEventNames;
        if(nameList == null)
            nameList = new ArrayList<String>();
        nameList.add(event.eventName);
        User user = accountViewModel.liveUser.getValue();
        user.createdEventNames = nameList;
        accountViewModel.liveUser.setValue(user);
    }

    public void getEvent(String eventName, ICallback callback) {

        dbRef.child("events").child(eventName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful() && task.getResult().getValue() != null) {

                    DataSnapshot snapshot = task.getResult();
                    Event e = createEventFromSnapshot(snapshot);
                    eventsViewModel.liveEvent.setValue(e);

                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                    Log.e("firebase", "Error getting data", task.getException());

                }
            }
        });
    }
    private Event createEventFromSnapshot(DataSnapshot snapshot)
    {
        return new Event(String.valueOf(snapshot.child("eventName").getValue()),
                String.valueOf(snapshot.child("caption").getValue()),
                Integer.parseInt(String.valueOf(snapshot.child("skillLevel").getValue())),
                Integer.parseInt(String.valueOf(snapshot.child("currentPlayerCount").getValue())),
                Integer.parseInt(String.valueOf(snapshot.child("maxPlayers").getValue())),
                Double.parseDouble(String.valueOf(snapshot.child("latitude").getValue())),
                Double.parseDouble(String.valueOf(snapshot.child("longitude").getValue())));
    }

    public void loadEvents(ICallback callback)
    {
        dbRef.child("events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful() && task.getResult().hasChildren()) {

                    List<Event> list = new ArrayList<Event>();
                    for(DataSnapshot childrenSnapshot : task.getResult().getChildren())
                    {
                        list.add(createEventFromSnapshot(childrenSnapshot));
                    }
                    eventsViewModel.liveEventList.setValue(list);
                    callback.onCallback(true);

                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }
    public void setCurrentPlayerCount(int oldPlayerCount, int newCurrentPlayerCount, Event event, ICallback callback) {

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/events/" + event.eventName + "/currentPlayerCount",  newCurrentPlayerCount);

        //if old player count > new player count this means we left the event
        // set the val to null to delete the entry from the database under joined events
        Integer val = null;
        if(newCurrentPlayerCount > oldPlayerCount)
        {
            val = 0;
        }
        childUpdates.put("/users/" + accountViewModel.liveUser.getValue().username + "/joinedEvents/" + event.eventName, val);


        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        mainActivity.hideLoadingScreen();
                        if (task.isSuccessful()) {

                            // change current player count on live event
                            Event e = eventsViewModel.liveEvent.getValue();
                            e.currentPlayerCount = newCurrentPlayerCount;
                            eventsViewModel.liveEvent.setValue(e);

                            if(oldPlayerCount > newCurrentPlayerCount)
                            {
                                leaveEvent(event);
                            }
                            else
                            {
                                joinEvent(event);
                            }

                            callback.onCallback(true);
                        } else {
                            callback.onCallback(false);
                        }

                    }
                });
    }
    private void leaveEvent(Event event)
    {
        // remove event name to user's joined events
        User user = accountViewModel.liveUser.getValue();
        user.joinedEventNames.remove(event.eventName);
        accountViewModel.liveUser.setValue(user);
    }
    private void joinEvent(Event event)
    {
        // add event name to user's joined events
        User user = accountViewModel.liveUser.getValue();
        if(user.joinedEventNames == null)
            user.joinedEventNames = new ArrayList<String>();
        user.joinedEventNames.add(event.eventName);
        accountViewModel.liveUser.setValue(user);
    }

    public void deleteEvent(Event event, ICallback callback) {

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/events/" + event.eventName,  null);
        childUpdates.put("/users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + event.eventName, null);

        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mainActivity.hideLoadingScreen();

                if (task.isSuccessful()) {

                    eventsViewModel.liveEvent.setValue(null);

                    // remove event from created events list
                    User user = accountViewModel.liveUser.getValue();
                    user.createdEventNames.remove(event.eventName);
                    accountViewModel.liveUser.setValue(user);

                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }

            }
        });
    }

}
