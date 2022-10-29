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
import com.example.pickupgamefinder.R;

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

        skillLevelLeftArrow = v.findViewById(R.id.skill_level_left_arrow);
        skillLevelRightArrow = v.findViewById(R.id.skill_level_right_arrow);
        skillLevelText = v.findViewById(R.id.skill_level_number);

        maxPlayersLeftArrow = v.findViewById(R.id.max_players_left_arrow);
        maxPlayersRightArrow = v.findViewById(R.id.max_players_right_arrow);
        maxPlayersText = v.findViewById(R.id.max_players_number);

        tempText = v.findViewById(R.id.temp_event_text_view);
        createEventButton = v.findViewById(R.id.create_event_button);


        skillLevelLeftArrow.setOnClickListener(this);
        skillLevelRightArrow.setOnClickListener(this);
        maxPlayersLeftArrow.setOnClickListener(this);
        maxPlayersRightArrow.setOnClickListener(this);
        createEventButton.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == createEventButton.getId())
        {
            UpdateDebugText();
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

    // used to show the values are correct when the database values are created
    private void UpdateDebugText()
    {
        String eventName = eventNameET.getText().toString();
        String caption = captionET.getText().toString();
        int skillLevel = Integer.parseInt(skillLevelText.getText().toString());
        int maxPlayers = Integer.parseInt(maxPlayersText.getText().toString());

        Event event = new Event(eventName, caption, skillLevel, 0, maxPlayers);

        mEventViewModel.addEvent(event);

        tempText.setText(eventName + "\n" + caption + "\n" + skillLevel + "\n" + maxPlayers);

    }
}