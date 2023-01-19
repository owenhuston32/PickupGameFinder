package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MainActivity;
import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.Models.GroupChat;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Models.User;
import com.example.pickupgamefinder.Repositories.AccountRepository;
import com.example.pickupgamefinder.Repositories.MessageRepository;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MessageViewModel extends ViewModel {

    public MutableLiveData<GroupChat> liveGroupChat = new MutableLiveData<GroupChat>();
    public MutableLiveData<List<Message>> liveMessageList = new MutableLiveData<List<Message>>();
    public MessageRepository messageRepository = null;
    public MainActivity mainActivity;

    public void addGroupChat(String groupChatID, String groupChatName, String creator, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            messageRepository.addGroupChat(groupChatID, groupChatName, creator, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void addMessage(String chatID, Message message, ICallback callback) {

        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            messageRepository.addMessage(chatID, message, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void getGroupChat(String chatID, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            messageRepository.getGroupChat(chatID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void loadMessages(String chatID, ICallback callback)
    {
        if(mainActivity.checkWifi())
        {
            mainActivity.showLoadingScreen();
            messageRepository.loadMessages(chatID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }
}
