package com.example.pickupgamefinder.ui.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.R;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.example.pickupgamefinder.ViewModels.EventsViewModel;

public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private static final String TAG = "MAP_FRAGMENT";
    private static final String EVENT_LIST_KEY = "EVENT_LIST";
    private static final String CAN_DRAG_MARKER_KEY = "CAN_DRAG_MARKER";
    private Handler locationTrackerHandler;
    private Runnable locationTrackerRunnable;
    private final int getLocationWaitTime = 100000;

    private AccountViewModel accountViewModel;
    private EventsViewModel mEventsViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Activity mainActivity;
    private List<Event> eventList;
    private LatLng eventLocation;
    private boolean canDragMarker;
    private Button createEventButton;
    private CircleOptions userLocationCircle;
    private Boolean isTrackingUserLocation = false;

    public MapFragment() { }

    public MapFragment newInstance(List<Event> eventList, boolean canDragMarker)
    {
        MapFragment mapFragment = new MapFragment();

        Bundle args = new Bundle();
        args.putSerializable(EVENT_LIST_KEY, (Serializable) eventList);
        args.putBoolean(CAN_DRAG_MARKER_KEY, canDragMarker);
        mapFragment.setArguments(args);

        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        locationTrackerHandler = new Handler();
        super.onCreate(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        mEventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        final Observer<Boolean> locationPermissionObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newBool) {
                if(newBool != null && newBool)
                    permissionResultCallback(true);
            }
        };

        accountViewModel.getLiveHasLocationAccess().observe(this, locationPermissionObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle args = getArguments();

        if(args != null)
        {
            eventList = (List<Event>) args.getSerializable(EVENT_LIST_KEY);
            canDragMarker = args.getBoolean(CAN_DRAG_MARKER_KEY);
        }

        isTrackingUserLocation = false;
        mainActivity = requireActivity();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);

        createEventButton = (Button)v.findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(this);

        if(canDragMarker)
            createEventButton.setVisibility(View.VISIBLE);
        else
            createEventButton.setVisibility(View.GONE);


        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                initializeMap(googleMap);

                ((MainActivity)mainActivity).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        new ICallback() {
                            @Override
                            public void onCallback(boolean result) {
                                permissionResultCallback(result);
                            }
                        });
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        if(isTrackingUserLocation)
            startLocationTrackingThread();
        super.onResume();
    }

    @Override
    public void onStop() {

        if(locationTrackerRunnable != null)
            locationTrackerHandler.removeCallbacks(locationTrackerRunnable);

        super.onStop();
    }

    public void permissionResultCallback(boolean result)
    {
        if(result)
        {
            getUserLocation(true);
        }

    }
    public void initializeMap(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.googleMap.setOnInfoWindowClickListener(this);
    }

    private void startLocationTrackingThread()
    {
        locationTrackerRunnable = new Runnable() {
            public void run() {
                if(googleMap != null)
                {
                    getUserLocation(false);
                }
                locationTrackerHandler.postDelayed(this, getLocationWaitTime);
            }
        };
        locationTrackerHandler.postDelayed(locationTrackerRunnable, getLocationWaitTime);
    }

    private void AddMarkers(List<Event> eventList)
    {
        Log.d(TAG, "add marker");
        if(googleMap != null && eventList != null)
        {
            for (Event e : eventList) {
                MarkerOptions markerOptions = new MarkerOptions();

                if(canDragMarker)
                {
                    markerOptions = new MarkerOptions().position(eventLocation)
                            .title("DRAG THIS MARKER TO EVENT LOCATION")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }
                else if(accountViewModel.liveUser != null && accountViewModel.liveUser.getValue() != null)
                {
                    User user = accountViewModel.liveUser.getValue();
                    if(user.joinedEventIds != null && user.joinedEventIds.contains(e.id))
                    {
                        markerOptions = new MarkerOptions().position(new LatLng(e.latitude, e.longitude))
                                .title(e.eventName)
                                .snippet(e.caption)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    else if(user.createdEventIds != null && user.createdEventIds.contains(e.id))
                    {
                        markerOptions = new MarkerOptions().position(new LatLng(e.latitude, e.longitude))
                                .title(e.eventName)
                                .snippet(e.caption)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                    else
                    {
                        markerOptions = new MarkerOptions()
                                .position(new LatLng(e.latitude, e.longitude))
                                .title(e.eventName)
                                .snippet(e.caption);
                    }
                }
                else
                {
                    markerOptions = new MarkerOptions()
                            .position(new LatLng(e.latitude, e.longitude))
                            .title(e.eventName)
                            .snippet(e.caption);
                }
                Marker marker = googleMap.addMarker(markerOptions);
                marker.setTag(e.id);
                Log.d(TAG, e.id);
            }
            if(canDragMarker)
                InitializeMarkerDrag();
        }
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

    private void getUserLocation(Boolean shouldMoveCamera) {

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

            eventLocation = new LatLng(task.getResult().getLatitude(),
                    task.getResult().getLongitude());

            if(googleMap != null)
            {
                setUserLocationCircle(task.getResult());
                updateCamera(shouldMoveCamera);
            }
        }
        // start a thread to update user location every few seconds
        if(!isTrackingUserLocation) {
            isTrackingUserLocation = true;
            startLocationTrackingThread();
            AddMarkers(eventList);
        }
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
        Event event = eventList.get(0);
        event.latitude = eventLocation.latitude;
        event.longitude = eventLocation.longitude;
        mEventsViewModel.addEvent(event, new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if(result)
                {
                    ((MainActivity) mainActivity).addFragment(new EventPageFragment().newInstance(event), "EventPageFragment");
                }
                else
                {
                    Log.e(TAG, "Failed to create event");
                }
            }
        });
    }

    private void updateCamera(boolean shouldMoveCamera)
    {
        if(shouldMoveCamera)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocationCircle.getCenter(), 13));
    }

    private void setUserLocationCircle(Location location)
    {
        Log.d(TAG, "set user location circle");
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        userLocationCircle = new CircleOptions()
                .center(latLng)
                .radius(100)
                .strokeColor(Color.RED)
                .strokeWidth(5)
                .fillColor(Color.BLUE);

        googleMap.addCircle(userLocationCircle);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        if(!marker.isDraggable())
        {
            String eventId = Objects.requireNonNull(marker.getTag()).toString();
            NavigationController.getInstance().goToEventPage(eventId);
        }
    }

}