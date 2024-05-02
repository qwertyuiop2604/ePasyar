package com.example.epasyaaar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.List;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private SearchView mapSearchView;

    private  IconClickListener iconClickListener;

    private ActivityResultLauncher<ScanOptions> barLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        mapSearchView = findViewById(R.id.searchView);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = mapSearchView.getQuery().toString();
                List <Address> addressList = null;

                if(location !=null ){
                    Geocoder geocoder = new Geocoder(Maps.this);

                    try{

                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                    Address address = addressList.get(0);
                    LatLng latlang= new LatLng(address.getLatitude(), address.getLongitude());
                    myMap.addMarker(new MarkerOptions().position(latlang).title(location));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 15));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(Maps.this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap= googleMap;
        LatLng vigan = new LatLng(17.5705,120.3873);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vigan, 20));








    }
}