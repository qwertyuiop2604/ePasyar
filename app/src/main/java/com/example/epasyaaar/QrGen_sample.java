package com.example.epasyaaar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;



public class QrGen_sample extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_gen_sample);

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("v_ts").document("caesar");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String documentId = documentSnapshot.getId(); // Get the document ID
                    generateQRCode(documentId); // Pass document ID to generate QR code
                    passDocumentIdToAnotherActivity(documentId); // Pass document ID to another activity+-
                } else {
                    Toast.makeText(QrGen_sample.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QrGen_sample.this, "Failed to retrieve data from Firestore", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateQRCode(String documentId) {
        // Generate QR code using ZXing library
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(documentId, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // Display the generated QR code
            ImageView imageView = findViewById(R.id.QR_pic);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(QrGen_sample.this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void passDocumentIdToAnotherActivity(String documentId) {
        Intent intent = new Intent(QrGen_sample.this, TouristSpots.class);
        intent.putExtra("documentId", documentId);

    }

}
