package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.IFirebaseCallback;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.User;

public class CreateEventFragment extends Fragment implements View.OnClickListener {

    EventsViewModel mEventViewModel;
    EditText eventNameET;
    EditText captionET;
    EditText skillLevelET;
    EditText maxPlayersET;
    Button createEventButton;
    TextView tempText;


    public CreateEventFragment() {
        // Required empty public constructor
    }

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_event, container, false);

        mEventViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        eventNameET = v.findViewById(R.id.event_name_et);
        captionET = v.findViewById(R.id.caption_et);
        skillLevelET = v.findViewById(R.id.skill_level_et);
        maxPlayersET = v.findViewById(R.id.max_players);
        tempText = v.findViewById(R.id.temp_event_text_view);
        createEventButton = v.findViewById(R.id.create_event_button);


        createEventButton.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == R.id.create_event_button)
        {

            String eventName = eventNameET.getText().toString();
            String caption = captionET.getText().toString();
            int skillLevel = Integer.parseInt(skillLevelET.getText().toString());
            int maxPlayers = Integer.parseInt(maxPlayersET.getText().toString());

            Event event = new Event(eventName, caption, skillLevel, 0, maxPlayers);


            mEventViewModel.addEvent(event);

            tempText.setText(eventName + "\n" + caption + "\n" + skillLevel + "\n" + maxPlayers);


        }


    }
}