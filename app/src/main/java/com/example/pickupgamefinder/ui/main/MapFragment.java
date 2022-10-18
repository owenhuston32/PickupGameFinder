package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pickupgamefinder.R;

public class MapFragment extends Fragment {

    private static final String USERNAME = "username";

    private String username;
    private TextView welcomeMessage;

    public MapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String username) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logged_in, container, false);

        welcomeMessage = (TextView)  v.findViewById(R.id.loggedin_welcome_message);

        welcomeMessage.setText("Welcome " + getArguments().getString(USERNAME) + "!");

        return v;
    }
}