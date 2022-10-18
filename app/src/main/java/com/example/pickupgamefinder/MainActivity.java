package com.example.pickupgamefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.pickupgamefinder.ui.main.AccountFragment;
import com.example.pickupgamefinder.ui.main.CreateEventFragment;
import com.example.pickupgamefinder.ui.main.EventListFragment;
import com.example.pickupgamefinder.ui.main.LoginFragment;
import com.example.pickupgamefinder.ui.main.MainViewModel;
import com.example.pickupgamefinder.ui.main.MapFragment;
import com.example.pickupgamefinder.ui.main.SignupFragment;
import com.example.pickupgamefinder.ui.main.WelcomeScreenFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements LifecycleObserver, NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, WelcomeScreenFragment.newInstance())
                    .commitNow();
        }

        model  = new ViewModelProvider(this).get(MainViewModel.class);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void addFragment(Fragment fragment, String fragmentTag)
    {
        if(fragmentTag.equals("MapFragment") || fragmentTag.equals("AccountFragment")
    || fragmentTag.equals("CreateEventFragment") || fragmentTag.equals("EventListFragment"))
        {
            getSupportActionBar().show();
        }
        else
        {
            getSupportActionBar().hide();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_account)
        {
            addFragment(new AccountFragment().newInstance(), "AccountFragment");
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if(id == R.id.menu_map)
        {
            addFragment(new MapFragment().newInstance(model.username), "MapFragment");
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if(id == R.id.menu_event_list)
        {
            addFragment(new EventListFragment().newInstance(), "EventListFragment");
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if(id == R.id.menu_create_event)
        {
            addFragment(new CreateEventFragment().newInstance(), "CreateEventFragment");
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if(id == R.id.menu_signout)
        {
            //clear backstack
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); i++)
            {
                fm.popBackStack();
            }

            addFragment(new WelcomeScreenFragment().newInstance(), "WelcomeScreenFragment");
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
/*
    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestory() {
        super.onDestory();
    }
*/

}
