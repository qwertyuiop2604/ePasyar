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
        nav_visitsData data = list.get(position);
        holder.textViewTitle.setText(data.getName());
        Picasso.get()
                .load(data.getPhoto())
                //.placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.error_image)
                .into(holder.imageViewPhoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.rv_Name);
            imageViewPhoto = itemView.findViewById(R.id.rv_pic);
        }
    }
}
