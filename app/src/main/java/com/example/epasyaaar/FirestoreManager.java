package com.example.epasyaaar;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {

    private static final String TAG = "FirestoreManager";
    private final FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance(); // Initialize Firestore database instance
    }

    public void updateTotalScansAndUsers(String documentId, String userId) {
        DocumentReference docRef = db.collection("total_scans").document(documentId);

        // Check if total_scans document exists
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Document exists, update total scans count and add user
                    updateDocument(docRef, userId);
                    // Add recent visit to user's collection
                    addRecentVisitToUser(userId, documentId); // Pass the document ID from the "v_ts" collection
                } else {
                    Log.e(TAG, "Total scans document does not exist"); // Log an error if the document does not exist
                }
            } else {
                Log.e(TAG, "Error getting document: ", task.getException()); // Log an error if there's an issue getting the document
            }
        });
    }


    private void updateDocument(DocumentReference docRef, String userId) {
        // Use a transaction to ensure atomicity
        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRef);

            Long totalScans = snapshot.getLong("total_scans");
            if (totalScans == null) {
                totalScans = 0L;
            }
            totalScans++; // Increment total scans count
            transaction.update(docRef, "total_scans", totalScans); // Update total scans count in document

            // Add user to scanned_users map
            Map<String, Object> scannedUsers = (Map<String, Object>) snapshot.getData().get("scanned_users");
            if (scannedUsers == null) {
                scannedUsers = new HashMap<>();
            }
            scannedUsers.put(userId, true);
            transaction.update(docRef, "scanned_users", scannedUsers); // Update scanned_users map in document

            return null;
        }).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Transaction successfully completed!"); // Log success message
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Transaction failure.", e); // Log failure message
        });
    }

    private void addRecentVisitToUser(String userId, String documentId) {
        // Create a map with visit data
        Map<String, Object> visitData = new HashMap<>();
        visitData.put("timestamp", FieldValue.serverTimestamp()); // Use server timestamp for visit timestamp

        // Add the visit data to the "recent_visits" subcollection under the user's document
        db.collection("users").document(userId)
                .collection("recent_visits")
                .document(documentId) // Use the same document ID as in the "v_ts" collection
                .set(visitData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Recent visit added to user's collection"); // Log success message
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding recent visit to user's collection", e); // Log failure message
                });
    }


}
