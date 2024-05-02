package com.example.epasyaaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Events_MyAdapter extends RecyclerView.Adapter<Events_MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<EventData> list;

    public Events_MyAdapter(Context context, ArrayList<EventData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Events_MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Events_MyAdapter.MyViewHolder holder, int position) {
        EventData data = list.get(position);
        holder.textViewTitle.setText(data.getName());
        holder.textViewDescription.setText(data.getDescription());
        //for festival photos
       // Picasso.get()
                //.load(data.getPhoto())
                //.placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.error_image)
                //.into(holder.imageViewPhoto);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription;
        ImageView imageViewPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.event_name);
            imageViewPhoto = itemView.findViewById(R.id.event_pic);
            textViewDescription = itemView.findViewById(R.id.event_description);
        }
    }
}
