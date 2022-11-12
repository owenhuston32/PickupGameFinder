package com.example.pickupgamefinder.ui.main;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.AccountRepository;
import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    public MutableLiveData<Event> liveEvent = new MutableLiveData<Event>();
    public MutableLiveData<List<Event>> liveEventList = new MutableLiveData<List<Event>>();
    public EventRepository eventRepository = null;
    public AccountRepository accountRepository = null;

    public void addEvent(Event event, ICallback callback)
    {
        eventRepository.addEvent(event, callback);
    }
    public void getEvent(String eventName, ICallback callback)
    {
        eventRepository.getEvent(eventName, callback);
    }

    public void loadEvents(ICallback callback)
    {
        eventRepository.loadEvents(callback);
    }

    public void setCurrentPlayerCount(int oldPlayerCount, int newCurrentPlayerCount, Event event, ICallback callback)
    {
        eventRepository.setCurrentPlayerCount(oldPlayerCount, newCurrentPlayerCount, event, callback);
    }

    public void deleteEvent(Event event, ICallback callback) {
        eventRepository.deleteEvent(event, callback);
    }
}