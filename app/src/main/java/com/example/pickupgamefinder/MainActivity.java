package com.example.pickupgamefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.pickupgamefinder.ui.main.LoginFragment;
import com.example.pickupgamefinder.ui.main.MainViewModel;
import com.example.pickupgamefinder.ui.main.WelcomeScreenFragment;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WelcomeScreenFragment.newInstance())
                    .commitNow();
        }

        MainViewModel model  = new ViewModelProvider(this).get(MainViewModel.class);
        model.getUsers().observe(this, users -> {
            // Update  UI here
        });

    }

    public void addFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    public void onStart() {
        super.onStart();
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
