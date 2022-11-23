package com.example.pickupgamefinder.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    private int getLocationWaitTime = 10000;
    private EventsViewModel mEventsViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Activity activity;
    private Button requestLocationButton;
    private CircleOptions userLocationCircle;
    private Boolean isTrackingUserLocation = false;

    public MapFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("TAG", "on create");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        activity = requireActivity();

        mEventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        Log.d("TAG", "on create view");

        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Log.d("TAG", "on view created");
        // setup permissionhandler fragment
        Fragment permissionHandler = new PermissionHandlerFragment(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_permission_fragment_container, permissionHandler).commit();

        Log.d("TAG", "permission fragment added");

        Log.d("TAG", "on view created");
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                initializeMap(googleMap);
                ((PermissionHandlerFragment)permissionHandler).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        new ICallback() {
                        @Override
                        public void onCallback(Object data) {
                            permissionResultCallback((boolean) data);
                        }
                    });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    public void permissionResultCallback(boolean result)
    {
        if(result)
        {
            getUserLocation(true);
            loadEvents();
        }

    }
    public void initializeMap(@NonNull GoogleMap googleMap) {

        Log.d("TAG", "initialize map");

        this.googleMap = googleMap;
        this.googleMap.setOnInfoWindowClickListener(this);
    }



    private void loadEvents()
    {

        mEventsViewModel.loadEvents(new ICallback() {
            @Override
            public void onCallback(Object data) {
                if(data.toString().equals("success"))
                {
                    AddMarkers(mEventsViewModel.liveEventList.getValue());
                }
                else
                {
                    AddMarkers(mEventsViewModel.liveEventList.getValue());
                    Log.e("Map Fragment", "Failed to load events");
                }
            }
        });
    }

    private void startLocationTrackingThread()
    {
        Log.d("TAG", "start location tracker");

        Handler handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                if(googleMap != null)
                {
                    // update user location but don't move
                    getUserLocation(false);
                }
                handler.postDelayed(this, getLocationWaitTime);
            }
        };
        handler.postDelayed(r, getLocationWaitTime);
    }

    private void AddMarkers(List<Event> eventList)
    {
        Log.d("TAG", "add marker");
        if(googleMap != null)
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
    }

    private void getUserLocation(Boolean shouldMoveCamera) {

        Log.d("TAG", "get user location");

        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                onGetLocationCallback(task, shouldMoveCamera);
            }
        });
    }
    private void onGetLocationCallback(Task<Location> task, boolean shouldMoveCamera)
    {
        // if location data is not null
        if (task.getResult() != null) {

            if(googleMap != null)
            {
                setUserLocationCircle(task.getResult());
                updateCamera(shouldMoveCamera);
            }
        }
        // start a thread to update user location every few seconds
        if(isTrackingUserLocation)
            startLocationTrackingThread();
    }
    private void updateCamera(boolean shouldMoveCamera)
    {
        // move camera
        if(shouldMoveCamera)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocationCircle.getCenter(), 13));
    }

    private void setUserLocationCircle(Location location)
    {
        Log.d("TAG", "set user location circle");
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        userLocationCircle = new CircleOptions()
                .center(latLng)
                .radius(100)
                .strokeColor(Color.BLUE)
                .fillColor(Color.BLUE);

        googleMap.addCircle(userLocationCircle);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        Log.d("TAG", "INFO WINDOW CLICK, marker title: " + marker.getTitle());

        if(((MainActivity) activity).checkWifi())
        {
            loadEventPage(marker);
        }
        else
        {
            ((MainActivity)activity).hideLoadingScreen();
            ((MainActivity)activity).addFragment( new EventPageFragment(mEventsViewModel.liveEvent.getValue()), "EventPageFragment");
        }

    }
    private void loadEventPage(Marker marker)
    {
        mEventsViewModel.getEvent(marker.getTitle(), new ICallback() {
            @Override
            public void onCallback(Object data) {

                if(data.toString().equals("success"))
                {
                    ((MainActivity)activity).hideLoadingScreen();
                    ((MainActivity)activity).addFragment( new EventPageFragment(mEventsViewModel.liveEvent.getValue()), "EventPageFragment");
                }
                else
                {
                    Log.e("TAG", "error finding event by name");
                }
            }
        });
    }

}