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
import androidx.activity.OnBackPressedCallback;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCode extends AppCompatActivity {

    ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Initialize barLauncher
        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                showResultDialog(result.getContents());
            }
        });

        scanCode();

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goBackToDashboard();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                goBackToDashboard();
            });
            builder.show();
        });
    }

    private void goBackToDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
