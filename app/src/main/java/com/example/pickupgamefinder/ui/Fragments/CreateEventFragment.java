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

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;

import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateEventFragment extends Fragment implements View.OnClickListener {

    private EventsViewModel mEventViewModel;
    private AccountViewModel mAccountViewModel;
    private MessageViewModel messageViewModel;
    private EditText eventNameET;
    private EditText captionET;

    private Button skillLevelLeftArrow;
    private Button skillLevelRightArrow;
    private TextView skillLevelText;

    private Button maxPlayersLeftArrow;
    private Button maxPlayersRightArrow;
    private TextView maxPlayersText;

    private Activity activity;

    private Button nextPageButton;

    public CreateEventFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_event, container, false);
        mEventViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        mAccountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        messageViewModel = new ViewModelProvider(requireActivity()).get(MessageViewModel.class);

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
            Event event = CreateEvent();
            messageViewModel.addGroupChat(event.id, event.eventName + " Group", mAccountViewModel.liveUser.getValue().username,
                    new ICallback() {
                        @Override
                        public void onCallback(boolean result) {
                            if(result)
                                NavigationController.getInstance().gotoSingleEventMap(event, true);
                        }
                    });
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

        Event event = new Event("0", eventName, caption, skillLevel, maxPlayers, 0, 0
                , mAccountViewModel.liveUser.getValue().username, new HashMap<String, String>());

        return event;
    }
}
