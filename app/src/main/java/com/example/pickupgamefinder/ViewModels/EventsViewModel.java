package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Repositories.EventRepository;
import com.example.pickupgamefinder.Singletons.InternetManager;
import com.example.pickupgamefinder.Singletons.LoadingScreen;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    public MutableLiveData<Event> liveEvent = new MutableLiveData<Event>();
    public MutableLiveData<List<Event>> liveEventList = new MutableLiveData<List<Event>>();
    public EventRepository eventRepository = null;
    public AccountRepository accountRepository = null;

    public void addEvent(Event event, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            eventRepository.addEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }
    public MutableLiveData<Event> getLiveEvent()
    {
        if(liveEvent == null)
            liveEvent = new MutableLiveData<Event>();

        return liveEvent;

    }
    public void getEvent(String eventId, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            eventRepository.getEvent(eventId, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void loadEvents(ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            eventRepository.loadEvents(callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void leaveEvent(Event event, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            eventRepository.leaveEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void joinEvent(Event event, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            eventRepository.joinEvent(event, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void deleteEvent(Event event, ICallback callback) {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
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