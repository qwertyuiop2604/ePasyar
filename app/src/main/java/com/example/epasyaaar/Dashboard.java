package com.example.epasyaaar;

import static com.google.android.gms.vision.L.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    private IconClickListener iconClickListener;
    private DrawerManager drawerManager;

    private SimpleExoPlayer player;

    private PlayerView playerView;

    ImageButton qr;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btn_Drawer;

    VideoView videoView;

    ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //icon functions

        iconClickListener = new IconClickListener(this, barLauncher);

        ImageView eventsImageView = findViewById(R.id.option_events);
        eventsImageView.setOnClickListener(iconClickListener);

        ImageView otopImageView = findViewById(R.id.option_otop);
        otopImageView.setOnClickListener(iconClickListener);

        ImageView mapsImageView = findViewById(R.id.option_maps);
        mapsImageView.setOnClickListener(iconClickListener);

        ImageView homeImageView = findViewById(R.id.option_home);
        homeImageView.setOnClickListener(iconClickListener);

        ImageView searchImageView = findViewById(R.id.top_search);
        searchImageView.setOnClickListener(iconClickListener);

        //Drawer functions

         drawerLayout = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
         btn_Drawer = findViewById(R.id.menu);

        btn_Drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
                DrawerManager drawerManager = new DrawerManager(Dashboard.this, drawerLayout, navigationView);
                drawerManager.setupNavigationDrawer();
            }
        });

        VideoView videoView = findViewById(R.id.dash_video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); // Start the video again when playback completes
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference videoRef = db.collection("mob_dash").document("QwUTdYAKiHkhB680ldPU");

        videoRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String videoUrl = documentSnapshot.getString("mob_vid_prom");
                videoView.setVideoURI(Uri.parse(videoUrl));
                videoView.start();
            } else {
                Log.d("VideoView", "Video URL not found");
            }
        }).addOnFailureListener(e -> {
            Log.e("VideoView", "Error fetching video URL", e);
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("VideoView", "Error: " + what + "," + extra);
            return true;
        });

        DocumentReference descRef = db.collection("mob_dash").document("Vigan Description");
        descRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String viganDescription = documentSnapshot.getString("Description");
                    TextView descVigan = findViewById(R.id.dash_viganDescription);
                    descVigan.setText(viganDescription);
                } else {
                    Log.d(TAG, "Document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error retrieving document", e);
            }
        });





        //for Qr
        qr = findViewById(R.id.option_QR);
        qr.setOnClickListener(this);



        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                showResultDialog(result.getContents());
            }
        });



    }



    private void scanCode() {
        ScanOptions option = new ScanOptions();
        option.setBeepEnabled(true);
        option.setPrompt("Place the code inside the frame");
        option.setOrientationLocked(true);
        option.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(option);
    }

    private void showResultDialog(final String contents) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Please select the number of tourists:");

                // Create a number picker dialog
                final NumberPicker numberPicker = new NumberPicker(Dashboard.this);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(100); // Set the maximum number of tourists
                builder.setView(numberPicker);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int numberOfTourists = numberPicker.getValue();
                        int scansToAdd = numberOfTourists;

                        Log.d("Dashboard", "Scanned document ID: " + contents);

                        Map<String, Object> scanData = new HashMap<>();
                        scanData.put("documentId", contents);
                        scanData.put("currentUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirestoreManager firestoreManager = new FirestoreManager();
                        firestoreManager.updateTotalScansAndUsers(scanData, scansToAdd);

                        Intent intent = new Intent(Dashboard.this, TouristSpots.class);
                        intent.putExtra("documentId", contents);
                        intent.putExtra("numberOfTourists", numberOfTourists);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option_QR) {
            scanCode();
        }

    }
}
