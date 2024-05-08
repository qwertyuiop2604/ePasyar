package com.example.epasyaaar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OverallSearch_Adapter extends RecyclerView.Adapter<OverallSearch_Adapter.MyViewHolder> {

    private Context context;
    private List<OverallSearch_Data> list;
    private List<OverallSearch_Data> filteredList; // Add this line

    public OverallSearch_Adapter(Context context, List<OverallSearch_Data> list) {
        this.context = context;
        this.list = list;
        this.filteredList = new ArrayList<>(list); // Initialize filteredList with a copy of the full list
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_overall, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OverallSearch_Data item;
        if (filteredList != null && !filteredList.isEmpty()) {
            item = filteredList.get(position);
        } else {
            item = list.get(position);
        }
        holder.est_Name.setText(item.getName());
        if (holder.est_Photo != null) {
            Picasso.get().load(item.getPhoto()).into(holder.est_Photo);
        } else {
            Log.e("OverallSearch_Adapter", "ImageView is null");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverallSearch_Data clickedItem = item; // Use the item directly

                Class<?> destinationClass = null;
                if (clickedItem.getCategory().equals("Tourist Spot")) {
                    destinationClass = TouristSpots.class;
                } else if (clickedItem.getCategory().equals("Hotel")) {
                    destinationClass = Hotel.class;
                } else if (clickedItem.getCategory().equals("Restaurant")) {
                    destinationClass = Restaurant.class;
                } else if (clickedItem.getCategory().equals("Souvenir Shop")) {
                    destinationClass = Souvenir.class;
                }

                if (destinationClass != null) {
                    Intent intent = new Intent(v.getContext(), destinationClass);
                    intent.putExtra("documentId", clickedItem.getDocumentId()); // Use clickedItem's documentId
                    v.getContext().startActivity(intent);
                    Log.d("OverallSearch_Adapter", "Selected Document ID: " + clickedItem.getDocumentId());
                } else {
                    Log.e("OverallSearch_Adapter", "Unknown category: " + clickedItem.getCategory());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filteredList != null && !filteredList.isEmpty()) {
            return filteredList.size();
        } else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView est_Name;
        public ImageView est_Photo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            est_Name = itemView.findViewById(R.id.overall_Name);
            est_Photo = itemView.findViewById(R.id.overall_Photo);

        }
    }

    public void setFilteredList(List<OverallSearch_Data> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}