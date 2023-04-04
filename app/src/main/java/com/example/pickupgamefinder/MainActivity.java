package com.example.pickupgamefinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.pickupgamefinder.Handlers.NavigationBarHandler;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Repositories.EventRepository;
import com.example.pickupgamefinder.Repositories.MessageRepository;
import com.example.pickupgamefinder.Singletons.ErrorUIHandler;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.example.pickupgamefinder.ui.Fragments.MapFragment;
import com.example.pickupgamefinder.ui.Fragments.PopupNotificationFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements  LifecycleObserver{

    private NavigationBarHandler navigationBarHandler;
    private DrawerLayout drawerLayout;
    private InternetManager internetManager;
    private AccountViewModel accountViewModel;
    private EventsViewModel eventsViewModel;
    private MessageViewModel messageViewModel;
    private PopupNotificationFragment popup;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK)
            {
                Log.d("result code: ", "" + result.getResultCode());

                navigationBarHandler.activateSignedInDrawer();

                if(result.getData() != null)
                {
                    if(result.getData().getStringExtra(("UserName")) != null)
                    {
                        // get data and sign in here
                    }
                }
            }
            else
            {
                navigationBarHandler.activateSignedOutDrawer();
            }
        }
    });

    public void launchSignIn()
    {
        Intent intent = new Intent(this, SignInActivity.class);
        startForResult.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            InitializeViewModels();
            NavigationController.getInstance().setupNavController(this, eventsViewModel, accountViewModel, messageViewModel);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            internetManager = new InternetManager(this);
            navigationBarHandler = new NavigationBarHandler(accountViewModel, eventsViewModel, this, drawerLayout);

            showLoadingScreen();
            eventsViewModel.loadEvents(new ICallback() {
                @Override
                public void onCallback(boolean result) {
                    addFragment(new MapFragment(eventsViewModel.liveEventList.getValue(), false), "MapFragment");
                    hideLoadingScreen();
                }
            });

            createPopupFragment();
            launchSignIn();
        }
        Objects.requireNonNull(getSupportActionBar()).show();
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
                dbRef);

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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public void onStart() {
        super.onStart();
    }

}
