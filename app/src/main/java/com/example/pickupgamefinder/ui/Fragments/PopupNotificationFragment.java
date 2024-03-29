package com.example.pickupgamefinder.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.pickupgamefinder.R;

public class PopupNotificationFragment extends Fragment implements View.OnClickListener {

    private FrameLayout container;
    private Button okButton;
    private TextView errorTV;

    public PopupNotificationFragment()
    {

    }

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

        errorTV = v.findViewById(R.id.popup_text);
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

    public void showPopup(String errorMessage)
    {
        this.errorTV.setText(errorMessage);
        container.setVisibility(View.VISIBLE);
    }
}