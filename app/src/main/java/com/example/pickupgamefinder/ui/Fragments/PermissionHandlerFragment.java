package com.example.pickupgamefinder.ui.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;

import java.io.Serializable;

public class PermissionHandlerFragment extends Fragment implements  View.OnClickListener{

    private static final String TAG = "PERMISSION_HANDLER";
    private AccountViewModel accountViewModel;
    private ICallback callback;
    private Button requestLocationButton;
    private Activity mainActivity;

    public PermissionHandlerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_permission_request, container, false);

        mainActivity = requireActivity();

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        requestLocationButton = v.findViewById(R.id.map_location_permission);
        requestLocationButton.setOnClickListener(this);
        requestLocationButton.setVisibility(View.GONE);

        return v;
    }

    private ActivityResultLauncher<String> requestPermissionLauncher;
    {
        Log.d(TAG, "requestPermissionLauncher");
            requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(), isGranted ->
                    {
                        Log.d(TAG, "is permission granted: " + isGranted.toString());
                        if(isGranted)
                        {
                            requestLocationButton.setVisibility(View.GONE);
                            callback.onCallback(true);
                        }
                        else
                        {
                            requestLocationButton.setVisibility(View.VISIBLE);
                            callback.onCallback(false);
                        }
                    });

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == requestLocationButton.getId())
        {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, new ICallback() {
                @Override
                public void onCallback(boolean result) {
                    accountViewModel.liveHasLocationAccess.setValue(result);

                }
            });
        }

    }

    public void requestPermission(String permission, ICallback callback)
    {
        // if we don't already have location permission
        if(ActivityCompat.checkSelfPermission(mainActivity, permission) != PackageManager.PERMISSION_GRANTED) {

            this.callback = callback;
            requestPermissionLauncher.launch(permission);
        }
        // we already have permission
        else
        {
            requestLocationButton.setVisibility(View.GONE);
            callback.onCallback(true);
        }
    }
}
