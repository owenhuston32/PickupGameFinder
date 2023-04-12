package com.example.pickupgamefinder.ui.Fragments;

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
import android.widget.Button;

import com.example.pickupgamefinder.Models.Event;
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

import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class CreateEventMapFragment extends Fragment implements View.OnClickListener {

    private static final String EVENT_KEY = "EVENT";
    private EventsViewModel mEventViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Activity activity;
    private Button createEventButton;
    private Event event;
    private LatLng eventLocation;
    private View view;
    private Bundle savedInstanceState;
    private AccountViewModel mAccountViewModel;

    public CreateEventMapFragment() { }

    public CreateEventMapFragment newInstance(Event event)
    {
        CreateEventMapFragment createEventMapFragment = new CreateEventMapFragment();

        Bundle args = new Bundle();
        args.putSerializable(EVENT_KEY, event);
        createEventMapFragment.setArguments(args);

        return createEventMapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_event_map, container, false);

        Bundle args = getArguments();
        if(args != null)
        {
            event = args.getParcelable(EVENT_KEY);
        }

        activity = requireActivity();
        mEventViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        mAccountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        createEventButton = (Button)v.findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(this);

        this.view = v;
        this.savedInstanceState = savedInstanceState;

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

    }
    private void InitializeMapView()
    {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
    }
    private void InitializeMarkerDrag()
    {
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                eventLocation = marker.getPosition();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }
    private void getCurrentLocation()
    {
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        Log.e("GoogleMaps", "Get current location called");
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.e("GoogleMaps", "onSuccess good");
                if(location != null)
                {
                    Log.e("GoogleMaps", "location not null");
                    if(mapView == null)
                    {
                        InitializeMapView();
                    }

                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap map) {
                            eventLocation = new LatLng(location.getLatitude(),
                                    location.getLongitude());


                            MarkerOptions options = new MarkerOptions().position(eventLocation)
                                    .title("DRAG THIS MARKER TO EVENT LOCATION")
                                    .draggable(true);

                            googleMap = map;

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 13));

                            googleMap.addMarker(options);
                            Log.e("GoogleMaps", "Drag should be initialized");
                            InitializeMarkerDrag();
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
    public void onResume() {
        super.onResume();
        if(mapView == null)
            InitializeMapView();
        mapView.onResume();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == createEventButton.getId())
        {
            createEvent();
        }
    }
    private void createEvent()
    {
        event.latitude = eventLocation.latitude;
        event.longitude = eventLocation.longitude;
        mEventViewModel.addEvent(event, new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    ((MainActivity)activity).addFragment(new EventPageFragment().newInstance(event), "EventPageFragment");
                }
                else
                {
                    Log.e("TAG", "Failed to create event");
                }
            }
        });
    }
}