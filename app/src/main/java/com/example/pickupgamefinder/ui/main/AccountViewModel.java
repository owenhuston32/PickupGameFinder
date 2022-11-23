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
    public AccountRepository accountRepository = null;
    public EventsViewModel eventsViewModel = null;


    public void addUser(User user, ICallback callback) {

        accountRepository.addUser(user, callback);

    }

    public void getUser(String username, ICallback callback) {
        accountRepository.getUser(username, callback);
    }

    public void loadUserEvents(ICallback callback)
    {
        accountRepository.loadUserEvents(callback);
    }

    public List<Event> getJoinedEventList()
    {
        List<Event> joinedEvents = new ArrayList<Event>();

        List<Event> liveEventList = eventsViewModel.liveEventList.getValue();

        if(liveEventList != null && liveUser.getValue().joinedEventNames != null)
        {
            for(Event e : liveEventList)
            {
                if(liveUser.getValue().joinedEventNames.contains(e.eventName))
                    joinedEvents.add(e);
            }
        }
        return joinedEvents;
    }

    public List<Event> getCreatedEventList()
    {
        List<Event> createdEvents = new ArrayList<Event>();

        List<Event> liveEventList = eventsViewModel.liveEventList.getValue();
        if(liveEventList != null && liveUser.getValue().createdEventNames != null)
        {
            for(Event e : liveEventList)
            {
                if(liveUser.getValue().createdEventNames.contains(e.eventName))
                    createdEvents.add(e);
            }
        }
        return createdEvents;
    }

}
