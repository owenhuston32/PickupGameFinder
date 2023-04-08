package com.example.pickupgamefinder.Repositories;

import com.example.pickupgamefinder.Models.GroupChat;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Singletons.LoadingScreen;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class EventRepository {

    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;
    private DatabaseReference dbRef;

    public EventRepository(EventsViewModel eventsViewModel, AccountViewModel accountViewModel,DatabaseReference dbRef)
    {
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.dbRef = dbRef;
    }

    public void addEvent(Event event, ICallback callback) {

        dbRef.child("server").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Long eventID = currentData.child("events/count").getValue(Long.class);
                if(eventID == null)
                    eventID = 0L;

                event.id = eventID.toString();
                currentData.child("events/" + event.id).setValue(event);
                currentData.child("users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + event.id).setValue("0");

                currentData.child("groupChats/" + event.id + "/info/id").setValue(event.id);
                currentData.child("groupChats/" + event.id + "/info/name").setValue(event.eventName + " Group");
                currentData.child("groupChats/" + event.id + "/info/creator").setValue(accountViewModel.liveUser.getValue().username);
                currentData.child("groupChats/" + event.id + "/info/joinedUsers").setValue(new ArrayList<User>());
                currentData.child("groupChats/" + event.id + "/info/mCount").setValue(0L);

                currentData.child("events/count").setValue(ServerValue.increment(1));


                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(committed) {
                    User user = accountViewModel.liveUser.getValue();
                    user.addIDToList(event.id, user.createdEventIds);

                    eventsViewModel.addToLiveEventList(event);
                }
                callback.onCallback(committed);
            }
        });
    }

    public void getEvent(String eventId, ICallback callback) {

        dbRef.child("/server/events/" + eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(task.isSuccessful() && task.getResult().getValue() != null)
                {
                        Event event = task.getResult().getValue(Event.class);
                        eventsViewModel.liveEvent.setValue(event);
                }
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    public void loadEvents(ICallback callback)
    {
        dbRef.child("/server/events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(task.isSuccessful())
                {
                    List<Event> eventList = new ArrayList<Event>();
                    for(DataSnapshot snapshot : task.getResult().getChildren())
                    {
                        if(!snapshot.getKey().equals("count"))
                        {
                            Event event = snapshot.getValue(Event.class);
                            eventList.add(event);
                        }
                    }
                    eventsViewModel.liveEventList.setValue(eventList);
                }
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    public void leaveEvent(Event event, ICallback callback)
    {
        String username = accountViewModel.liveUser.getValue().username;

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/server/events/" + event.id + "/joinedUsers/" + username,  null);
        childUpdates.put("/server/users/" + username + "/joinedEvents/" + event.id, null);

        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if (task.isSuccessful()) {

                    // remove event from created events list
                    accountViewModel.liveUser.getValue().joinedEventIds.remove(event.id);

                    if(event.joinedUsers != null)
                        event.joinedUsers.remove(username);

                }
                callback.onCallback(task.isSuccessful());

            }
        });
    }

    public void joinEvent(Event event, ICallback callback)
    {
        String username = accountViewModel.liveUser.getValue().username;

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/server/events/" + event.id + "/joinedUsers/" + username,  "0");
        childUpdates.put("/server/users/" + username + "/joinedEvents/" + event.id, "0");

        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if (task.isSuccessful()) {

                    if (accountViewModel.liveUser.getValue().joinedEventIds == null)
                        accountViewModel.liveUser.getValue().joinedEventIds = new ArrayList<String>();

                    accountViewModel.liveUser.getValue().joinedEventIds.add(event.id);

                    if (event.joinedUsers == null)
                        event.joinedUsers = new HashMap<String, String>();
                    event.joinedUsers.put(username, "0");

                }
                callback.onCallback(task.isSuccessful());

            }
        });
    }

    public void deleteEvent(Event event, ICallback callback) {

        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/server/events/" + event.id,  null);
        childUpdates.put("/server/users/" + accountViewModel.liveUser.getValue().username + "/createdEvents/" + event.id, null);
        dbRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if (task.isSuccessful()) {

                    accountViewModel.liveUser.getValue().createdEventIds.remove(event.id);
                    eventsViewModel.liveEventList.getValue().remove(event);

                }
                callback.onCallback(task.isSuccessful());

            }
        });
    }

}
