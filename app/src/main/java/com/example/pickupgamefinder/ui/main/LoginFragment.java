package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;

import java.util.Map;

public class LoginFragment extends Fragment implements  View.OnClickListener {

    private MainViewModel mViewModel;
    private TextView mUsersText;
    private TextView mErrorMessage;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mLoginButton;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mErrorMessage = (TextView) v.findViewById(R.id.signin_errorMessage);
        mUsernameField = (EditText) v.findViewById(R.id.signin_username);
        mPasswordField = (EditText) v.findViewById(R.id.signin_password);
        mLoginButton = (Button) v.findViewById(R.id.signin_login_button);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mLoginButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        if(viewId == mLoginButton.getId())
        {
            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();
            Map<String, String> users = mViewModel.getUsers().getValue();
            if(users.containsKey(username) && users.get(username).equals(password))
            {
                mLoginButton.setText("Logged In");
                ((MainActivity)getActivity()).addFragment(((LoggedInFragment) new LoggedInFragment()).newInstance(username), "LoggedInFragment");
            }
            else
            {
                mErrorMessage.setText("Invalid Username or Password");
            }
        }
        else {

        }
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
        //mErrorMessage.setText("Debug: " + message);
    }

}