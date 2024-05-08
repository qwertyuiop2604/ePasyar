package com.example.epasyaaar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
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

public class OverallSearch_Hotel extends AppCompatActivity {

    private static final String TAG = "Hotels";
    private FirebaseFirestore db; // Firebase Firestore database instance
    private FirebaseAuth mAuth; // Firebase Authentication instance
    private String currentUserId; // ID of the current user
    private FirestoreManager firestoreManager; // Custom FirestoreManager class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_overall_search_hotel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


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
                        String owner = document.getString("Owner");
                        String photoUrl = document.getString("Photo");
                        String number = document.getString("Contact Number");
                        String email = document.getString("Email");
                        String address = document.getString("Address");
                        String rooms = document.getString("Rooms");
                        String roomscap = document.getString("Capacity");
                        String roomrate = document.getString("Room Rates");

                        // Display name and description
                        TextView h_nameTV = findViewById(R.id.E_name);
                        TextView h_ownerTV = findViewById(R.id.ownerName);
                        TextView h_NumTV = findViewById(R.id.ownerContact);
                        TextView h_emailTV = findViewById(R.id.ownerEmail);
                        TextView h_addressTV = findViewById(R.id.ownerAddress);
                        TextView h_roomsTV = findViewById(R.id.ownerRooms);
                        TextView h_roomcapTV = findViewById(R.id.ownerRoomCap);
                        TextView h_roomrateTV = findViewById(R.id.ownerRoomRates);

                        h_nameTV.setText(name);
                        h_ownerTV.setText(owner);
                        h_NumTV.setText(number);
                        h_emailTV.setText(email);
                        h_addressTV.setText(address);
                        h_roomsTV.setText(rooms);
                        h_roomcapTV.setText(roomscap);
                        h_roomrateTV.setText(roomrate);

                        // Load and display photo
                        ImageView photoImageView = findViewById(R.id.E_pic);
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