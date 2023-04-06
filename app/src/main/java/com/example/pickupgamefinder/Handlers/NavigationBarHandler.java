package com.example.pickupgamefinder.Handlers;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.SignInActivity;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.google.android.material.navigation.NavigationView;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class NavigationBarHandler implements NavigationView.OnNavigationItemSelectedListener {

    AccountViewModel accountViewModel;
    EventsViewModel eventsViewModel;
    MainActivity mainActivity;
    DrawerLayout drawerLayout;
    androidx.appcompat.app.ActionBar actionBar;
    NavigationView navigationView;

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

        navigationView = mainActivity.findViewById(R.id.default_navigation_view);
        activateSignedOutDrawer();

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

    public void activateSignedInDrawer()
    {
        navigationView.getMenu().setGroupVisible(R.id.signed_in_menu_group, true);
        navigationView.getMenu().setGroupVisible(R.id.signed_out_menu_group, false);
    }

    public void activateSignedOutDrawer()
    {
        navigationView.getMenu().setGroupVisible(R.id.signed_in_menu_group, false);
        navigationView.getMenu().setGroupVisible(R.id.signed_out_menu_group, true);
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
        } else if (id == R.id.menu_sign_out) {
            menuItemClick("Map");
            activateSignedOutDrawer();
            signOutButtonClick();
            return true;
        } else if (id == R.id.menu_sign_in)
        {
            menuItemClick("");
            mainActivity.launchSignIn();
        }
        return false;
    }

    private void accountButtonClick()
    {
        NavigationController.getInstance().goToAccountFrag();
    }
    private void createdEventsButtonClick()
    {
        NavigationController.getInstance().goToCreatedEvents();
    }
    private void joinedEventsButtonClick()
    {
        NavigationController.getInstance().goToJoinedEvents();
    }
    private void mapButtonClick()
    {
        NavigationController.getInstance().goToMap();
    }
    private void viewEventsButtonClick()
    {
        NavigationController.getInstance().goToAllEventsList();
    }
    private void createEventButtonClick()
    {
        NavigationController.getInstance().goToCreateEvent();
    }
    private void signOutButtonClick()
    {
        NavigationController.getInstance().signOut();
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
