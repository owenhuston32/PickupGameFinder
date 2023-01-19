package com.example.pickupgamefinder.Repositories;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.GroupChat;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Models.User;
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

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    private final MainActivity mainActivity;
    private final MessageViewModel messageViewModel;
    private final DatabaseReference dbRef;

    public MessageRepository(MainActivity mainActivity, MessageViewModel messageViewModel
            , DatabaseReference dbRef)
    {
        this.mainActivity = mainActivity;
        this.messageViewModel = messageViewModel;
        this.dbRef = dbRef;
    }

    public void addGroupChat(String groupChatID, String groupChatName, String creator, ICallback callback)
    {
        dbRef.child("server").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Long chatID = currentData.child("groupChats/GC/count").getValue(Long.class);

                if(chatID == null)
                    chatID = 0L;


                currentData.child("groupChats/GC/" + chatID + "/info/id").setValue(chatID.toString());
                currentData.child("groupChats/GC/" + chatID + "/info/name").setValue(groupChatName);
                currentData.child("groupChats/GC/" + chatID + "/info/creator").setValue(creator);
                currentData.child("groupChats/GC/" + chatID + "/info/joinedUsers").setValue(new ArrayList<User>());
                currentData.child("groupChats/GC/" + chatID + "/info/mCount").setValue(0L);

                currentData.child("groupChats/GC/count").setValue(ServerValue.increment(1));

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                mainActivity.hideLoadingScreen();

                if(committed) {
                    Long id = currentData.child("groupChats/GC/count").getValue(Long.class) - 1;
                    GroupChat groupChat =  new GroupChat(id.toString(), groupChatName, creator, new ArrayList<User>(), new ArrayList<Message>());
                    messageViewModel.liveGroupChat.setValue(groupChat);
                }
                callback.onCallback(committed);
            }
        });
    }

    public void getGroupChat(String chatID, ICallback callback)
    {
        dbRef.child("server/groupChats/GC/" + chatID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();

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

        dbRef.child("server/groupChats/GC/" + chatId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Long id = currentData.child("mCount").getValue(Long.class);

                if(id == null)
                    id = 0L;

                String idString = id.toString();
                currentData.child("/messages/m" + idString).setValue(message);

                currentData.child("mCount").setValue(ServerValue.increment(1));

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@com.google.firebase.database.annotations.Nullable DatabaseError error, boolean committed, @com.google.firebase.database.annotations.Nullable DataSnapshot currentData) {
                mainActivity.hideLoadingScreen();

                if(committed)
                {
                    if(messageViewModel.liveMessageList.getValue() == null)
                        messageViewModel.liveMessageList.setValue(new ArrayList<Message>());

                    List<Message> messageList = messageViewModel.liveMessageList.getValue();
                    messageList.add(message);
                }
                callback.onCallback(committed);
            }
        });
    }

    public void loadMessages(String chatID, ICallback callback)
    {
        dbRef.child("server/groupChats/GC/" + chatID + "/messages").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                mainActivity.hideLoadingScreen();

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
