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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainViewModel extends ViewModel {

    public User user;
    public MutableLiveData<User> liveUser;

    public FirebaseDatabase database;
    public DatabaseReference dbUserRef;

    // TODO: Implement the ViewModel
    private MutableLiveData<Map<String,String>> users;

    /*
    public MutableLiveData<Map<String,String>> getUsers() {



        if (users == null) {
            users = new MutableLiveData<Map<String,String>>();
            users.setValue(new HashMap<String,String>());
            loadUsers();
        }
        return this.users;
    }*/

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public void addUser(String username, String password)
    {
        User user = new User(username,password); // Create user class for firebase DB
        dbUserRef.child(username).setValue(user);  // database
        this.user = user;

    }

    public LiveData<User> getUser(String username/*, ICallback callback */) {
        User tempUser = new User("", "");
       // tUser = new  User("", "");
        if (liveUser == null) {
            liveUser = new MutableLiveData<User>();
        }
        liveUser.setValue(tempUser);
        dbUserRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    tempUser.username = username;
                    tempUser.password =  String.valueOf(task.getResult().child("password").getValue());
                    liveUser.setValue(tempUser);


                  //  callback.OnResponse(tempUser);

                    Log.d("firebase", tempUser.password);
                    }
                }
            });
         //   return tempUser;
            return liveUser;
        }

        public LiveData<User> UpdatePassword(String username, String newPassword)
        {
            dbUserRef.child(username).child("password").setValue(newPassword);  // database
            return getUser(username);
        }

        public void DeleteUser(String username) {
            dbUserRef.child(username).removeValue();
        }
    }
