package com.example.pickupgamefinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pickupgamefinder.Handlers.NavigationBarHandler;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Repositories.EventRepository;
import com.example.pickupgamefinder.Repositories.MessageRepository;
import com.example.pickupgamefinder.Singletons.ErrorUIHandler;
import com.example.pickupgamefinder.Singletons.InternetManager;
import com.example.pickupgamefinder.Singletons.LoadingScreen;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.example.pickupgamefinder.ui.Fragments.MapFragment;
import com.example.pickupgamefinder.ui.Fragments.PermissionHandlerFragment;
import com.example.pickupgamefinder.ui.Fragments.PopupNotificationFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements  LifecycleObserver{

    private static final String TAG = "MainActivity";
    private static final String USERNAME_KEY = "USERNAME";
    private static final String ID_KEY = "ID";
    private NavigationBarHandler navigationBarHandler;
    private DrawerLayout drawerLayout;
    private AccountViewModel accountViewModel;
    private EventsViewModel eventsViewModel;
    private MessageViewModel messageViewModel;
    private PermissionHandlerFragment permissionHandlerFragment;

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            navigationBarHandler.activateSignedOutDrawer();
            String ID;
            if(result.getResultCode() == RESULT_OK && result.getData() != null && (ID = result.getData().getStringExtra(ID_KEY)) != null)
            {
                String username = result.getData().getStringExtra(USERNAME_KEY);
                accountViewModel.getID(ID, new ICallback() {
                    @Override
                    public void onCallback(boolean getIdResult) {
                        if (getIdResult) {
                            accountViewModel.tryLogin(ID, new ICallback() {
                                @Override
                                public void onCallback(boolean loginResult) {
                                    if (loginResult) {
                                        Toast.makeText(getApplicationContext(), "Welcome: " + username, Toast.LENGTH_SHORT).show();
                                        navigationBarHandler.activateSignedInDrawer();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "UNABLE TO SIGN IN", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            accountViewModel.addUser(ID, username, new ICallback() {
                                @Override
                                public void onCallback(boolean addUserResult) {
                                    if (addUserResult) {
                                        Toast.makeText(getApplicationContext(), "Welcome: " + username, Toast.LENGTH_SHORT).show();
                                        navigationBarHandler.activateSignedInDrawer();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });


    public void launchSignIn()
    {
        Intent intent = new Intent(this, SignIn.class);
        intent.putExtra("REQUEST_CODE", "SIGN_IN");
        signInLauncher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        permissionHandlerFragment = new PermissionHandlerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.permission_fragment_container, permissionHandlerFragment);
        fragmentTransaction.commit();

        if (savedInstanceState == null) {
            InternetManager.getInstance().setContext(this);
            LoadingScreen.getInstance().setContext(this);

            InitializeViewModels();
            NavigationController.getInstance().setupNavController(this, eventsViewModel, accountViewModel, messageViewModel);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationBarHandler = new NavigationBarHandler(accountViewModel, eventsViewModel, this, drawerLayout);
            createInitialMapScreen();
            createPopupFragment();
            Objects.requireNonNull(getSupportActionBar()).show();

            tryInitialSignIn();
        }
    }

    private void tryInitialSignIn()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null)
        {
            accountViewModel.tryLogin(account.getId(), new ICallback() {
                @Override
                public void onCallback(boolean result) {
                    if(result)
                    {
                        Toast.makeText(getApplicationContext(), "Welcome: " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
                        navigationBarHandler.activateSignedInDrawer();
                    }
                }
            });
        }
    }

    private void createInitialMapScreen()
    {
        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {

                List<Event> eventList;
                if(result && eventsViewModel.liveEventList != null)
                {
                    eventList = eventsViewModel.liveEventList.getValue();
                }
                else
                {
                    eventList = new ArrayList<Event>();
                    ErrorUIHandler.getInstance().showError("Error Loading Events");
                }

                Fragment mapFragment = new MapFragment().newInstance(eventList, false);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, mapFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void createPopupFragment()
    {
        PopupNotificationFragment popup = new PopupNotificationFragment();

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

        EventRepository eventRepository = new EventRepository(eventsViewModel, accountViewModel, messageViewModel, dbRef);
        AccountRepository accountRepository = new AccountRepository(accountViewModel, dbRef);
        MessageRepository messageRepository = new MessageRepository(messageViewModel, dbRef);

        accountViewModel.eventsViewModel = eventsViewModel;
        accountViewModel.accountRepository = accountRepository;

        eventsViewModel.eventRepository = eventRepository;
        eventsViewModel.accountRepository = accountRepository;

        messageViewModel.messageRepository = messageRepository;
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
            Log.d(TAG, "portrait");
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Log.d(TAG, "landscape");
        }
        else
        {
            Log.d(TAG, "other orientation " + orientation);
        }

    }

    public void addFragment(Fragment fragment, String fragmentTag) // new fragment added here
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public void requestPermission(String permission, ICallback callback)
    {
        permissionHandlerFragment.requestPermission(permission, callback);
    }

    public void onStart() {
        super.onStart();
    }

}
