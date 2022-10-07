package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.R;

public class LoginFragment extends Fragment implements  View.OnClickListener {

    private MainViewModel mViewModel;

    private TextView mDebugText;
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


        mDebugText = (TextView) v.findViewById(R.id.debug_text);
        mUsernameField = (EditText) v.findViewById(R.id.username_input);
        mPasswordField = (EditText) v.findViewById(R.id.password_input);
        mLoginButton = (Button) v.findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        if(viewId == R.id.login_button)
        {
            mLoginButton.setText("LoginClicked");
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
        TextView textView = (TextView) getView().findViewById(R.id.debug_text);

        textView.setText("Debug: " + message);
    }

}