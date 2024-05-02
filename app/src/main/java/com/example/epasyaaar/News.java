package com.example.epasyaaar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.ScanOptions;

public class News extends AppCompatActivity {

    private IconClickListener iconClickListener;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


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

        ImageView searchImageView = findViewById(R.id.top_search);
        searchImageView.setOnClickListener(iconClickListener);


    }
}