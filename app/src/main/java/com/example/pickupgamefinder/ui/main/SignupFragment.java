package com.example.pickupgamefinder.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class SignupFragment extends Fragment implements View.OnClickListener/*, ICallback*/{

    private MainViewModel mViewModel;
    private TextView mErrorMessage;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private Button mSignUpButton;

    public static SignupFragment newInstance() {
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

        mErrorMessage = (TextView) v.findViewById(R.id.signup_errorMessage);
        mUsernameField = (EditText) v.findViewById(R.id.signup_username);
        mPasswordField = (EditText) v.findViewById(R.id.signup_password);
        mConfirmPasswordField = (EditText) v.findViewById(R.id.signup_password_confirm);
        mSignUpButton = (Button) v.findViewById(R.id.signup_create_account);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        int viewId = view.getId();

        if(viewId == mSignUpButton.getId())
        {
            if(isUserNameAvailable() && isPasswordValid(mPasswordField.getText().toString(), mConfirmPasswordField.getText().toString())) // makes sure your password matches in both fields
            {
                mViewModel.addUser(mUsernameField.getText().toString(),mPasswordField.getText().toString());
               // mSignUpButton.setText("Signed up");

                ((MainActivity)getActivity()).addFragment(((MapFragment) new MapFragment()).newInstance(), "MapFragment");
            }
        }
        else {

        }
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
    private boolean isUserNameAvailable()
    {
        boolean available = true;
        String username = mUsernameField.getText().toString();

        if  (mViewModel.getUser(username).getValue().username != "") {
            available = false;
            mErrorMessage.setText(username + " is not available");
        }
        else
        {
            mErrorMessage.setText("");
        }
        return available;
    }
/*
    @Override
    public void OnResponse(Object data)
    {

    }


 */
}