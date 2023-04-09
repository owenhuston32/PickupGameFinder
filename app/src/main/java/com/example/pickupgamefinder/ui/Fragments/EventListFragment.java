package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.EventRecyclerAdapter;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.R;

import java.util.Arrays;
import java.util.List;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class EventListFragment extends Fragment implements View.OnClickListener {

    Activity activity;
    Button refreshButton;
    EventsViewModel eventsViewModel;
    AccountViewModel accountViewModel;
    RecyclerView recyclerView;
    List<Event> eventList;
    boolean showAllEvents;
    boolean showCreatedEvents;
    boolean showJoinedEvents;
    boolean showRefreshButton;


    public EventListFragment() { }

    public EventListFragment newInstance(boolean showAllEvents, boolean showCreatedEvents, boolean showJoinedEvents
            , boolean showRefreshButton)
    {
        EventListFragment eventListFragment = new EventListFragment();

        Bundle args = new Bundle();
        args.putBoolean("SHOW_ALL_EVENTS", showAllEvents);
        args.putBoolean("SHOW_CREATED_EVENTS", showCreatedEvents);
        args.putBoolean("SHOW_JOINED_EVENTS", showJoinedEvents);
        args.putBoolean("SHOW_REFRESH_BUTTON", showRefreshButton);
        eventListFragment.setArguments(args);

        return eventListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_list, container, false);

        Bundle args = getArguments();

        if(args != null)
        {
            showAllEvents = args.getBoolean("SHOW_ALL_EVENTS");
            showCreatedEvents = args.getBoolean("SHOW_CREATED_EVENTS");
            showJoinedEvents = args.getBoolean("SHOW_JOINED_EVENTS");
            showRefreshButton = args.getBoolean("SHOW_REFRESH_BUTTON");
        }


        activity = requireActivity();

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        recyclerView = v.findViewById(R.id.events_list_recyclerView);
        refreshButton = v.findViewById(R.id.event_list_refresh_button);

        if(showRefreshButton)
            refreshButton.setVisibility(View.VISIBLE);
        else
            refreshButton.setVisibility(View.GONE);

        refreshButton.setOnClickListener(this);

        if(showAllEvents)
        {
            loadEvents();
        }
        if(showCreatedEvents)
        {
            eventList = accountViewModel.getEventsFromEventIds(accountViewModel.liveUser.getValue().createdEventIds);
            setAdapter();
        }
        if(showJoinedEvents)
        {
            eventList = accountViewModel.getEventsFromEventIds(accountViewModel.liveUser.getValue().joinedEventIds);
            setAdapter();
        }

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == refreshButton.getId())
        {
            refreshEvents();
        }
    }

    private void refreshEvents()
    {
        loadEvents();
    }

    private void loadEvents()
    {
        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {

                if(eventsViewModel.liveEventList.getValue() != null)
                {
                    eventList = eventsViewModel.liveEventList.getValue();
                    setAdapter();
                }
                else
                {
                    setAdapter();
                    Log.e("EventListFragment", "no events found");
                }
            }
        });
    }

    private void setAdapter()
    {
        EventRecyclerAdapter adapter = new EventRecyclerAdapter(eventList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}