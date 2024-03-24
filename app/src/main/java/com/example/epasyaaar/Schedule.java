package com.example.epasyaaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Schedule extends AppCompatActivity implements View.OnClickListener {

    // Declare UI elements
    Button selectDateButton, saveButton;
    EditText eventTitleText, descriptionEditText;
    TextView emailTextView;
    DatePickerDialog datePickerDialog;
    FirebaseUser currentUser;
    NavigationView navigationView;
    ImageButton drawerButton;
    ImageView userImageView;
    TextView userNameTextView, userEmailTextView;
    Uri imageUri;

    // Firebase
    String userID;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // Get current user and initialize Views
            userID = currentUser.getUid();
            emailTextView = findViewById(R.id.e_email);
            emailTextView.setText(currentUser.getEmail());

            eventTitleText = findViewById(R.id.addEvent);
            descriptionEditText = findViewById(R.id.txtbox_description);
            selectDateButton = findViewById(R.id.btn_sDate);
            saveButton = findViewById(R.id.btn_saveEvent);
            selectDateButton.setOnClickListener(this);
            saveButton.setOnClickListener(this);

            // Initialize Date Picker
            initDatePicker();
        }
    }

    // Method to initialize the Date Picker Dialog
    private void initDatePicker() {
        // Listener for when a date is set in the Date Picker
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = makeDateString(dayOfMonth, month, year);
            selectDateButton.setText(date);
        };

        // Get current date for the Date Picker Dialog
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Create the Date Picker Dialog
        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    // Method to format the date string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Method to get month name from month number
    private String getMonthFormat(int month) {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        return months[month - 1];
    }

    // Method to save the event to Firebase Firestore
    private void saveEventToFirebase(String date, String title) {
        DocumentReference documentReference = firestoreDB.collection("users")
                .document(userID)
                .collection("events")
                .document(date);

        // Get description from EditText
        String description = descriptionEditText.getText().toString().trim();

        // Create event data
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("title", title);
        eventData.put("description", description);

        // Save the event
        documentReference.set(eventData)
                .addOnSuccessListener(aVoid -> showToast("Event Saved"))
                .addOnFailureListener(e -> showToast("Error adding event"));
    }

    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Click listener for the buttons
    @Override
    public void onClick(View v) {
        if (v == selectDateButton) {
            // Show the Date Picker Dialog when 'selectDateButton' button is clicked
            datePickerDialog.show();
        } else if (v == saveButton) {
            // Save the event to Firebase when 'saveButton' button is clicked
            String date = selectDateButton.getText().toString().trim();
            String event = eventTitleText.getText().toString().trim();

            if (!date.isEmpty() && !event.isEmpty()) {
                saveEventToFirebase(date, event);
            } else {
                showToast("Please select a date and enter an event");
            }
        }
    }
}
