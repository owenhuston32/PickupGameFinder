package com.example.pickupgamefinder.Singletons;

import android.app.Application;

import androidx.fragment.app.FragmentManager;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.example.pickupgamefinder.ui.Fragments.AccountFragment;
import com.example.pickupgamefinder.ui.Fragments.ChatListFragment;
import com.example.pickupgamefinder.ui.Fragments.CreateEventFragment;
import com.example.pickupgamefinder.ui.Fragments.EventListFragment;
import com.example.pickupgamefinder.ui.Fragments.EventPageFragment;
import com.example.pickupgamefinder.ui.Fragments.MapFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

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
    private MessageViewModel messageViewModel;

    public void setupNavController(MainActivity mainActivity, EventsViewModel eventsViewModel
            , AccountViewModel accountViewModel, MessageViewModel messageViewModel)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
        this.messageViewModel = messageViewModel;
    }


    public void goToAccountFrag()
    {
        mainActivity.addFragment(new AccountFragment().newInstance(accountViewModel.liveUser.getValue()), "AccountFragment");
    }
    public void goToMap()
    {
        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    mainActivity.addFragment(new MapFragment().newInstance(eventsViewModel.liveEventList.getValue(), false), "MapFragment");
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
        mainActivity.addFragment(new EventListFragment().newInstance(true, false, false, true), "EventListFragment");
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
                    mainActivity.addFragment( new EventPageFragment().newInstance(eventsViewModel.liveEvent.getValue()), "EventPageFragment");
                }
                else
                {
                    ErrorUIHandler.getInstance().showError("Error Finding Event");
                }
            }
        });
    }

    public void gotoSingleEventMap(Event event, boolean canDragEventMarker)
    {
        List<Event> eventList = new ArrayList<Event>();
        eventList.add(event);
        mainActivity.addFragment(new MapFragment().newInstance(eventList, canDragEventMarker), "MapFragment");
    }

    public void gotoGroupChat(String groupChatID)
    {
        messageViewModel.getGroupChat(groupChatID, new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    mainActivity.addFragment(new ChatListFragment().newInstance(messageViewModel.liveGroupChat.getValue()), "ChatListFragment");
                }
                else
                {
                    ErrorUIHandler.getInstance().showError("Error fetching group chat");
                }

            }
        });
    }

    public void signOut()
    {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(mainActivity, signInOptions);

        signInClient.signOut();

        accountViewModel.liveUser.setValue(null);

        //clear all but the starting fragment
        FragmentManager fm = mainActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }

}