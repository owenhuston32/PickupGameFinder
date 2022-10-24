package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.IFirebaseCallback;
import com.example.pickupgamefinder.R;

public class EventListFragment extends Fragment implements View.OnClickListener {


    TextView textView;
    Button refresh;
    EventsViewModel eventsViewModel;

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

        textView = v.findViewById(R.id.event_list_text_view);
        refresh = v.findViewById(R.id.event_list_refresh_button);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);



        refreshEvents();

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == refresh.getId())
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
                    String s = "";
                    for(Event e : eventsViewModel.liveEventList.getValue())
                    {
                        s += "name: " + e.eventName + " caption: " + e.caption +
                                " currentPlayerCount: " + e.currentPlayerCount + " maxPlayers: " + e.maxPlayers + "\n";
                    }
                    textView.setText(s);
                }
                else
                {
                    textView.setText("no events found");
                }
            }
        });
    }

}