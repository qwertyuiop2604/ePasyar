package com.example.epasyaaar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.content.pm.ActivityInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCode extends AppCompatActivity {

    ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize barLauncher
        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                showResultDialog(result.getContents());
            }
        });

        scanCode();
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
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(QRCode.this);
            builder.setTitle("Result");
            builder.setMessage(contents);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }



}
