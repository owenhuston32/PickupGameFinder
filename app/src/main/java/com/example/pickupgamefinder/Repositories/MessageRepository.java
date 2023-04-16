package com.example.pickupgamefinder.Repositories;

import androidx.annotation.NonNull;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.GroupChat;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.Singletons.LoadingScreen;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    private final MessageViewModel messageViewModel;
    private final DatabaseReference dbRef;

    public MessageRepository(MessageViewModel messageViewModel, DatabaseReference dbRef)
    {
        this.messageViewModel = messageViewModel;
        this.dbRef = dbRef;
    }

    public void getGroupChat(String chatID, ICallback callback)
    {
        dbRef.child("server/groupChats/" + chatID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(task.isSuccessful() && task.getResult().getValue() != null) {

                    GroupChat groupChat = new GroupChat();

                    groupChat.addInfoFromSnapshot(task.getResult().child("info"));

                    messageViewModel.liveGroupChat.setValue(groupChat);
                    callback.onCallback(true);
                }
                else
                {
                    callback.onCallback(false);
                }
            }
        });
    }

    public void addMessage(String chatId, Message message, ICallback callback) {

        String messageID = dbRef.child("server/groupChats/" + chatId).push().getKey();

        dbRef.child("server/groupChats/" + chatId + "/messages/" + messageID).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(error == null) {
                    if(messageViewModel.liveMessageList.getValue() == null)
                        messageViewModel.liveMessageList.setValue(new ArrayList<Message>());

                    List<Message> messageList = messageViewModel.liveMessageList.getValue();
                    messageList.add(message);

                }
                callback.onCallback(error == null);
            }
        });

    }

    public void loadMessages(String chatID, ICallback callback)
    {
        dbRef.child("server/groupChats/" + chatID + "/messages").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoadingScreen.getInstance().hideLoadingScreen();

                if(task.isSuccessful()) {
                    List messages = new ArrayList<>();
                    if (task.getResult().getValue() != null) {
                        for(DataSnapshot snapshot : task.getResult().getChildren())
                        {
                            messages.add(snapshot.getValue(Message.class));
                        }
                    }

                    messageViewModel.liveMessageList.setValue(messages);
                }
                callback.onCallback(task.isSuccessful());
            }
        });
    }
}
