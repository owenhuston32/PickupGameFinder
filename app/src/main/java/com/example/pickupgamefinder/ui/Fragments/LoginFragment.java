package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Handlers.PasswordHandler;
import com.example.pickupgamefinder.R;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class LoginFragment extends Fragment implements  View.OnClickListener {

    private PasswordHandler passwordHandler;
    private AccountViewModel mAccountViewModel;
    private TextView mErrorMessage;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private EventsViewModel eventsViewModel;
    private Activity activity;

    public LoginFragment()
    {
        Log.d("LoginFragment", "LoginFragment constructor");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        activity = requireActivity();

        mErrorMessage = (TextView) v.findViewById(R.id.signin_errorMessage);
        mUsernameField = (EditText) v.findViewById(R.id.signin_username);
        mPasswordField = (EditText) v.findViewById(R.id.signin_password);
        mLoginButton = (Button) v.findViewById(R.id.signin_login_button);

        passwordHandler = new PasswordHandler();

        mAccountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        mLoginButton.setOnClickListener(this);

        Log.d("LoginFragment", "on create view");

        return v;
    }

    @Override
    public void onClick(View view)
    {

        Log.d("LoginFragment", "on click");

        int viewId = view.getId();

        if(viewId == mLoginButton.getId()) {
            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();

            checkLoginInfo(username, password);

        }
    }
    private void checkLoginInfo(String username, String password)
    {
        String hashedPassword = passwordHandler.getHashedPassword(password);

        mAccountViewModel.tryLogin(username, hashedPassword, new ICallback()
        {
            @Override
            public void onCallback(boolean result) {

                if(result)
                {
                    Login();
                }
                else
                {
                    mErrorMessage.setText("Invalid Username or Password");
                    Log.e("Login Fragment", "failed to get user from database");
                }
            }
        });

    }
    private void Login()
    {

        eventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {
                ((MainActivity)activity).addFragment(new MapFragment(eventsViewModel.liveEventList.getValue(), false), "MapFragment");
            }
        });

    }
}