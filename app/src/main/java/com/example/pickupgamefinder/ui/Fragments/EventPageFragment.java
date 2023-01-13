package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.R;

import java.util.List;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class EventPageFragment extends Fragment implements View.OnClickListener {

    Activity activity;
    TextView eventDetailsTV;
    TextView currentPlayerTV;
    Button joinEvent;
    Button leaveEvent;
    Button deleteEvent;

    EventsViewModel eventsViewModel;
    Event event;
    AccountViewModel accountViewModel;

    public EventPageFragment(Event event) {
        this.event = event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_page, container, false);

        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        eventDetailsTV = v.findViewById(R.id.event_page_event_details);
        eventDetailsTV.setText("Title: " + event.eventName + "\n" + "caption: " + event.caption
         + "\nSkill Level: " + event.skillLevel + "/10\n");

        currentPlayerTV = v.findViewById(R.id.event_page_player_count);

        Integer playerCount = event.joinedUsers != null ? event.joinedUsers.size() : 0;

        currentPlayerTV.setText("Players: " + playerCount + "\\" + event.maxPlayers);

        joinEvent = v.findViewById(R.id.event_page_join);
        leaveEvent = v.findViewById(R.id.event_page_leave);
        deleteEvent = v.findViewById(R.id.event_page_delete);

        activity = requireActivity();

        InitializeUI();

        joinEvent.setOnClickListener(this);
        leaveEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    public void InitializeUI()
    {
        List<String> createdEventNames = accountViewModel.liveUser.getValue().createdEventIds;
        List<String> joinedEventNames = accountViewModel.liveUser.getValue().joinedEventIds;

        // if the user created this event
        if(createdEventNames != null && createdEventNames.contains(event.id)) {
            leaveEvent.setVisibility(View.GONE);
            joinEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.VISIBLE);
        }
        // if the user already joined this event
        else if(joinedEventNames != null && joinedEventNames.contains(event.id))
        {
            leaveEvent.setVisibility(View.VISIBLE);
            joinEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
        }
        else
        {
            leaveEvent.setVisibility(View.GONE);
            joinEvent.setVisibility(View.VISIBLE);
            deleteEvent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == joinEvent.getId())
        {

        }
        else if(id == leaveEvent.getId()) {

        }
        else if(id == deleteEvent.getId())
        {
            deleteEvent();
        }
    }

    private void deleteEvent()
    {
        eventsViewModel.deleteEvent(event,
                new ICallback() {
                    @Override
                    public void onCallback(boolean result) {
                        if(result)
                        {
                            deleteEvent.setVisibility(View.GONE);
                            eventDetailsTV.setText("EVENT HAS BEEN DELETED");
                            currentPlayerTV.setText("");
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to delete event");
                        }
                    }
                });
    }
}