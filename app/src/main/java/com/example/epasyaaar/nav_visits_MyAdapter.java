package com.example.epasyaaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Text;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class nav_visits_MyAdapter extends RecyclerView.Adapter<nav_visits_MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<nav_visitsData> list;



    public nav_visits_MyAdapter(Context context, ArrayList<nav_visitsData> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_visits, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        nav_visitsData clickedItem = list.get(position); // Use the item directly
        holder.textViewTitle.setText(clickedItem.getName());
        Picasso.get()
                .load(clickedItem.getPhoto())
                //.placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.error_image)
                .into(holder.imageViewPhoto);

        SharedPreferences prefs = context.getSharedPreferences("ReviewStatus", Context.MODE_PRIVATE);
        boolean reviewSubmitted = prefs.getBoolean(clickedItem.getName() + "_reviewed", false); // Unique identifier for each establishment

        // Show or hide review status TextView based on reviewSubmitted flag
        if (reviewSubmitted) {
            holder.reviewStatus.setVisibility(View.VISIBLE);
            holder.btnRate.setVisibility(View.GONE);
        } else {
            holder.reviewStatus.setVisibility(View.GONE);
            holder.btnRate.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    intent.putExtra("fromNavVis", true);
                    Log.d("nav_visits_MyAdapter", "Selected Document ID: " + clickedItem.getDocumentId());
                } else {
                    Log.e("nav_visits_MyAdapter", "Unknown category: " + clickedItem.getCategory());
                }
            }
        });

        // Set OnClickListener for the button
        holder.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, navigate to UserReview class
                Intent intent = new Intent(context, UserReview.class);
                intent.putExtra("documentId", clickedItem.getDocumentId()); // Pass documentId
                intent.putExtra("establishmentName", clickedItem.getName()); // Pass establishment name
                intent.putExtra("establishmentPhotoUrl", clickedItem.getPhoto()); // Pass establishment photo URL
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewPhoto;
        Button btnRate; // Declare Button

        TextView reviewStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.rv_Name);
            imageViewPhoto = itemView.findViewById(R.id.rv_pic);
            btnRate = itemView.findViewById(R.id.btn_userRating);
            reviewStatus = itemView.findViewById(R.id.txt_rated);
        }
    }
}
