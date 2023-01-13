package com.example.pickupgamefinder.Singletons;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class NavigationController {
    private static volatile NavigationController INSTANCE = null;
    private NavigationController() {}

    public static NavigationController getInstance() {
        if(INSTANCE == null) {
            synchronized (NavigationController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NavigationController();
                }
            }
        }
        return INSTANCE;
    }

    private MainActivity mainActivity;
    private EventsViewModel eventsViewModel;
    private AccountViewModel accountViewModel;

    public void setupNavController(MainActivity mainActivity, EventsViewModel eventsViewModel
            , AccountViewModel accountViewModel)
    {
        this.mainActivity = mainActivity;
        this.eventsViewModel = eventsViewModel;
        this.accountViewModel = accountViewModel;
    }


    public void goToAccountFrag()
    {

    }
    public void goToCreateEventFrag()
    {

    }
    public void goToEventListFrag()
    {

    }
    public void goToEventPageFrag()
    {

    }
    public void goToLoginFrag()
    {

    }
    public void goToMapFrag()
    {

    }
    public void goToSignUpFrag()
    {

    }
    public void goToWelcomeScreen()
    {

    }

}