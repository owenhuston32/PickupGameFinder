package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;

public class SignupFragment extends Fragment implements View.OnClickListener{

    private PasswordHandler passwordHandler;
    private AccountViewModel mViewModel;
    private TextView mErrorMessage;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private Button mSignUpButton;
    private Activity activity;

    public SignupFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        activity = requireActivity();

        mErrorMessage = (TextView) v.findViewById(R.id.signup_errorMessage);
        mUsernameField = (EditText) v.findViewById(R.id.signup_username);
        mPasswordField = (EditText) v.findViewById(R.id.signup_password);
        mConfirmPasswordField = (EditText) v.findViewById(R.id.signup_password_confirm);
        mSignUpButton = (Button) v.findViewById(R.id.signup_create_account);

        mViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        setTextChangedListener(mPasswordField);
        setTextChangedListener(mConfirmPasswordField);

        passwordHandler = new PasswordHandler();

        mSignUpButton.setOnClickListener(this);

        return v;
    }

    public void setTextChangedListener(EditText editText)
    {
        int textId = editText.getId();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordHandler.isPasswordValid(mPasswordField.getText().toString()
                        ,mConfirmPasswordField.getText().toString()
                        ,mErrorMessage);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        if(viewId == mSignUpButton.getId())
        {

            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();
            String passwordConfirm = mConfirmPasswordField.getText().toString();

            if(passwordHandler.isPasswordValid(password, passwordConfirm, mErrorMessage))
            {
                trySignup(username, password);
            }
        }
    }

    private void trySignup(String username, String password)
    {
        mViewModel.getUserName(username, new ICallback()
        {
            // if we fail to get user that means the username is available
            @Override
            public void onCallback(boolean result) {

                if(result)
                {
                    mErrorMessage.setTextColor(Color.RED);
                    mErrorMessage.setText("Username Not Available");
                }
                else
                {
                    signUp(username, password);
                }
            }
        });
    }

    private void signUp(String username, String password)
    {
        String hashedPassword = passwordHandler.getHashedPassword(password);

        mViewModel.addUser(username, hashedPassword, new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    ((MainActivity)activity).addFragment(new MapFragment(), "MapFragment");
                }
                else
                {
                    Log.e("SignupFragment", "Failed to add user to database");
                }
            }
        });
    }
}