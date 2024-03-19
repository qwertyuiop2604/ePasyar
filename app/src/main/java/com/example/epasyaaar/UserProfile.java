package com.example.epasyaaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_IMAGE_PICK = 103;

    private StorageReference storageReference;


    TextView fullName, email, country;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    ImageView userProf;
    ImageButton addProf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        country = findViewById(R.id.profileCountry);

        userProf = findViewById(R.id.userPic);
        addProf = findViewById(R.id.btn_addPic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();



        if (firebaseUser != null) {
            userID = firebaseUser.getUid();

            DocumentReference documentReference = fStore.collection("users").document(userID);
            storageReference = FirebaseStorage.getInstance().getReference("UserProfile");
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        String firstName = value.getString("fName");
                        String lastName = value.getString("lName");

                        // Concatenate the first and last name
                        String fullNameText = firstName + " " + lastName;

                        fullName.setText(fullNameText);
                        email.setText(value.getString("email"));
                        country.setText(value.getString("country"));
                    }
                }
            });
        }

        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
            Uri uri = firebaseUser.getPhotoUrl();
            // Set User's current Dp in ImageView
            Picasso.with(UserProfile.this).load(uri).into(userProf);
        }

        addProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserProfile.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                Uri imageUri = data.getData();
                userProf.setImageURI(imageUri);

                // Upload the image to Firebase Storage
                StorageReference profileImageRef = storageReference.child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
                assert imageUri != null;
                profileImageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Image uploaded successfully, now get the download URL
                                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Update the user's profile picture in Firebase Authentication
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                .setPhotoUri(uri)
                                                .build();
                                        assert user != null;
                                        user.updateProfile(request);
                                    }
                                });
                            }
                        });
            }
        } else {
            // If user didn't select an image, show a toast message
            Toast.makeText(UserProfile.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }


}
