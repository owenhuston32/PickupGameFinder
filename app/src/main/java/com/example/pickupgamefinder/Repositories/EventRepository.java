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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class EventRepository {

    private MainActivity mainActivity;
    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;
    private DatabaseReference dbRef;

    public EventRepository(MainActivity mainActivity, EventsViewModel eventsViewModel, AccountViewModel accountViewModel
            , DatabaseReference dbRef)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.dbRef = dbRef;
    }

    public void addEvent(Event event, ICallback callback) {

        dbRef.child("server").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Long id = currentData.child("events").getChildrenCount();

                event.id = id.toString();
                currentData.child("events/" + id).setValue(event);
                currentData.child("users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + id).setValue(0);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                mainActivity.hideLoadingScreen();

                if(committed)
                {
                    User user = accountViewModel.liveUser.getValue();
                    user.addIdToList(event.id, user.createdEventIds);

                    eventsViewModel.addToLiveEventList(event);
                    callback.onCallback(true);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }

    public void getEvent(String eventName, ICallback callback) {

    }

    public void loadEvents(ICallback callback)
    {

    }
    public void setCurrentPlayerCount(int oldPlayerCount, int newCurrentPlayerCount, Event event, ICallback callback) {


    }
    private void leaveEvent(Event event)
    {

    }
    private void joinEvent(Event event)
    {

    }

    public void deleteEvent(Event event, ICallback callback) {

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/events/" + event.id,  null);
        childUpdates.put("/users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + event.id, null);

        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mainActivity.hideLoadingScreen();

                if (task.isSuccessful()) {

                    eventsViewModel.liveEvent.setValue(null);

                    // remove event from created events list
                    User user = accountViewModel.liveUser.getValue();
                    user.createdEventIds.remove(event.id);
                    accountViewModel.liveUser.setValue(user);

                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }

            }
        });
    }

}
