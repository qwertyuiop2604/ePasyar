package com.example.epasyaaar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomCalendarView extends CalendarView {
    private Paint textPaint;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private List<String> selectedDays;

    public CustomCalendarView(Context context) {
        super(context);
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(40); // Set text size as needed
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        selectedDays = new ArrayList<>();
        fetchSelectedDays();
    }

    private void fetchSelectedDays() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firestoreDB.collection("users")
                    .document(userId)
                    .collection("events")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        selectedDays.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                String date = document.getId();
                                selectedDays.add(date);
                            }
                        }
                        invalidate(); // Redraw the calendar with the updated event dates
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw numbers for days
        drawNumbersForDays(canvas);
    }

    private void drawNumbersForDays(Canvas canvas) {
        int cellWidth = getWidth() / 7;
        int cellHeight = getHeight() / 6;

        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(getFirstDayOfWeek());
        cal.setTimeInMillis(getDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int startDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < maxDays; i++) {
            int x = (i + startDay) % 7 * cellWidth + cellWidth / 2;
            int y = (i + startDay) / 7 * cellHeight + cellHeight / 2;

            int day = i + 1;
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            if (selectedDays.contains(getDateString(day, month, year))) {
                textPaint.setColor(Color.BLUE); // Change color to blue if there is an event for that date
            } else {
                textPaint.setColor(Color.BLACK);
            }

            canvas.drawText(String.valueOf(day), x, y, textPaint);
        }
    }

    private String getDateString(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return String.valueOf(cal.getTimeInMillis());
    }
}
