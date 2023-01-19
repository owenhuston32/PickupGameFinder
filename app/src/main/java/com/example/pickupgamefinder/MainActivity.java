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
import android.view.View;

import com.example.pickupgamefinder.Handlers.NavigationBarHandler;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Repositories.EventRepository;
import com.example.pickupgamefinder.Repositories.MessageRepository;
import com.example.pickupgamefinder.Singletons.ErrorUIHandler;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.example.pickupgamefinder.ui.Fragments.PopupNotificationFragment;
import com.example.pickupgamefinder.ui.Fragments.WelcomeScreenFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements  LifecycleObserver{

    private DrawerLayout drawerLayout;
    private InternetManager internetManager;
    private AccountViewModel accountViewModel;
    private EventsViewModel eventsViewModel;
    private MessageViewModel messageViewModel;
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
        NavigationController.getInstance().setupNavController(this, eventsViewModel, accountViewModel, messageViewModel);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        internetManager = new InternetManager(this);
        new NavigationBarHandler(accountViewModel, eventsViewModel, this, drawerLayout);
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

        ErrorUIHandler.getInstance().setPopupFragment(popup);
    }

    private void InitializeViewModels()
    {
        accountViewModel  = new ViewModelProvider(this).get(AccountViewModel.class);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        EventRepository eventRepository = new EventRepository(this, eventsViewModel, accountViewModel,
                messageViewModel, dbRef);

        AccountRepository accountRepository = new AccountRepository(this, accountViewModel,
                dbRef);

        MessageRepository messageRepository = new MessageRepository(this, messageViewModel,
                dbRef);

        accountViewModel.eventsViewModel = eventsViewModel;
        accountViewModel.accountRepository = accountRepository;
        accountViewModel.mainActivity = this;

        eventsViewModel.eventRepository = eventRepository;
        eventsViewModel.accountRepository = accountRepository;
        eventsViewModel.mainActivity = this;

        messageViewModel.messageRepository = messageRepository;
        messageViewModel.mainActivity = this;
    }

    public boolean checkWifi()
    {
        return internetManager.checkWifi();
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
