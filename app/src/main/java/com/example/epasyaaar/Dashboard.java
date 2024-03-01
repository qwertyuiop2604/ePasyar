package com.example.epasyaaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    float x1,x2,y1,y2;

    ImageButton qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        qr = findViewById(R.id.imgBtn_QR);
        qr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgBtn_QR) {
            Intent intent = new Intent(this, QRCode.class);
            startActivity(intent);
        }
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 <  x2){
                //Intent i = new Intent(Dashboard.this, News.class);
                //startActivity(i);
            }else if(x1 >  x2){
                Intent i = new Intent(Dashboard.this, News.class);
                startActivity(i);
            }
            break;
        }
        return false;
    }
}
