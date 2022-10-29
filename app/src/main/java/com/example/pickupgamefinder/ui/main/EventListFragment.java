package com.example.pickupgamefinder.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.EventRecyclerAdapter;
import com.example.pickupgamefinder.IFirebaseCallback;
import com.example.pickupgamefinder.R;

public class EventListFragment extends Fragment implements View.OnClickListener {

    Activity activity;
    TextView textView;
    Button refreshButton;
    EventsViewModel eventsViewModel;
    RecyclerView recyclerView;


    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_list, container, false);

        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        recyclerView = v.findViewById(R.id.events_list_recyclerView);
        refreshButton = v.findViewById(R.id.event_list_refresh_button);

        refreshEvents();

        activity = requireActivity();

        refreshButton.setOnClickListener(this);

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
        eventsViewModel.loadEvents(new IFirebaseCallback() {
            @Override
            public void onCallback(Object data) {

                if(eventsViewModel.liveEventList.getValue() != null)
                {
                    setAdapter();
                }
                else
                {
                    textView.setText("no events found");
                }
            }
        });
    }
    private void setAdapter()
    {
        EventRecyclerAdapter adapter = new EventRecyclerAdapter(eventsViewModel.liveEventList.getValue());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}