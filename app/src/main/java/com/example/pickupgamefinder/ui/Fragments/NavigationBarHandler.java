package com.example.pickupgamefinder.ui.Fragments;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class NavigationBarHandler implements NavigationView.OnNavigationItemSelectedListener {

    AccountViewModel accountViewModel;
    EventsViewModel eventsViewModel;
    MainActivity mainActivity;
    DrawerLayout drawerLayout;
    androidx.appcompat.app.ActionBar actionBar;

    public NavigationBarHandler(AccountViewModel accountViewModel, EventsViewModel eventsViewModel,
                                MainActivity mainActivity, DrawerLayout drawerLayout)
    {
        this.accountViewModel = accountViewModel;
        this.eventsViewModel = eventsViewModel;
        this.mainActivity = mainActivity;
        this.drawerLayout = drawerLayout;
        initializeActionbar();
    }

    private void initializeActionbar()
    {
        NavigationView navigationView = mainActivity.findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);

        actionBar = mainActivity.getSupportActionBar();

        drawerLayout = (DrawerLayout) mainActivity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainActivity, drawerLayout, toolbar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        setActionBarTitle("");
        actionBar.hide();

    }

    @Override
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
        setActionBarTitle("Account");
        mainActivity.addFragment(new AccountFragment(), "AccountFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void createdEventsButtonClick()
    {
        setActionBarTitle("Created Events");
        List<Event> createdEvents = accountViewModel.getCreatedEventList();

        mainActivity.addFragment(new EventListFragment(createdEvents, false), "EventListFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void joinedEventsButtonClick()
    {
        setActionBarTitle("Joined Events");
        List<Event> joinedEvents = accountViewModel.getJoinedEventList();

        mainActivity.addFragment(new EventListFragment(joinedEvents, false), "MapFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void mapButtonClick()
    {
        setActionBarTitle("Map");
        mainActivity.addFragment(new MapFragment(), "MapFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void viewEventsButtonClick()
    {
        setActionBarTitle("View Events");
        List<Event> eventList = eventsViewModel.liveEventList.getValue();

        if(eventList != null)
            mainActivity.addFragment(new EventListFragment(eventList, true), "EventListFragment");
        else
            mainActivity.addFragment(new EventListFragment(new ArrayList<Event>(), true), "EventListFragment");

        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void createEventButtonClick()
    {
        setActionBarTitle("Create Events");
        mainActivity.addFragment(new CreateEventFragment().newInstance(), "CreateEventFragment");
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void signOutButtonClick()
    {
        setActionBarTitle("");
        //clear backstack
        FragmentManager fm = mainActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }

        mainActivity.addFragment(new WelcomeScreenFragment(), "WelcomeScreenFragment");
        accountViewModel.liveUser.setValue(new User("", "", new ArrayList<String>(), new ArrayList<String>()));
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setActionBarTitle(String title)
    {
        actionBar.setTitle(title);
    }


}
