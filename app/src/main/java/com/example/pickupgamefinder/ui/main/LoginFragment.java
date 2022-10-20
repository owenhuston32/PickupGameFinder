package com.example.pickupgamefinder.ui.main;

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

import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference firebaseDB = database.getReference("Users");

            firebaseDB.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                    //    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        Log.d("firebase", String.valueOf(task.getResult().child("password").getValue()));


                        if  (password.equals(String.valueOf(task.getResult().child("password").getValue())))  {
                            mViewModel.username = mUsernameField.getText().toString();
                            mLoginButton.setText("Logged In");
                            ((MainActivity)getActivity()).addFragment(((MapFragment) new MapFragment()).newInstance(username), "MapFragment");

                        }
                    }
                }
            });





//
//            if(users.containsKey(username) && users.get(username).equals(password))
//            {
//                mViewModel.username = mUsernameField.getText().toString();
//                mLoginButton.setText("Logged In");
//                ((MainActivity)getActivity()).addFragment(((MapFragment) new MapFragment()).newInstance(username), "MapFragment");
//            }
//            else
//            {
//                mErrorMessage.setText("Invalid Username or Password");
//            }
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