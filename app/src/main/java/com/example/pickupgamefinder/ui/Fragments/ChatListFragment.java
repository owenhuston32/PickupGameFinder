package com.example.pickupgamefinder.ui.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pickupgamefinder.ICallback;
import com.example.pickupgamefinder.MessageRecyclerAdapter;
import com.example.pickupgamefinder.Models.GroupChat;
import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.R;

import java.util.ArrayList;
import java.util.List;

import com.example.pickupgamefinder.Singletons.ErrorUIHandler;
import com.example.pickupgamefinder.ViewModels.AccountViewModel;
import com.example.pickupgamefinder.ViewModels.MessageViewModel;

public class ChatListFragment extends Fragment implements View.OnClickListener {

    private static final String GROUP_CHAT_KEY = "GROUP_CHAT";
    private Activity activity;
    private MessageViewModel messageViewModel;
    private AccountViewModel accountViewModel;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private Button sendMessageButton;
    private EditText messageET;
    private GroupChat groupChat;

    public ChatListFragment() { }
    public ChatListFragment newInstance(GroupChat groupChat) {
        ChatListFragment chatListFragment = new ChatListFragment();

        Bundle args = new Bundle();
        args.putSerializable(GROUP_CHAT_KEY, groupChat);
        chatListFragment.setArguments(args);

        return chatListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);

        Bundle args = getArguments();
        if(args != null)
        {
            groupChat = (GroupChat) args.getSerializable(GROUP_CHAT_KEY);
        }

        activity = requireActivity();

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        messageViewModel = new ViewModelProvider(requireActivity()).get(MessageViewModel.class);


        recyclerView = v.findViewById(R.id.chat_list_recyclerView);
        sendMessageButton = v.findViewById(R.id.chat_list_send_button);
        messageET = v.findViewById(R.id.chat_list_message_edit);

        loadMessages();

        sendMessageButton.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == sendMessageButton.getId())
        {
            Message message = new Message(accountViewModel.liveUser.getValue().username, messageET.getText().toString());
            messageViewModel.addMessage(groupChat.id, message, new ICallback() {
                @Override
                public void onCallback(boolean result) {
                    if(result)
                    {
                        messageET.setText("");

                        setAdapter();
                    }
                    else
                    {
                        ErrorUIHandler.getInstance().showError("error sending message");
                    }
                }
            });
        }
    }

    private void loadMessages()
    {
        messageViewModel.loadMessages(groupChat.id, new ICallback() {
            @Override
            public void onCallback(boolean result) {
                if (result) {
                    messageList = messageViewModel.liveMessageList.getValue();

                    if(messageList == null)
                        messageList = new ArrayList<Message>();

                    setAdapter();
                } else {
                    ErrorUIHandler.getInstance().showError("Error loading chat messages");
                }
            }
        });
    }

    private void setAdapter()
    {
        MessageRecyclerAdapter adapter = new MessageRecyclerAdapter(messageList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}