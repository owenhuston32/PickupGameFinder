package com.example.pickupgamefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickupgamefinder.Models.Message;
import com.example.pickupgamefinder.Singletons.NavigationController;

import java.util.List;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MyViewHolder>  {

    private List<Message> messageList;

    public MessageRecyclerAdapter(List<Message> messageList)
    {
        this.messageList = messageList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView sender;
        private TextView message;

        public MyViewHolder(final View view)
        {
            super(view);

            sender = view.findViewById(R.id.chat_item_sender);
            message = view.findViewById(R.id.chat_item_message);
        }
    }


    @NonNull
    @Override
    public MessageRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerAdapter.MyViewHolder holder, int position) {

        Message message = messageList.get(position);

        holder.sender.setText(message.creator);
        holder.message.setText(message.messageText);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
