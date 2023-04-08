package com.example.pickupgamefinder.Repositories;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.Singletons.LoadingScreen;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountRepository {

    private final AccountViewModel accountViewModel;
    private final DatabaseReference dbRef;

    public AccountRepository(AccountViewModel accountViewModel, DatabaseReference dbRef)
    {
        this.accountViewModel = accountViewModel;
        this.dbRef = dbRef;
    }

    public void addUser(String hashedID, ICallback callback) {

        User user = new User(hashedID, new ArrayList<String>(), new ArrayList<String>());
        dbRef.child("server/users/" + hashedID).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(error == null) {
                    accountViewModel.liveUser.setValue(user);
                }
                callback.onCallback(error == null);
            }
        });
    }

    public void tryLogin(String ID, ICallback callback)
    {
        dbRef.child("server/users/" + ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();
                if(task.isSuccessful() && task.getResult().getValue() != null)
                {
                    User user = task.getResult().getValue(User.class);

                    boolean success = user != null;

                    if(success)
                    {
                        if(task.getResult().getValue() != null)
                        {
                            List<String> createdIds = new ArrayList<String>();
                            for(DataSnapshot snapshot : task.getResult().child("/createdEvents").getChildren())
                            {
                                createdIds.add(snapshot.getKey());
                            }
                            user.createdEventIds = createdIds;

                            List<String> joinedIds = new ArrayList<String>();
                            for(DataSnapshot snapshot : task.getResult().child("/joinedEvents").getChildren()) {
                                joinedIds.add(snapshot.getKey());
                            }

                            user.joinedEventIds = joinedIds;
                        }

                        accountViewModel.liveUser.setValue(user);
                    }
                    callback.onCallback(success);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }

    public void getID(String hashedID, ICallback callback) {

        dbRef.child("server/users/" + hashedID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();
                callback.onCallback(task.isSuccessful() && task.getResult().getValue() != null);
            }
        });
    }

}
