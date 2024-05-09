package com.example.epasyaaar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;

public class UserReview extends AppCompatActivity {

    EditText review;
    Button submit;
    RatingBar ratingBar;
    ImageView establishmentImage;
    TextView charCount;
    String userID;

    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        // Initialize FirebaseFirestore instance
        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= firebaseAuth.getCurrentUser();

        // Get the documentId, establishmentName, and establishmentPhotoUrl from the Intent
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        String establishmentName = intent.getStringExtra("establishmentName");
        String establishmentPhotoUrl = intent.getStringExtra("establishmentPhotoUrl");

        ratingBar = findViewById(R.id.user_ratingbar);
        ratingBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        ratingBar.setSecondaryProgressTintList(ColorStateList.valueOf(Color.YELLOW));


        review = findViewById(R.id.user_review);
        charCount = findViewById(R.id.character_count);

        review.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int remainingChars = 300 - (s != null ? s.length() : 0);
                charCount.setText(remainingChars + "/300");
            }
        });

        submit = findViewById(R.id.btn_Submit);
        submit.setOnClickListener(v -> {
            float totalRateValue = ratingBar.getRating();
            float ratingValue = ratingBar.getRating();
            String userReview = review.getText().toString().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            if (currentUser != null) {
                // Get current user and initialize Views
                userID = currentUser.getUid();

                // Create a new HashMap to store review data
                HashMap<String, Object> reviewData = new HashMap<>();
                reviewData.put("rating", ratingValue);
                reviewData.put("review_text", userReview);
                reviewData.put("timestamp", timestamp);

                HashMap<String, Object> totalreviewData = new HashMap<>();
                totalreviewData.put("total_rating", totalRateValue);

                // Reference to the ratings collection
                CollectionReference ratingsCollectionRef = firestoreDB.collection("ratings");

                // Get the category of the establishment
                firestoreDB.collection("vigan_establishments").document(documentId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String category = documentSnapshot.getString("Category");
                                if (category != null) {
                                    // Reference to the specific category_reviews subcollection
                                    CollectionReference categoryReviewsCollectionRef = ratingsCollectionRef
                                            .document(category)
                                            .collection(category + "_reviews"); // Modified to use category in subcollection name

                                    // Add the review data to the category_reviews subcollection
                                    categoryReviewsCollectionRef.document(documentId).set(totalreviewData)
                                            .addOnSuccessListener(aVoid -> {
                                                // Document set successfully, now add review to user_reviews subcollection
                                                categoryReviewsCollectionRef.document(documentId).collection("user_reviews").document(userID).set(reviewData)
                                                        .addOnSuccessListener(aVoid1 -> {
                                                            // Review added successfully
                                                            // Handle success
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Failed to add review to user_reviews subcollection
                                                            // Handle failure
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Failed to set document
                                                // Handle failure
                                            });

                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Failed to retrieve establishment data
                            // Handle failure
                        });
            } else {
                // User is not logged in, handle this case (e.g., show a message)
            }



        });


        SharedPreferences.Editor editor = getSharedPreferences("ReviewStatus", MODE_PRIVATE).edit();
        editor.putBoolean(establishmentName + "_reviewed", true); // Unique identifier for each establishment
        editor.apply();


        retrieveEstablishmentforReview(establishmentName, establishmentPhotoUrl);
    }

    private void retrieveEstablishmentforReview(String establishmentName, String establishmentPhotoUrl) {
        // Display establishment name
        TextView establishmentNameTextView = findViewById(R.id.review_Name);
        establishmentNameTextView.setText(establishmentName);

        // Load establishment photo using Picasso
        establishmentImage = findViewById(R.id.review_Photo);
        Picasso.get()
                .load(establishmentPhotoUrl)
                .into(establishmentImage);
    }
}
