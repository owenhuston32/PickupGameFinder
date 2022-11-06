package com.example.pickupgamefinder.ui.main;

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
import android.widget.TextView;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.EventRecyclerAdapter;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.R;

import java.util.List;

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
        eventDetailsTV.setText(event.eventName + "\n" + event.caption
         + "\n" + event.skillLevel + "/10\n");

        currentPlayerTV = v.findViewById(R.id.event_page_player_count);
        currentPlayerTV.setText(event.currentPlayerCount + "\\" + event.maxPlayers);

        joinEvent = v.findViewById(R.id.event_page_join);
        leaveEvent = v.findViewById(R.id.event_page_leave);
        deleteEvent = v.findViewById(R.id.event_page_delete);

        activity = requireActivity();

        accountViewModel.LoadUserEvents(new ICallback() {
            @Override
            public void onCallback(Object data) {
                InitializeUI((List<Event>) data);
            }
        });

        joinEvent.setOnClickListener(this);
        leaveEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    public void InitializeUI(List<Event> eventList)
    {
        if(eventList.contains(event.eventName))
        {
            leaveEvent.setVisibility(View.GONE);
            joinEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.VISIBLE);
        }
        else
        {
            leaveEvent.setVisibility(View.VISIBLE);
            joinEvent.setVisibility(View.VISIBLE);
            deleteEvent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == joinEvent.getId())
        {
            SetCurrentPlayerCount(event.currentPlayerCount + 1);
        }
        else if(id == leaveEvent.getId()) {
            SetCurrentPlayerCount(event.currentPlayerCount - 1);
        }
        else if(id == deleteEvent.getId())
        {
            DeleteEvent();
        }
    }
    private void SetCurrentPlayerCount(int newPlayercount)
    {
        eventsViewModel.SetCurrentPlayerCount(newPlayercount, event.eventName,
                new ICallback() {
                    @Override
                    public void onCallback(Object data) {
                        if(data.toString().equals("success"))
                        {
                            if(newPlayercount > event.currentPlayerCount)
                            {
                                leaveEvent.setVisibility(View.VISIBLE);
                                joinEvent.setVisibility(View.GONE);
                            }
                            else
                            {
                                leaveEvent.setVisibility(View.GONE);
                                joinEvent.setVisibility(View.VISIBLE);
                            }
                            currentPlayerTV.setText(newPlayercount + "\\" + event.maxPlayers);
                            event.currentPlayerCount = newPlayercount;
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to set current player count");
                        }
                    }
                });
    }
    private void DeleteEvent()
    {
        eventsViewModel.DeleteEvent(event,
                new ICallback() {
                    @Override
                    public void onCallback(Object data) {
                        if(data.toString().equals("success"))
                        {
                            deleteEvent.setVisibility(View.GONE);
                            eventDetailsTV.setText("EVENT HAS BEEN DELETED");
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to delete event");
                        }
                    }
                });
    }
}