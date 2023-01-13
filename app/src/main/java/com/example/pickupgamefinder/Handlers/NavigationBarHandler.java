package com.example.pickupgamefinder.Handlers;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.ui.Fragments.AccountFragment;
import com.example.pickupgamefinder.ui.Fragments.CreateEventFragment;
import com.example.pickupgamefinder.ui.Fragments.EventListFragment;
import com.example.pickupgamefinder.ui.Fragments.MapFragment;
import com.example.pickupgamefinder.ui.Fragments.WelcomeScreenFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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
            menuItemClick("Account");
            accountButtonClick();
            return true;
        } else if (id == R.id.menu_created_events) {
            menuItemClick("Created Events");
            createdEventsButtonClick();
            return true;
        } else if (id == R.id.menu_joined_events) {
            menuItemClick("Joined Events");
            joinedEventsButtonClick();
            return true;
        } else if (id == R.id.menu_map) {
            menuItemClick("Map");
            mapButtonClick();
            return true;
        } else if (id == R.id.menu_event_list) {
            menuItemClick("Events");
            viewEventsButtonClick();
            return true;
        } else if (id == R.id.menu_create_event) {
            menuItemClick("Created Events");
            createEventButtonClick();
            return true;
        } else if (id == R.id.menu_signout) {
            menuItemClick("");
            signOutButtonClick();
            return true;
        }
        return false;
    }

    private void accountButtonClick()
    {
        mainActivity.addFragment(new AccountFragment(accountViewModel.liveUser.getValue()), "AccountFragment");
    }
    private void createdEventsButtonClick()
    {
        mainActivity.addFragment(new EventListFragment(false, true, false, false), "EventListFragment");
    }
    private void joinedEventsButtonClick()
    {
        mainActivity.addFragment(new EventListFragment(false, false, true, false), "MapFragment");
    }
    private void mapButtonClick()
    {
        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {
                mainActivity.addFragment(new MapFragment(eventsViewModel.liveEventList.getValue(), false), "MapFragment");
            }
        });
    }
    private void viewEventsButtonClick()
    {
        mainActivity.addFragment(new EventListFragment(true, false, false, true), "EventListFragment");
    }
    private void createEventButtonClick()
    {
        mainActivity.addFragment(new CreateEventFragment(), "CreateEventFragment");
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

    private void menuItemClick(String newActionBarTitle)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        setActionBarTitle(newActionBarTitle);
    }

}
