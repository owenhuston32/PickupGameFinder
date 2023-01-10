package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
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
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;

import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class CreateEventFragment extends Fragment implements View.OnClickListener {

    EventsViewModel mEventViewModel;
    EditText eventNameET;
    EditText captionET;

    Button skillLevelLeftArrow;
    Button skillLevelRightArrow;
    TextView skillLevelText;

    Button maxPlayersLeftArrow;
    Button maxPlayersRightArrow;
    TextView maxPlayersText;

    Activity activity;

    Button nextPageButton;

    public CreateEventFragment()
    {

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

        skillLevelLeftArrow = v.findViewById(R.id.skill_level_left_arrow);
        skillLevelRightArrow = v.findViewById(R.id.skill_level_right_arrow);
        skillLevelText = v.findViewById(R.id.skill_level_number);

        maxPlayersLeftArrow = v.findViewById(R.id.max_players_left_arrow);
        maxPlayersRightArrow = v.findViewById(R.id.max_players_right_arrow);
        maxPlayersText = v.findViewById(R.id.max_players_number);

        nextPageButton = v.findViewById(R.id.create_event_next_page);


        skillLevelLeftArrow.setOnClickListener(this);
        skillLevelRightArrow.setOnClickListener(this);
        maxPlayersLeftArrow.setOnClickListener(this);
        maxPlayersRightArrow.setOnClickListener(this);
        nextPageButton.setOnClickListener(this);

        activity = requireActivity();

        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == nextPageButton.getId())
        {
            ((MainActivity)activity).addFragment(new CreateEventMapFragment(CreateEvent()), "MapFragment");
        }
        else if(id == skillLevelLeftArrow.getId())
        {
            DecrementTextView(skillLevelText);
        }
        else if(id == skillLevelRightArrow.getId())
        {
            IncrementTextView(skillLevelText);
        }
        else if(id == maxPlayersLeftArrow.getId())
        {
            DecrementTextView(maxPlayersText);
        }
        else if(id == maxPlayersRightArrow.getId())
        {
            IncrementTextView(maxPlayersText);
        }
    }

    private void DecrementTextView(TextView textView)
    {
        int num = Integer.parseInt(textView.getText().toString());

        if(num > 0)
        {
            num -= 1;
            textView.setText("" + num);
        }
    }
    private void IncrementTextView(TextView textView)
    {
        int num = Integer.parseInt(textView.getText().toString());


        // make 10 the max skill level
        if(!textView.equals(skillLevelText) || num < 10) {
            num += 1;
            textView.setText("" + num);
        }

    }
    private Event CreateEvent()
    {
        String eventName = eventNameET.getText().toString();
        String caption = captionET.getText().toString();
        int skillLevel = Integer.parseInt(skillLevelText.getText().toString());
        int maxPlayers = Integer.parseInt(maxPlayersText.getText().toString());

        Event event = new Event("0", eventName, caption, skillLevel, 0, maxPlayers, 0, 0);

        return event;
    }
}
