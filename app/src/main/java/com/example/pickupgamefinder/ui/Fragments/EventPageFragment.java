package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.R;

import java.util.List;

import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class EventPageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "EVENT_PAGE_FRAGMENT";
    private static final String EVENT_KEY = "EVENT";
    private User user;
    private Activity activity;
    private TextView eventDetailsTV;
    private TextView currentPlayerTV;
    private Button loginButton;
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

        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);


        initializeUserObserver();
        initializeEventObserver();
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
        activity = requireActivity();

        initializeViews(v);
        InitializeUI();
        initializeText();
        initializeOnClickListeners();

        // Inflate the layout for this fragment
        return v;
    }

    private void initializeUserObserver()
    {
        Fragment fragment = this;

        final Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User newUser) {
                user = newUser;

                if(fragment.getView() != null)
                    InitializeUI();
            }
        };

        accountViewModel.getLiveUser().observe(this, userObserver);

    }

    private void initializeEventObserver()
    {
        Fragment fragment = this;

        final Observer<Event> eventObserver = new Observer<Event>() {
            @Override
            public void onChanged(@Nullable final Event newEvent) {
                event = newEvent;

                if(fragment.getView() != null)
                    InitializeUI();
            }
        };

        eventsViewModel.getLiveEvent().observe(this, eventObserver);
    }

    private void initializeText()
    {
        eventDetailsTV.setText("Title: " + event.eventName + "\n" + "caption: " + event.caption
                + "\nSkill Level: " + event.skillLevel + "/10\n");

        int playerCount = event.joinedUsers != null ? event.joinedUsers.size() : 0;

        currentPlayerTV.setText("Players: " + playerCount + "\\" + event.maxPlayers);

    }

    private void initializeViews(View v)
    {
        eventDetailsTV = v.findViewById(R.id.event_page_event_details);
        currentPlayerTV = v.findViewById(R.id.event_page_player_count);
        joinEvent = v.findViewById(R.id.event_page_join);
        leaveEvent = v.findViewById(R.id.event_page_leave);
        deleteEvent = v.findViewById(R.id.event_page_delete);
        groupChatButton = v.findViewById(R.id.event_page_group_chat);
        viewMapButton = v.findViewById(R.id.event_page_event_map);
        loginButton = v.findViewById(R.id.event_page_login);
    }

    private void initializeOnClickListeners()
    {

        joinEvent.setOnClickListener(this);
        leaveEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        groupChatButton.setOnClickListener(this);
        viewMapButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void InitializeUI()
    {
        if(event == null)
        {
            initializeEventDeletedUI();
        }
        else if(user != null)
        {
            initializeSignedInUI(user);
        }
        else
        {
            initializeSignedOutUI();
        }
    }

    private void initializeSignedInUI(User user)
    {
        List<String> createdEventNames = user.getCreatedEventIds();
        List<String> joinedEventNames = user.getJoinedEventIds();

        groupChatButton.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
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
    private void initializeSignedOutUI()
    {
        groupChatButton.setVisibility(View.GONE);
        leaveEvent.setVisibility(View.GONE);
        joinEvent.setVisibility(View.GONE);
        deleteEvent.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }
    private void initializeEventDeletedUI()
    {
        eventDetailsTV.setText("EVENT HAS BEEN DELETED");
        groupChatButton.setVisibility(View.GONE);
        leaveEvent.setVisibility(View.GONE);
        joinEvent.setVisibility(View.GONE);
        deleteEvent.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
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
        else if(id == loginButton.getId())
        {
            ((MainActivity)activity).launchSignIn();
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
                            initializeText();
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
                            initializeText();
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
                            initializeEventDeletedUI();
                        }
                        else
                        {
                            Log.e("Event page frag", "failed to delete event");
                        }
                    }
                });
    }
}