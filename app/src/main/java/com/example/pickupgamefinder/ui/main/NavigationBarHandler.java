package com.example.pickupgamefinder.ui.main;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.User;

import java.util.ArrayList;
import java.util.List;

public class NavigationBarHandler {

    AccountViewModel accountViewModel;
    EventsViewModel eventsViewModel;
    MainActivity mainActivity;
    DrawerLayout drawerLayout;

    public NavigationBarHandler(AccountViewModel accountViewModel, EventsViewModel eventsViewModel,
                                MainActivity mainActivity, DrawerLayout drawerLayout)
    {
        this.accountViewModel = accountViewModel;
        this.eventsViewModel = eventsViewModel;
        this.mainActivity = mainActivity;
        this.drawerLayout = drawerLayout;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // this is for menu icon
        int id = item.getItemId();

        if (id == R.id.menu_account) {
            Log.e("NaviationBarHandler", "account click");
            accountButtonClick();
            return true;
        } else if (id == R.id.menu_created_events) {
            Log.e("NaviationBarHandler", "created events: " + eventsViewModel.liveEventList.getValue());
            createdEventsButtonClick();
            return true;
        } else if (id == R.id.menu_joined_events) {
            Log.e("NaviationBarHandler", "created events: " + eventsViewModel.liveEventList.getValue());
            joinedEventsButtonClick();
            return true;
        } else if (id == R.id.menu_map) {
            mapButtonClick();
            return true;
        } else if (id == R.id.menu_event_list) {
            Log.e("NaviationBarHandler", "created events: " + eventsViewModel.liveEventList.getValue());
            viewEventsButtonClick();
            return true;
        } else if (id == R.id.menu_create_event) {
            createEventButtonClick();
            return true;
        } else if (id == R.id.menu_signout) {
            signOutButtonClick();
            return true;
        }
        return false;
    }

    private void accountButtonClick()
    {
        mainActivity.addFragment(new AccountFragment(), "AccountFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void createdEventsButtonClick()
    {
        List<Event> createdEvents = getCreatedEventList();

        mainActivity.addFragment(new EventListFragment(createdEvents, false), "EventListFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void joinedEventsButtonClick()
    {
        List<Event> joinedEvents = getJoinedEventList();

        mainActivity.addFragment(new EventListFragment(joinedEvents, false), "MapFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void mapButtonClick()
    {
        mainActivity.addFragment(new MapFragment(), "MapFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void viewEventsButtonClick()
    {
        List<Event> eventList = eventsViewModel.liveEventList.getValue();

        if(eventList != null)
            mainActivity.addFragment(new EventListFragment(eventList, true), "EventListFragment");
        else
            mainActivity.addFragment(new EventListFragment(new ArrayList<Event>(), true), "EventListFragment");

        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void createEventButtonClick()
    {
        mainActivity.addFragment(new CreateEventFragment().newInstance(), "CreateEventFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void signOutButtonClick()
    {
        //clear backstack
        FragmentManager fm = mainActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }

        mainActivity.addFragment(new WelcomeScreenFragment(), "WelcomeScreenFragment");
        accountViewModel.liveUser.setValue(new User("", "", new ArrayList<String>(), new ArrayList<String>()));
        drawerLayout.closeDrawer(GravityCompat.START);
    }




    private List<Event> getCreatedEventList()
    {
        List<Event> createdEvents = new ArrayList<Event>();

        List<Event> liveEventList = eventsViewModel.liveEventList.getValue();
        if(liveEventList != null && accountViewModel.liveUser.getValue().createdEventNames != null)
        {
            for(Event e : liveEventList)
            {
                if(accountViewModel.liveUser.getValue().createdEventNames.contains(e.eventName))
                    createdEvents.add(e);
            }
        }
        return createdEvents;
    }
    private List<Event> getJoinedEventList()
    {
        List<Event> joinedEvents = new ArrayList<Event>();

        List<Event> liveEventList = eventsViewModel.liveEventList.getValue();

        if(liveEventList != null && accountViewModel.liveUser.getValue().joinedEventNames != null)
        {
            for(Event e : liveEventList)
            {
                if(accountViewModel.liveUser.getValue().joinedEventNames.contains(e.eventName))
                    joinedEvents.add(e);
            }
        }
        return joinedEvents;
    }

}
