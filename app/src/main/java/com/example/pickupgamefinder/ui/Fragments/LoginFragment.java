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
import com.example.pickupgamefinder.PasswordHandler;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.User;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;

public class LoginFragment extends Fragment implements  View.OnClickListener {

    private PasswordHandler passwordHandler;
    private AccountViewModel mAccountViewModel;
    private TextView mErrorMessage;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mLoginButton;
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
        mAccountViewModel.getUser(username, new ICallback()
        {
            @Override
            public void onCallback(boolean result) {

                if(result)
                {
                    User user = mAccountViewModel.liveUser.getValue();

                    if(user.password.equals(password))
                    {
                        Login(username, password);
                    }
                    else
                    {
                        mErrorMessage.setText("Invalid Username or Password");
                    }
                }
                else
                {
                    mErrorMessage.setText("Invalid Username or Password");
                    Log.e("Login Fragment", "failed to get user from database");
                }
            }
        });

    }
    private void Login(String username, String password)
    {
        mAccountViewModel.loadUserEvents(new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    Log.d("LoginFragment", "log in click");
                    mLoginButton.setText("Logged In");
                    ((MainActivity)activity).addFragment(new MapFragment(), "MapFragment");
                }
                else
                {
                    mLoginButton.setText("Failed to load user events");
                }
            }
        });
    }
    private void moveToMapFragment()
    {

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

    }

}