package com.example.pickupgamefinder.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pickupgamefinder.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        SetDebugMessage("onStart");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SetDebugMessage("onResume");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SetDebugMessage("onPause");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        SetDebugMessage("onStop");
    }

    private void SetDebugMessage(String message)
    {
        TextView textView = (TextView) getView().findViewById(R.id.debug_text);

        textView.setText("Debug: " + message);
    }

}