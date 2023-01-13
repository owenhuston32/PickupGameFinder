package com.example.pickupgamefinder;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.Models.Event;
import com.example.pickupgamefinder.Singletons.NavigationController;
import com.example.pickupgamefinder.ui.Fragments.EventPageFragment;
import com.example.pickupgamefinder.ViewModels.EventsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder>  {

    private List<Event> eventsList;

    public EventRecyclerAdapter(List<Event> eventsList)
    {
        this.eventsList = eventsList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName;
        private TextView caption;
        private TextView skillLevel;
        private TextView players;
        private Button viewEvent;

        public MyViewHolder(final View view)
        {
            super(view);

            eventName = view.findViewById(R.id.list_event_name);
            caption = view.findViewById(R.id.list_event_caption);
            skillLevel = view.findViewById(R.id.list_event_skill_level);
            players = view.findViewById(R.id.list_event_players);
            viewEvent = view.findViewById(R.id.list_event_view_event);
        }
    }


    @NonNull
    @Override
    public EventRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.MyViewHolder holder, int position) {

        Event e = eventsList.get(position);

        Integer playerCount = e.joinedUsers != null ? e.joinedUsers.size() : 0;

        String eventName = "Name: " + e.eventName;
        String caption = "Caption: " + e.caption;
        String skill = "Skill Level: " + e.skillLevel;
        String players = "Players: " + playerCount + "/" + e.maxPlayers;


        holder.eventName.setText(eventName);
        holder.caption.setText(caption);
        holder.skillLevel.setText(skill);
        holder.players.setText(players);
        holder.viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationController.getInstance().goToEventPage(e.id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
