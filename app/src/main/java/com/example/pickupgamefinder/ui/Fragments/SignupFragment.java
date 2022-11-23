package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
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
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.User;

import java.util.ArrayList;

import com.example.pickupgamefinder.ViewModels.AccountViewModel;

public class SignupFragment extends Fragment implements View.OnClickListener{

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

    public SignupFragment newInstance() {
        return new SignupFragment();
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

        setTextChangedListener(mUsernameField);
        setTextChangedListener(mPasswordField);
        setTextChangedListener(mConfirmPasswordField);
        mSignUpButton.setOnClickListener(this);

        return v;
    }


    private void setTextChangedListener(EditText editText)
    {
        int textId = editText.getId();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(textId == mConfirmPasswordField.getId() || textId == mPasswordField.getId())
                {
                    isPasswordValid(mPasswordField.getText().toString(), mConfirmPasswordField.getText().toString());
                }
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

            if(isPasswordValid(password, passwordConfirm))
            {
                if(((MainActivity) activity).checkWifi())
                {
                    trySignup(username, password);
                }
            }
        }
        else {

        }
    }

    private void trySignup(String username, String password)
    {
        mViewModel.getUser(username, new ICallback()
        {
            // if we fail to get user that means the username is available
            @Override
            public void onCallback(Object data) {

                if(data.toString().equals("success"))
                {
                    ((MainActivity) activity).hideLoadingScreen();
                    mErrorMessage.setText("Username Not Available");
                }
                else
                {
                    signUp(username, password);
                }
            }
        });
    }


    private boolean isPasswordValid(String pass1, String pass2)
    {
        boolean valid = true;
        if(pass1.equals(pass2))
        {
            mErrorMessage.setText("");
        }
        else
        {
            valid = false;
            mErrorMessage.setText("Passwords Do not match");
        }
        return valid;
    }
    private void signUp(String username, String password)
    {
        User user = new User(username, password, new ArrayList<String>(), new ArrayList<String>());
        mViewModel.addUser(user, new ICallback() {
            @Override
            public void onCallback(Object data) {
                if(data.toString().equals("success"))
                {
                    ((MainActivity) activity).hideLoadingScreen();
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