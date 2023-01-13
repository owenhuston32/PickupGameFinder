package com.example.pickupgamefinder.Repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

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

        User user = new User(userName, hashedPassword, new ArrayList<>(), new ArrayList<>());
        dbRef.child("server/users/" + userName).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                mainActivity.hideLoadingScreen();

                if(error == null)
                {
                    accountViewModel.liveUser.setValue(user);
                    callback.onCallback(true);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
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

                    boolean success = user != null && user.password.equals(hashedPassword);

                    if(success)
                    {
                        List<String> createdEventIds
                                = user.createListFromSnapshotKeys(task.getResult().child("createdEvents").getChildren());

                        List<String> joinedEventIds
                                = user.createListFromSnapshotKeys(task.getResult().child("joinedEvents").getChildren());

                        user.createdEventIds = createdEventIds;
                        user.joinedEventIds = joinedEventIds;

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

    public void getUserName(String username, ICallback callback) {

        dbRef.child("server/users/" + username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();
                callback.onCallback(task.isSuccessful() && task.getResult().getValue() != null);
            }
        });
    }

    public void loadCreatedEventIds(User user, ICallback callback)
    {
        dbRef.child("server/users/" + user.username + "/createdEvents").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                boolean successful = task.isSuccessful() && task.getResult().getValue() != null;
                if(successful)
                {
                    List<String> eventIds = new ArrayList<String>();
                    for(DataSnapshot snapshot : task.getResult().getChildren())
                    {
                        eventIds.add(snapshot.getKey());
                    }
                    user.createdEventIds = eventIds;
                    accountViewModel.liveUser.setValue(user);
                }
                mainActivity.hideLoadingScreen();
                callback.onCallback(successful);
            }
        });
    }

    public void loadJoinedEvents(User user, ICallback callback)
    {
        dbRef.child("server/users/" + user.username + "/joinedEvents").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                boolean successful = task.isSuccessful() && task.getResult().getValue() != null;
                if(successful)
                {
                    List<String> eventIds = new ArrayList<String>();
                    for(DataSnapshot snapshot : task.getResult().getChildren())
                    {
                        eventIds.add(snapshot.getKey());
                    }
                    user.joinedEventIds = eventIds;
                    accountViewModel.liveUser.setValue(user);
                }
                mainActivity.hideLoadingScreen();
                callback.onCallback(successful);
            }
        });
    }

}
