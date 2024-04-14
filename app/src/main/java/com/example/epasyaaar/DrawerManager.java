package com.example.epasyaaar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

public class DrawerManager {

    private final Activity activity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navigationView;

    private FirebaseAuth fAuth;
    private FirebaseUser firebaseUser;

    private StorageReference storageReference;
    private FirebaseFirestore fStore;

    private String userID;
    private Uri uri;

    public DrawerManager(Activity activity, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("UserProfile");

        setupNavigationDrawer();
    }

    void setupNavigationDrawer() {
        ImageView userImage;
        TextView userName, userEmail;

        View headerView = navigationView.getHeaderView(0);
        userImage = headerView.findViewById(R.id.userProfile);
        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DrawerManager", "User Image Clicked");

                Intent intent = new Intent(activity, UserProfile.class);
                activity.startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.userProfile) {
                    Log.d("DrawerManager", "User Profile Clicked");

                    drawerLayout.closeDrawer(navigationView);

                    Intent intent = new Intent(activity, UserProfile.class);
                    activity.startActivity(intent);

                    return true;
                }

                if(itemID ==R.id.menu_personal_Schedule){
                    activity.startActivity(new Intent(activity, Nav_sched.class));
                    return true;
                }

                if(itemID ==R.id.menu_recentV){
                    activity.startActivity(new Intent(activity, Nav_visits.class));
                    return true;
                }
                return false;
            }
        });

        if (fAuth.getCurrentUser() != null) {
            firebaseUser = fAuth.getCurrentUser(); // Get the current user
            userID = firebaseUser.getUid();

            DocumentReference documentReference = fStore.collection("users").document(userID);

            documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        String firstName = value.getString("fName");
                        String lastName = value.getString("lName");
                        String fullNameText = firstName + " " + lastName;
                        userName.setText(fullNameText);
                        userEmail.setText(value.getString("email"));

                        uri = firebaseUser.getPhotoUrl();
                        Picasso.get().load(uri).into(userImage);

                        // Check if user has an image URL in Firestore
                        if (value.getString("imageUrl") != null) {
                            String imagePath = value.getString("imageUrl");
                            StorageReference imageRef = storageReference.child(imagePath);
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
                                        uri = firebaseUser.getPhotoUrl();
                                        // Set User's current Dp in ImageView
                                    }
                                }
                            });
                        }

                    } else {
                        // Handle the case where the document does not exist
                        // or there is an error fetching the document.
                    }
                }
            });
        } else {
            // Handle the case where the user is not authenticated.
        }
    }
}
