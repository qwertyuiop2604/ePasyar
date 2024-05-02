package com.example.epasyaaar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class OverallSearch_TouristSpot extends AppCompatActivity {

        private static final String TAG = "TouristSpots";
        private FirebaseFirestore db; // Firebase Firestore database instance
        private FirebaseAuth mAuth; // Firebase Authentication instance
        private String currentUserId; // ID of the current user
        private FirestoreManager firestoreManager; // Custom FirestoreManager class

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tourist_spots);

            db = FirebaseFirestore.getInstance(); // Initialize Firestore database
            mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

            // Get the scanned document ID passed from the previous activity
            String documentId2 = getIntent().getStringExtra("documentId2");

            // Check if the document ID is not null or empty
            if (documentId2 != null && !documentId2.isEmpty()) {
                // Get the document reference using the scanned document ID
                DocumentReference docRef = db.collection("vigan_establishments").document(documentId2);
                docRef.get().addOnCompleteListener(task -> {
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
        }
}