package com.example.epasyaaar;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanOptions;

public class IconClickListener implements View.OnClickListener {
    private Context context;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    public IconClickListener(Context context, ActivityResultLauncher<ScanOptions> barLauncher) {
        this.context = context;
        this.barLauncher = barLauncher;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option_events) {
            navigateTo(Events.class);
        } else if (v.getId() == R.id.option_otop) {
            navigateTo(QrGen_sample.class);
        } else if (v.getId() == R.id.option_maps) {
            navigateTo(Maps.class);
        } else if (v.getId() == R.id.option_home) {
            navigateTo(Dashboard.class);
        }
        else if (v.getId() == R.id.top_search) {
            navigateTo(OverallSearch.class);
        }
    }


    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(context, destination);
        context.startActivity(intent);
    }

}
