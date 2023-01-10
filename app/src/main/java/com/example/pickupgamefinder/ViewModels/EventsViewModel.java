package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Repositories.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    public MutableLiveData<Event> liveEvent = new MutableLiveData<Event>();
    public MutableLiveData<List<Event>> liveEventList = new MutableLiveData<List<Event>>();
    public EventRepository eventRepository = null;
    public AccountRepository accountRepository = null;
    public MainActivity mainActivity;

    public void addEvent(Event event, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.addEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }
    public void getEvent(String eventName, ICallback callback)
    {
        //check if event is already in our known list of events
        if(liveEventList.getValue() != null)
        {
            for(Event e : liveEventList.getValue())
            {
                if(e.eventName.equals(eventName))
                {
                    liveEvent.setValue(e);
                    callback.onCallback(true);
                    return;
                }
            }
        }

        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            // check for event in database
            eventRepository.getEvent(eventName, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void loadEvents(ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.loadEvents(callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void setCurrentPlayerCount(int oldPlayerCount, int newCurrentPlayerCount, Event event, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.setCurrentPlayerCount(oldPlayerCount, newCurrentPlayerCount, event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void deleteEvent(Event event, ICallback callback) {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.deleteEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }
    public void addToLiveEventList(Event event)
    {
        List<Event> eventList = liveEventList.getValue();
        if(eventList == null)
            eventList = new ArrayList<Event>();

        eventList.add(event);
        liveEventList.setValue(eventList);
    }
}