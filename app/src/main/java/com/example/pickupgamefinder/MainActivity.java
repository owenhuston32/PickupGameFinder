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

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.pickupgamefinder.ui.main.AccountFragment;
import com.example.pickupgamefinder.ui.main.CreateEventFragment;
import com.example.pickupgamefinder.ui.main.EventListFragment;
import com.example.pickupgamefinder.ui.main.AccountViewModel;
import com.example.pickupgamefinder.ui.main.EventsViewModel;
import com.example.pickupgamefinder.ui.main.MapFragment;
import com.example.pickupgamefinder.ui.main.NavigationBarHandler;
import com.example.pickupgamefinder.ui.main.PopupNotificationFragment;
import com.example.pickupgamefinder.ui.main.WelcomeScreenFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  LifecycleObserver, NavigationView.OnNavigationItemSelectedListener {

    private NavigationBarHandler navigationBarHandler;
    private InternetManager internetManager;
    private DrawerLayout drawerLayout;
    private AccountViewModel accountViewModel;
    private EventsViewModel eventsViewModel;
    private PopupNotificationFragment popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            createWelcomeScreen();
            createPopupFragment();

        }
        InitializeViewModels();
        InitializeActionbar();
        internetManager = new InternetManager(this);
        navigationBarHandler = new NavigationBarHandler(accountViewModel, eventsViewModel, this, drawerLayout);
    }

    private void createWelcomeScreen() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new WelcomeScreenFragment())
                .commitNow();
    }

    private void createPopupFragment()
    {
        popup = new PopupNotificationFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_popup_container, popup)
                .commitNow();
    }

    private void InitializeViewModels()
    {
        accountViewModel  = new ViewModelProvider(this).get(AccountViewModel.class);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        EventRepository eventRepository = new EventRepository( eventsViewModel, accountViewModel, FirebaseDatabase.getInstance(),
                FirebaseDatabase.getInstance().getReference());

        AccountRepository accountRepository = new AccountRepository(eventsViewModel, accountViewModel, FirebaseDatabase.getInstance(),
                FirebaseDatabase.getInstance().getReference());

        accountViewModel.eventRepository = eventRepository;
        accountViewModel.accountRepository = accountRepository;

        eventsViewModel.eventRepository = eventRepository;
        eventsViewModel.accountRepository = accountRepository;

    }

    public boolean checkWifi()
    {
        // show loading screen and move onto callback
        if(internetManager.checkWifi())
        {
            Log.d("MainActivity", "showLoadingScreen");
            showLoadingScreen();
            return true;
        }
        // show dialog saying "not connected to internet"
        else
        {
            Log.e("MainActivity", "show no internet dialog");
            popup.showPopup();
            return false;
        }
    }

    public void showLoadingScreen()
    {
        com.saksham.customloadingdialog.LoaderKt.showDialog(this, false, R.raw.loading_animation);
    }
    public void hideLoadingScreen()
    {
        com.saksham.customloadingdialog.LoaderKt.hideDialog();
    }

    private void InitializeActionbar()
    {
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) // DrawerLayout is where  items are // navigation buttons
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Log.d("tag", "portrait");
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Log.d("tag", "landscape");
        }
        else
        {
            Log.d("tag", "other orientation " + orientation);
        }

    }

    public void addFragment(Fragment fragment, String fragmentTag) // new fragment added here
    {

        if(!fragmentTag.equals("WelcomeScreenFragment")
            && !fragmentTag.equals("LoginFragment") && !fragmentTag.equals("SignupFragment"))
        {
            getSupportActionBar().show(); // Shows toolbar
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // this is for menu icon
        return navigationBarHandler.onNavigationItemSelected(item);
    }
}
