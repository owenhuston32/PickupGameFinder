package com.example.pickupgamefinder.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private EventsViewModel mEventsViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
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

        mEventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if(ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            getCurrentLocation();
        }
        else
        {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;

        //add markers
        mEventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(Object data) {
                if(data != null)
                    AddMarkers((List<Event>) data);
            }
        });

        this.googleMap.setOnInfoWindowClickListener(this);
    }

    private void AddMarkers(List<Event> eventList)
    {
        for(Event e : eventList)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.latitude, e.longitude))
                    .title(e.eventName)
                    .snippet(e.caption + "\n"
                            + "skill: " + e.skillLevel + "\n"
                            + "players: " + e.currentPlayerCount + "/" + e.maxPlayers));
        }
    }


    private void getCurrentLocation()
    {
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null)
                {
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("YOU ARE HERE");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            googleMap.addMarker(options);

                        }
                    });
                }
            }
        });
    }
    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>()
                {
                    @Override
                    public void onActivityResult(Boolean result)
                    {
                        if(result)
                        {
                            getCurrentLocation();
                        }
                        else
                        {
                            // permission denied
                        }
                    }
                });

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        Log.d("TAG", "INFO WINDOW CLICK, marker title: " + marker.getTitle());
        mEventsViewModel.getEvent(marker.getTitle(), new ICallback() {
            @Override
            public void onCallback(Object data) {

                Event event = (Event)data;
                if (!event.eventName.equals(""))
                {
                    Log.d("TAG", "add fragment event page");
                    ((MainActivity)activity).addFragment( new EventPageFragment(event), "EventPageFragment");
                }
                else
                {
                    Log.e("TAG", "error finding event by name");
                    //error finding event by event name
                }
            }
        });

    }
}