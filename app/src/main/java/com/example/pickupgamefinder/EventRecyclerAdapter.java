package com.example.pickupgamefinder;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pickupgamefinder.ui.main.EventPageFragment;
import com.example.pickupgamefinder.ui.main.EventsViewModel;
import com.example.pickupgamefinder.ui.main.MapFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder>  {

    private List<Event> eventsList;
    private Activity activity;
    private EventsViewModel eventsViewModel;

    public EventRecyclerAdapter(List<Event> eventsList, Activity activity, EventsViewModel eventsViewModel)
    {
        this.eventsList = eventsList;
        this.activity = activity;
        this.eventsViewModel = eventsViewModel;
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

        String eventName = "Name: " + e.eventName;
        String caption = "Caption: " + e.caption;
        String skill = "Skill Level: " + e.skillLevel;
        String players = "Players: " + e.currentPlayerCount + "/" + e.maxPlayers;


        holder.eventName.setText(eventName);
        holder.caption.setText(caption);
        holder.skillLevel.setText(skill);
        holder.players.setText(players);
        holder.viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsViewModel.getEvent(e.eventName, new ICallback() {
                    @Override
                    public void onCallback(Object data) {

                        if(data.toString().equals("success"))
                        {
                            ((MainActivity)activity).addFragment(new EventPageFragment(e), "EventPageFragment");
                        }
                        else
                        {
                            Log.e("EventRcyclerAdapter", "Failed to load event");
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
