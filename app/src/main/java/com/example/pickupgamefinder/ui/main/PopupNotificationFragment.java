package com.example.pickupgamefinder.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;

public class PopupNotificationFragment extends Fragment implements View.OnClickListener {

    private FrameLayout container;
    private Button okButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_popup_notification, container, false);

        this.container = (FrameLayout) container;

        container.setVisibility(View.GONE);

        okButton = v.findViewById(R.id.popup_Ok_Button);

        okButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        if(viewId == okButton.getId())
        {
            container.setVisibility(View.GONE);
        }
    }

    public void showPopup()
    {
        container.setVisibility(View.VISIBLE);
    }
}