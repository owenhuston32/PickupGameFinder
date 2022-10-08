package com.example.pickupgamefinder.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeScreenFragment extends Fragment implements View.OnClickListener {

    private Button mLoginButton;
    private Button mSignUpButton;
    private ViewGroup mContainer;

    public static WelcomeScreenFragment newInstance() {
        return new WelcomeScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        mContainer = container;

        mLoginButton = (Button) v.findViewById(R.id.welcome_login_button);
        mSignUpButton = (Button) v.findViewById(R.id.welcome_signup_button);

        mLoginButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        if(viewId == R.id.welcome_login_button)
        {
            ((MainActivity)getActivity()).addFragment(new LoginFragment(), "LoginFragment");
        }
        else if(viewId == R.id.welcome_signup_button)
        {
            ((MainActivity)getActivity()).addFragment(new SignupFragment(), "SignupFragment");
        }
        else {

        }
    }
}