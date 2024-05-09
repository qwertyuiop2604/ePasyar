package com.example.epasyaaar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TouristSpots extends AppCompatActivity {

    private static final String TAG = "TouristSpots";
    private FirebaseFirestore db; // Firebase Firestore database instance
    private String documentId; // ID of the current tourist spot
    private RecyclerView recyclerView;
    private TouristSpotReviewAdapter adapter;

    private Button navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spots);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore database

        // Get the scanned document ID passed from the previous activity
        documentId = getIntent().getStringExtra("documentId");

        // Check if the document ID is not null or empty
        if (documentId != null && !documentId.isEmpty()) {
            // Get the document reference using the scanned document ID
            db.collection("vigan_establishments").document(documentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve and display document fields
                                String name = document.getString("Name");
                                String description = document.getString("Description");
                                String photoUrl = document.getString("Photo");

                                // Display name and description
                                TextView nameTextView = findViewById(R.id.TS_name);
                                TextView descriptionTextView = findViewById(R.id.TS_description);
                                nameTextView.setText(name);
                                descriptionTextView.setText(description);

                                // Load and display photo
                                ImageView photoImageView = findViewById(R.id.TS_pic);
                                Picasso.get().load(photoUrl).into(photoImageView);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "Document ID is null or empty");
            // Handle the case where documentId is null, maybe show an error message or finish the activity
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list to store the reviews
        List<TouristSpotReview_Data> reviewsList = new ArrayList<>();

        // Create an adapter with the reviews list
        adapter = new TouristSpotReviewAdapter(this, reviewsList);

        // Set the adapter to your RecyclerView
        recyclerView.setAdapter(adapter);

        // Get the reviews for the tourist spot
        db.collection("ratings").document("Tourist Spot")
                .collection("Tourist Spot_reviews").document(documentId).collection("user_reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot userReviewSnapshot : task.getResult()) {
                            float rating = userReviewSnapshot.getDouble("rating").floatValue(); // Use getDouble for RatingBar
                            String reviewText = userReviewSnapshot.getString("review_text");
                            Date timestamp = userReviewSnapshot.getDate("timestamp");

                            // Create a TouristSpotReview_Data object and add it to the reviewsList
                            TouristSpotReview_Data reviewData = new TouristSpotReview_Data(rating, reviewText, timestamp);
                            reviewsList.add(reviewData);
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting user reviews: ", task.getException());
                    }
                });

        navigate = findViewById(R.id.navigate_TS);

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristSpots.this, Maps.class ));
            }
        });

    }
}
