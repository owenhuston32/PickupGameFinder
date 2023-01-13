package com.example.pickupgamefinder.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.Models.User;

public class AccountFragment extends Fragment {

    public AccountFragment(User user) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}