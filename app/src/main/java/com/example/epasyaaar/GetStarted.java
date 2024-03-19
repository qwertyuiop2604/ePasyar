package com.example.epasyaaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStarted extends AppCompatActivity implements View.OnClickListener{

    Button getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted= findViewById(R.id.btnGetstarted);

        getStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if ( v.getId() == R.id.btnGetstarted){
            startActivity(new Intent(GetStarted.this, Login.class));
        }

    }
}