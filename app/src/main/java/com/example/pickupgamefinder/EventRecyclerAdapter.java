package com.example.pickupgamefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

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

        public MyViewHolder(final View view)
        {
            super(view);

            eventName = view.findViewById(R.id.list_event_name);
            caption = view.findViewById(R.id.list_event_caption);
            skillLevel = view.findViewById(R.id.list_event_skill_level);
            players = view.findViewById(R.id.list_event_players);
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

        String eventName = "Name: " + e.eventName;
        String caption = "Caption: " + e.caption;
        String skill = "Skill Level: " + e.skillLevel;
        String players = "Players: " + e.currentPlayerCount + "/" + e.maxPlayers;


        holder.eventName.setText(eventName);
        holder.caption.setText(caption);
        holder.skillLevel.setText(skill);
        holder.players.setText(players);

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
