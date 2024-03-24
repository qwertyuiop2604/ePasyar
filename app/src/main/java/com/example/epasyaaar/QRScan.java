package com.example.epasyaaar;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScan {

    private final Activity activity;
    private final ActivityResultLauncher<ScanOptions> barLauncher;

    public QRScan(Activity activity, ActivityResultLauncher<ScanOptions> barLauncher) {
        this.activity = activity;
        this.barLauncher = barLauncher;
    }

    public void startScan() {
        ScanOptions option = new ScanOptions();
        option.setBeepEnabled(true);
        option.setPrompt("Place the code inside the frame");
        option.setOrientationLocked(true);
        option.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(option);
    }

    public void handleScanResult(BarcodeResult result) {
        if (result.getText() != null) {
            showResultDialog(result.getText());
        }
    }

    private void showResultDialog(String contents) {
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Result");
            builder.setMessage(contents);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }
}
