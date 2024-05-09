package com.example.epasyaaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TouristSpotReviewAdapter extends RecyclerView.Adapter<TouristSpotReviewAdapter.ViewHolder> {

    private Context context;
    private List<TouristSpotReview_Data> reviewsList;

    public TouristSpotReviewAdapter(Context context, List<TouristSpotReview_Data> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristSpotReview_Data review = reviewsList.get(position);
        holder.userEmail.setText(review.getEmail());
        holder.userReview.setText(review.getReview());
        holder.ratingBar.setRating((float) review.getRating()); // Cast double to float
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userEmail, userReview;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.r_userEmail);
            userReview = itemView.findViewById(R.id.r_reviews);
            ratingBar = itemView.findViewById(R.id.r_ratingBar);
        }
    }
}


