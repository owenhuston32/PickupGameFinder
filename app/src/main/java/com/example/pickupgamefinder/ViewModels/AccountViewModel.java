package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountViewModel extends ViewModel {
    
    public MutableLiveData<User> liveUser = new MutableLiveData<User>();
    public AccountRepository accountRepository = null;
    public EventsViewModel eventsViewModel = null;
    public MainActivity mainActivity;

    public void addUser(String hashedID, ICallback callback) {

        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            accountRepository.addUser(hashedID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void getID(String hashedID, ICallback callback) {

        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            accountRepository.getID(hashedID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void tryLogin(String hashedID, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            accountRepository.tryLogin(hashedID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public List<Event> getEventsFromEventIds(List<String> eventIds)
    {
        List<Event> events = new ArrayList<Event>();

        List<Event> liveEventList = eventsViewModel.liveEventList.getValue();

        if(liveEventList != null && eventIds != null)
        {
            for(Event e : liveEventList)
            {
                if(eventIds.contains(e.id))
                    events.add(e);
            }
        }
        return events;
    }
}
