package com.example.pickupgamefinder.ui.main;

import android.os.SystemClock;
import android.text.PrecomputedText;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.IFirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainViewModel extends ViewModel {

    public MutableLiveData<User> liveUser = new MutableLiveData<User>();

    public FirebaseDatabase database;
    public DatabaseReference dbUserRef;

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public void addUser(String username, String password)
    {
        User user = new User(username,password); // Create user class for firebase DB
        dbUserRef.child(username).setValue(user);  // database
        liveUser.setValue(user);

    }

    public void getUser(String username, IFirebaseCallback callback) {

        dbUserRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    callback.onCallback(new User("", ""));
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue() == null)
                    {
                        callback.onCallback(new User("", ""));
                        Log.d("firebase", "user not found in database");
                    }
                    else
                    {
                        callback.onCallback(new User(username, String.valueOf(task.getResult().child("password").getValue())));
                        Log.d("firebase", "successfully found user");
                    }
                }
            }
        });
        }

        public void UpdatePassword(String username, String newPassword)
        {
            dbUserRef.child(username).child("password").setValue(newPassword);
            liveUser.setValue(new User(username, newPassword));
        }

        public void DeleteUser(String username) {
            liveUser.setValue(new User("", ""));
            dbUserRef.child(username).removeValue();
        }
    }
