package com.example.pickupgamefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.Models.Chat;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Repositories.MessageRepository;
import com.example.pickupgamefinder.Singletons.InternetManager;
import com.example.pickupgamefinder.Singletons.LoadingScreen;

import java.util.List;

public class MessageViewModel extends ViewModel {

    public MutableLiveData<Chat> liveChat = new MutableLiveData<Chat>();
    public MutableLiveData<List<Message>> liveMessageList = new MutableLiveData<List<Message>>();
    public MessageRepository messageRepository = null;

    public void addMessage(String chatID, Message message, ICallback callback) {

        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            messageRepository.addMessage(chatID, message, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void getGroupChat(String chatID, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            messageRepository.getChat(chatID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }

    public void loadMessages(String chatID, ICallback callback)
    {
        if(InternetManager.getInstance().checkWifi())
        {
            LoadingScreen.getInstance().showLoadingScreen();
            messageRepository.loadMessages(chatID, callback);
        }
        else
        {
            callback.onCallback(false);
        }
    }
}
