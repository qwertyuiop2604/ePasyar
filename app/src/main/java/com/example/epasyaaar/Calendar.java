package com.example.epasyaaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Calendar extends AppCompatActivity implements View.OnClickListener {

    // Member variables
    private CalendarView calendarView;
    private java.util.Calendar calendar;
    private ImageButton add;
    private String stringDateSelected;

    private DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btn_Drawer;

    ImageView userImage;
    TextView userName, userEmail;

    TextView user_Nav_sched;

    TextView userSchedule;
    String userID;
    Uri uri;

    StorageReference storageReference;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        add = findViewById(R.id.btn_addEvent);
        calendar = java.util.Calendar.getInstance();

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        // Set listener for calendar date changes
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                // Toast.makeText(Calendar.this, stringDateSelected, Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for "Add Event" button
        add.setOnClickListener(this);

        // Initialize Firebase components
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("UserProfile");

        // Initialize navigation drawer views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btn_Drawer = findViewById(R.id.menu);

        // Set click listener for opening navigation drawer
        btn_Drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        // Set up navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        userImage = headerView.findViewById(R.id.userProfile);
        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);




        // Set click listener for user image in drawer header
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dashboard", "User Image Clicked");
                Intent intent = new Intent(Calendar.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Set navigation item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.userProfile) {
                    Log.d("Dashboard", "User Profile Clicked");
                    drawerLayout.closeDrawer(navigationView);
                    Intent intent = new Intent(Calendar.this, UserProfile.class);
                    startActivity(intent);
                    return true;
                }

                if(itemID == item.getItemId()){
                    startActivity(new Intent(Calendar.this, Nav_sched.class));
                    return true;
                }
                return false;
            }
        });

        // Check if user is authenticated
        if (fAuth.getCurrentUser() != null) {
            firebaseUser = fAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            // Get user data from Firestore
            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        // Update user information in navigation drawer
                        String firstName = value.getString("fName");
                        String lastName = value.getString("lName");
                        String fullNameText = firstName + " " + lastName;
                        userName.setText(fullNameText);
                        userEmail.setText(value.getString("email"));

                        // Load user profile image
                        uri = firebaseUser.getPhotoUrl();
                        Picasso.with(Calendar.this).load(uri).into(userImage);

                        // Check if user has a custom image in Firestore
                        if (value.getString("imageUrl") != null) {
                            String imagePath = value.getString("imageUrl");
                            StorageReference imageRef = storageReference.child(imagePath);
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Set User's current Dp in ImageView
                                    if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
                                        uri = firebaseUser.getPhotoUrl();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        // Handle click events
        if (v.getId() == R.id.btn_addEvent) {
            // Open Schedule activity with selected date
            Intent intent = new Intent(Calendar.this, Schedule.class);
            intent.putExtra("selectedDate", stringDateSelected);
            startActivity(intent);
        }


    }

}
