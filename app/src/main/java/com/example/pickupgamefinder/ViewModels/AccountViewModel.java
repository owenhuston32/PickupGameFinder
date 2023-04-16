package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.Singletons.InternetManager;
import com.example.pickupgamefinder.Singletons.LoadingScreen;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends ViewModel {

    public MutableLiveData<Boolean> liveHasLocationAccess = new MutableLiveData<Boolean>();
    public MutableLiveData<User> liveUser = new MutableLiveData<User>();
    public AccountRepository accountRepository = null;
    public EventsViewModel eventsViewModel = null;

    public MutableLiveData<User> getLiveUser()
    {
        if(liveUser == null)
            liveUser = new MutableLiveData<User>();

        return liveUser;
    }

    public MutableLiveData<Boolean> getLiveHasLocationAccess()
    {
        if(liveHasLocationAccess == null)
            liveHasLocationAccess = new MutableLiveData<Boolean>();

        return liveHasLocationAccess;
    }

    public void addUser(String hashedID, String username, ICallback callback) {

        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            accountRepository.addUser(hashedID, username, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void getID(String hashedID, ICallback callback) {

        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            accountRepository.getID(hashedID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void tryLogin(String hashedID, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
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
