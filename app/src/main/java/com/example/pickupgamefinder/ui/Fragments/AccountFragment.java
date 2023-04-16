package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private static final String USER_KEY = "USER";
    private User user;
    private Button messageButton;

    public AccountFragment() { }

    public AccountFragment newInstance(User user)
    {
        AccountFragment accountFragment = new AccountFragment();

        Bundle args = new Bundle();
        args.putSerializable(USER_KEY, user);
        accountFragment.setArguments(args);

        return accountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args != null)
        {
            user = (User) args.getSerializable("USER");
        }

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        TextView tv = v.findViewById(R.id.account_tv);
        messageButton = v.findViewById(R.id.account_message_button);
        tv.setText(user.username);

        Fragment createdEvents = new EventListFragment().newInstance(false, true, false, false);
        Fragment joinedEvents = new EventListFragment().newInstance(false, false, true, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.account_created_events_container, createdEvents).replace(R.id.account_joined_events_container, joinedEvents).commit();

        return v;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == messageButton.getId())
        {

        }
    }
}