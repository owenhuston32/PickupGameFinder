package com.example.pickupgamefinder.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pickupgamefinder.Event;
import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountRepository {

    private final MainActivity mainActivity;
    private final AccountViewModel accountViewModel;
    private final DatabaseReference dbRef;

    public AccountRepository(MainActivity mainActivity, AccountViewModel accountViewModel
            , DatabaseReference dbRef)
    {
        this.mainActivity = mainActivity;
        this.accountViewModel = accountViewModel;
        this.dbRef = dbRef;
    }

    public void addUser(String userName, String hashedPassword, ICallback callback) {

        User user = new User(0L, userName, hashedPassword, new ArrayList<>(), new ArrayList<>());
        dbRef.child("server/users/" + userName).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                callback.onCallback(error == null);
            }
        });


        /*
        dbRef.child("server").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                // abort transaction if someone with the same username tried signing up before transaction finishes
                if(currentData.child("users").child(userName).getValue() != null)
                    return Transaction.abort();

                // add user to database
                User user = new User(0L, userName, hashedPassword, new ArrayList<>(), new ArrayList<>());
                currentData.child("users").child(userName).setValue(user);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                mainActivity.hideLoadingScreen();

                if(committed)
                {
                    User user = currentData.child("users").child(userName).getValue(User.class);
                    accountViewModel.liveUser.setValue(user);
                    callback.onCallback(true);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
        */
    }

    public void tryLogin(String username, String hashedPassword, ICallback callback)
    {
        dbRef.child("server/users/" + username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if(task.isSuccessful() && task.getResult().getValue() != null)
                {
                    User user = task.getResult().getValue(User.class);

                    // check if passwords match
                    callback.onCallback(user != null && user.password.equals(hashedPassword));
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }

    public void getUserName(String username, ICallback callback) {

        dbRef.child("server/users/" + username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                callback.onCallback(task.isSuccessful() && task.getResult().getValue() != null);
            }
        });
    }

    public void loadUserEvents(ICallback callback) {

        dbRef.child("server/users/" + accountViewModel.liveUser.getValue().username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                if (task.isSuccessful()) {

                    User user = accountViewModel.liveUser.getValue();

                    if(task.getResult().hasChildren())
                    {
                        GenericTypeIndicator<HashMap<String, Integer>> t = new GenericTypeIndicator<HashMap<String, Integer>>() {};
                        HashMap<String, Integer> createdEventsMap = task.getResult().child("createdEvents").getValue(t);
                        HashMap<String, Integer> joinedEventsMap = task.getResult().child("joinedEvents").getValue(t);

                        user.createdEventNames = getEventNamesFromHashMap(createdEventsMap);
                        user.joinedEventNames = getEventNamesFromHashMap(joinedEventsMap);

                        accountViewModel.liveUser.setValue(user);

                        callback.onCallback(true);
                    }
                    else
                    {
                        callback.onCallback(false);
                        Log.e("Account repository", "load user event list is null");
                    }
                } else {
                    callback.onCallback(false);
                    Log.e("account repository", "Error loading user events");
                }
            }
        });
    }

    private List<String> getEventNamesFromHashMap(HashMap<String, Integer> map)
    {
        List<String> eventNames = null;

        if(map != null)
        {
            eventNames = new ArrayList<String>();
            for (String s : map.keySet()) {
                eventNames.add(s);
                Log.d("Account Repo", "created event: " + s);
            }
        }
        return eventNames;
    }

}
