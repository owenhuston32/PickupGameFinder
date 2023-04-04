package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Models.Event;
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
    public void getEvent(String eventId, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.getEvent(eventId, callback);
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

    public void leaveEvent(Event event, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.leaveEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void joinEvent(Event event, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            eventRepository.joinEvent(event, callback);
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