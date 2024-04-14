package com.example.epasyaaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Events extends AppCompatActivity implements View.OnClickListener {

    // Member variables
    // CustomCalendarView calendarView;


    private IconClickListener iconClickListener;

    private ActivityResultLauncher<ScanOptions> barLauncher;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btn_Drawer;
    ImageButton qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        iconClickListener = new IconClickListener(this, barLauncher);

        ImageView qrCodeImageView = findViewById(R.id.option_QR);
        qrCodeImageView.setOnClickListener(iconClickListener);

        ImageView eventsImageView = findViewById(R.id.option_events);
        eventsImageView.setOnClickListener(iconClickListener);

        ImageView otopImageView = findViewById(R.id.option_otop);
        otopImageView.setOnClickListener(iconClickListener);

        ImageView mapsImageView = findViewById(R.id.option_maps);
        mapsImageView.setOnClickListener(iconClickListener);

        ImageView homeImageView = findViewById(R.id.option_home);
        homeImageView.setOnClickListener(iconClickListener);

        //for Qr
        qr = findViewById(R.id.option_QR);
        qr.setOnClickListener(this);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btn_Drawer = findViewById(R.id.menu);

        btn_Drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
                DrawerManager drawerManager = new DrawerManager(Events.this, drawerLayout, navigationView);
                drawerManager.setupNavigationDrawer();
            }
        });

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

    private void showResultDialog(String contents) {
        /*runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setTitle("Result");
            builder.setMessage(contents);
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        });*/
        Log.d("Dashboard", "Scanned document ID: " + contents); // Log the scanned document ID
        Intent intent = new Intent(Events.this, TouristSpots.class);
        intent.putExtra("documentId", contents);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option_QR) {
            scanCode();
        }
    }

}