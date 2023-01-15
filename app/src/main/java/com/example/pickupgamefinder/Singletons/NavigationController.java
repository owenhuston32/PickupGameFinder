package com.example.pickupgamefinder.Singletons;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;
import com.example.pickupgamefinder.ui.Fragments.AccountFragment;
import com.example.pickupgamefinder.ui.Fragments.CreateEventFragment;
import com.example.pickupgamefinder.ui.Fragments.EventListFragment;
import com.example.pickupgamefinder.ui.Fragments.EventPageFragment;
import com.example.pickupgamefinder.ui.Fragments.LoginFragment;
import com.example.pickupgamefinder.ui.Fragments.MapFragment;
import com.example.pickupgamefinder.ui.Fragments.SignupFragment;
import com.example.pickupgamefinder.ui.Fragments.WelcomeScreenFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationController {
    private static volatile NavigationController INSTANCE = null;
    private NavigationController() {}

    public static NavigationController getInstance() {
        if(INSTANCE == null) {
            synchronized (NavigationController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NavigationController();
                }
            }
        }
        return INSTANCE;
    }

    private MainActivity mainActivity;
    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;

    public void setupNavController(MainActivity mainActivity, EventsViewModel eventsViewModel
            , AccountViewModel accountViewModel)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
    }


    public void goToLoginFrag()
    {
        mainActivity.addFragment(new LoginFragment(), "LoginFragment");
    }
    public void goToSignUpFrag()
    {
        mainActivity.addFragment(new SignupFragment(), "SignupFragment");
    }
    public void goToAccountFrag()
    {
        mainActivity.addFragment(new AccountFragment(accountViewModel.liveUser.getValue()), "AccountFragment");
    }
    public void goToCreatedEvents()
    {
        mainActivity.addFragment(new EventListFragment(false, true, false, false), "EventListFragment");
    }
    public void goToJoinedEvents()
    {
        mainActivity.addFragment(new EventListFragment(false, false, true, false), "MapFragment");
    }
    public void goToMap()
    {
        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    mainActivity.addFragment(new MapFragment(eventsViewModel.liveEventList.getValue(), false), "MapFragment");
                }
                else
                {
                    ErrorUIHandler.getInstance().showError("Error Loading Events");
                }
            }
        });
    }
    public void goToAllEventsList()
    {
        mainActivity.addFragment(new EventListFragment(true, false, false, true), "EventListFragment");
    }
    public void goToCreateEvent()
    {
        mainActivity.addFragment(new CreateEventFragment(), "CreateEventFragment");
    }

    public void goToEventPage(String eventId)
    {
        eventsViewModel.getEvent(eventId, new ICallback() {
            @Override
            public void onCallback(boolean result) {

                if(result)
                {
                    mainActivity.addFragment( new EventPageFragment(eventsViewModel.liveEvent.getValue()), "EventPageFragment");
                }
                else
                {
                    ErrorUIHandler.getInstance().showError("Error Finding Event");
                }
            }
        });
    }

    public void signOut()
    {
        //clear backstack
        FragmentManager fm = mainActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }

        mainActivity.addFragment(new WelcomeScreenFragment(), "WelcomeScreenFragment");
        accountViewModel.liveUser.setValue(new User("", "", new ArrayList<String>(), new ArrayList<String>()));
    }

}