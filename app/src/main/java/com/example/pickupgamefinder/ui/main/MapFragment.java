package com.example.pickupgamefinder.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pickupgamefinder.IFirebaseCallback;
import com.example.pickupgamefinder.R;

public class MapFragment extends Fragment implements View.OnClickListener {

    private TextView welcomeMessage;
    private MainViewModel mViewModel;

    private EditText mUpdateField;
    private Button bUpdateButton;
    private Button bDelAccountButton;
    private Activity activity;

    public MapFragment() {
        // Required empty public constructor

    }

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        activity = requireActivity();

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        welcomeMessage = (TextView)  v.findViewById(R.id.map_welcome_message);

        Log.d("MapFrag", "map frag live user: " + mViewModel.liveUser);
        Log.d("MapFrag", "map frag live user value: " + mViewModel.liveUser.getValue());

        welcomeMessage.setText("user " + mViewModel.liveUser.getValue().username + "\npassword:" + mViewModel.liveUser.getValue().password);


        mUpdateField = (EditText) v.findViewById(R.id.update_password_confirm);
        bUpdateButton = (Button) v.findViewById(R.id.submit_new_password);
        bDelAccountButton = (Button) v.findViewById(R.id.delete_account);
        bUpdateButton.setOnClickListener(this);
        bDelAccountButton.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View view) {

        int viewId = view.getId();

        if (viewId == bUpdateButton.getId()) {
            String newPassword = mUpdateField.getText().toString();

            mViewModel.UpdatePassword(mViewModel.liveUser.getValue().username, newPassword);

            mViewModel.getUser(mViewModel.liveUser.getValue().username, new IFirebaseCallback()
            {
                @Override
                public void onCallback(User user) {
                    UpdateWelcomeMessage(user);
                }
            });

        }  else if (viewId == bDelAccountButton.getId()) {
            mViewModel.DeleteUser(mViewModel.liveUser.getValue().username);
            UpdateWelcomeMessage(null);
        }
    }
    private void UpdateWelcomeMessage(User user)
    {
        if(user == null)
        {
            welcomeMessage.setText("user deleted");
        }
        else
        {
            welcomeMessage.setText("user " + user.username + "\npassword:" + user.password);
        }
    }
}