    package com.example.epasyaaar;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.drawerlayout.widget.DrawerLayout;

    import android.content.Intent;
    import android.media.Image;
    import android.net.Uri;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toolbar;

    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.auth.FirebaseUser;
    import com.squareup.picasso.Picasso;

    import com.google.android.material.navigation.NavigationView;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.EventListener;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.FirebaseFirestoreException;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;

    import org.w3c.dom.Text;

    public class Dashboard extends AppCompatActivity implements View.OnClickListener {

        float x1, x2, y1, y2;
        private static final int MIN_DISTANCE = 150;
        private StorageReference storageReference;


        ImageButton qr;
        ImageButton dashMaps;
        DrawerLayout drawerLayout;
        NavigationView navigationView;
        ImageButton btn_Drawer;
        FirebaseAuth fAuth;
        FirebaseUser firebaseUser;

        ImageView userImage;
        TextView userName, userEmail;

        String userID;
        Uri uri;

        FirebaseFirestore fStore;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

            qr = findViewById(R.id.imgBtn_QR);
            qr.setOnClickListener(this);

            dashMaps = findViewById(R.id.dash_maps);
            dashMaps.setOnClickListener(this);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            btn_Drawer = findViewById(R.id.menu);

            btn_Drawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(navigationView);
                }
            });

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("UserProfile");


            View headerView = navigationView.getHeaderView(0);
            userImage = headerView.findViewById(R.id.userProfile);
            userName = headerView.findViewById(R.id.userName);
            userEmail = headerView.findViewById(R.id.userEmail);

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Dashboard", "User Image Clicked");

                    Intent intent = new Intent(Dashboard.this, UserProfile.class);
                    startActivity(intent);
                }
            });

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemID = item.getItemId();
                    if (itemID == R.id.userProfile) {
                        Log.d("Dashboard", "User Profile Clicked");

                        drawerLayout.closeDrawer(navigationView);

                        Intent intent = new Intent(Dashboard.this, UserProfile.class);
                        startActivity(intent);

                        return true;
                    }
                    return false;
                }
            });

            if (fAuth.getCurrentUser() != null) {
                firebaseUser = fAuth.getCurrentUser(); // Get the current user
                userID = firebaseUser.getUid();

                DocumentReference documentReference = fStore.collection("users").document(userID);

                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists()) {
                            String firstName = value.getString("fName");
                            String lastName = value.getString("lName");
                            String fullNameText = firstName + " " + lastName;
                            userName.setText(fullNameText);
                            userEmail.setText(value.getString("email"));

                            uri = firebaseUser.getPhotoUrl();
                            Picasso.with(Dashboard.this).load(uri).into(userImage);

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


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgBtn_QR) {
                Intent intent = new Intent(this, QRCode.class);
                startActivity(intent);
            }
        }


    }