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

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;

public class EventPageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "EVENT_PAGE_FRAGMENT";
    private static final String EVENT_KEY = "EVENT";
    private Activity activity;
    private TextView eventDetailsTV;
    private TextView currentPlayerTV;
    private Button joinEvent;
    private Button leaveEvent;
    private Button deleteEvent;
    private Button groupChatButton;
    private Button viewMapButton;

    private EventsViewModel eventsViewModel;
    private Event event;
    private AccountViewModel accountViewModel;

    public EventPageFragment() { }

    public EventPageFragment newInstance(Event event){
        EventPageFragment eventPageFragment = new EventPageFragment();

        Bundle args = new Bundle();
        args.putSerializable(EVENT_KEY, event);
        eventPageFragment.setArguments(args);

        return eventPageFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_page, container, false);

        Bundle args = getArguments();

        if(args != null)
        {
            event = (Event)args.getSerializable(EVENT_KEY);
        }

        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        eventDetailsTV = v.findViewById(R.id.event_page_event_details);
        eventDetailsTV.setText("Title: " + event.eventName + "\n" + "caption: " + event.caption
         + "\nSkill Level: " + event.skillLevel + "/10\n");

        currentPlayerTV = v.findViewById(R.id.event_page_player_count);

        int playerCount = event.joinedUsers != null ? event.joinedUsers.size() : 0;

        currentPlayerTV.setText("Players: " + playerCount + "\\" + event.maxPlayers);

        joinEvent = v.findViewById(R.id.event_page_join);
        leaveEvent = v.findViewById(R.id.event_page_leave);
        deleteEvent = v.findViewById(R.id.event_page_delete);
        groupChatButton = v.findViewById(R.id.event_page_group_chat);
        viewMapButton = v.findViewById(R.id.event_page_event_map);

        activity = requireActivity();

        InitializeUI();

        joinEvent.setOnClickListener(this);
        leaveEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        groupChatButton.setOnClickListener(this);
        viewMapButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    public void InitializeUI()
    {
        List<String> createdEventNames = Objects.requireNonNull(accountViewModel.getLiveUser().getValue()).getCreatedEventIds();
        List<String> joinedEventNames = accountViewModel.getLiveUser().getValue().getJoinedEventIds();

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
            joinEvent();
        }
        else if(id == leaveEvent.getId())
        {
            leaveEvent();
        }
        else if(id == deleteEvent.getId())
        {
            deleteEvent();
        }
        else if(id == viewMapButton.getId())
        {
            NavigationController.getInstance().gotoSingleEventMap(event, false);
        }
        else if(id == groupChatButton.getId())
        {
            NavigationController.getInstance().gotoGroupChat(event.id);
        }
    }
    private void joinEvent()
    {
        eventsViewModel.joinEvent(event,
                new ICallback() {
                    @Override
                    public void onCallback(boolean result) {
                        if(result)
                        {
                            joinEvent.setVisibility(View.GONE);
                            leaveEvent.setVisibility(View.VISIBLE);
                            currentPlayerTV.setText("");
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to delete event");
                        }
                    }
                });
    }
    private void leaveEvent()
    {
        eventsViewModel.leaveEvent(event,
                new ICallback() {
                    @Override
                    public void onCallback(boolean result) {
                        if(result)
                        {
                            joinEvent.setVisibility(View.VISIBLE);
                            leaveEvent.setVisibility(View.GONE);
                            currentPlayerTV.setText("");
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to delete event");
                        }
                    }
                });
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