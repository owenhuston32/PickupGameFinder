package com.example.pickupgamefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Repositories.EventRepository;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.ui.Fragments.PopupNotificationFragment;
import com.example.pickupgamefinder.ui.Fragments.WelcomeScreenFragment;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements  LifecycleObserver{

    private NavigationBarHandler navigationBarHandler;
    private DrawerLayout drawerLayout;
    private InternetManager internetManager;
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        EventRepository eventRepository = new EventRepository(this, eventsViewModel, accountViewModel, FirebaseDatabase.getInstance(),
                FirebaseDatabase.getInstance().getReference());

        AccountRepository accountRepository = new AccountRepository(this, accountViewModel,
                FirebaseDatabase.getInstance().getReference());


        accountViewModel.eventsViewModel = eventsViewModel;
        accountViewModel.accountRepository = accountRepository;
        accountViewModel.mainActivity = this;

        eventsViewModel.eventRepository = eventRepository;
        eventsViewModel.accountRepository = accountRepository;
        eventsViewModel.mainActivity = this;
    }

    public void setActionBarTitle(String title)
    {
        navigationBarHandler.setActionBarTitle(title);
    }
    public boolean checkWifi()
    {
        // show loading screen
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

}
