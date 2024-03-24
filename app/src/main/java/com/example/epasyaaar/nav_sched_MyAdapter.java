package com.example.epasyaaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class nav_sched_MyAdapter extends RecyclerView.Adapter<nav_sched_MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<nav_schedData> list;

    public nav_sched_MyAdapter(Context context, ArrayList<nav_schedData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        nav_schedData user = list.get(position);
        holder.date.setText(user.getDate());
        holder.description.setText(user.getDescription());
        holder.title.setText(user.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, description, title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.txt_Date);
            description = itemView.findViewById(R.id.txt_desc);
            title = itemView.findViewById(R.id.txt_titleName);

        }
    }

}
