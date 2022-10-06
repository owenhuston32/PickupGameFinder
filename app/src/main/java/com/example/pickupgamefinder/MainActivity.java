package com.example.pickupgamefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.pickupgamefinder.ui.main.MainFragment;
import com.example.pickupgamefinder.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        MainViewModel model  = new ViewModelProvider(this).get(MainViewModel.class);
       // model.getUsers().observe(this, users -> {
            // Update  UI here
       // });

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
