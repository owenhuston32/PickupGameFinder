package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private EventsViewModel eventsViewModel;
    private User user;
    private TextView tv;

    public AccountFragment() { }

    public AccountFragment newInstance(User user)
    {
        AccountFragment accountFragment = new AccountFragment();

        Bundle args = new Bundle();
        args.putSerializable("USER", user);
        accountFragment.setArguments(args);

        return accountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args != null)
        {
            user = (User) args.getSerializable("USER");
        }

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        tv = v.findViewById(R.id.account_tv);
        tv.setText(user.username);

        Fragment createdEvents = new EventListFragment().newInstance(false, true, false, false);
        Fragment joinedEvents = new EventListFragment().newInstance(false, false, true, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.account_created_events_container, createdEvents).replace(R.id.account_joined_events_container, joinedEvents).commit();

        return v;
    }
}