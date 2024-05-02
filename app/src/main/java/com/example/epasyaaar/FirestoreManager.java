            package com.example.epasyaaar;

            import android.util.Log;
            import com.google.firebase.firestore.DocumentReference;
            import com.google.firebase.firestore.DocumentSnapshot;
            import com.google.firebase.firestore.FieldValue;
            import com.google.firebase.firestore.FirebaseFirestore;
            import com.google.firebase.firestore.FirebaseFirestoreException;
            import com.google.firebase.firestore.SetOptions;

            import java.text.SimpleDateFormat;
            import java.util.Date;
            import java.util.HashMap;
            import java.util.Map;
            import java.util.UUID;

            public class FirestoreManager {
                private static final String TAG = "FirestoreManager";
                private FirebaseFirestore db;

                // Constructor to initialize the Firestore instance
                public FirestoreManager() {
                    db = FirebaseFirestore.getInstance();
                }

                // Method to update the total scans and users in Firestore

                public void updateTotalScansAndUsers(Map<String, Object> scanData, int scansToAdd) {
                    String documentId = (String) scanData.get("documentId");
                    String currentUserId = (String) scanData.get("currentUserId");

                    if (documentId != null && !documentId.isEmpty()) {
                        DocumentReference totalScansRef = db.collection("total_scans").document(documentId);

                        totalScansRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (!document.exists()) {
                                    // Document doesn't exist, create it with default values
                                    totalScansRef.set(new HashMap<String, Object>() {{
                                        put("totalScans", scansToAdd);
                                    }});
                                } else {
                                    int currentTotalScans = document.getLong("totalScans").intValue();
                                    int newTotalScans = currentTotalScans + scansToAdd;

                                    // Update the totalScans field
                                    totalScansRef.update("totalScans", newTotalScans)
                                            .addOnSuccessListener(aVoid -> {
                                                // Update successful, log a message
                                                Log.d(TAG, "Document scans count updated successfully");
                                                // Store scanned user ID and add recent visit
                                                storeScannedUserId(totalScansRef, currentUserId, scansToAdd);
                                                addRecentVisitToUser(currentUserId, documentId);
                                            })
                                            .addOnFailureListener(e -> Log.e(TAG, "Error updating document scans count", e));
                                }
                            } else {
                                Log.e(TAG, "Error getting document", task.getException());
                            }
                        });
                    } else {
                        Log.e(TAG, "Document ID is null or empty");
                    }
                }



                private void storeScannedUserId(DocumentReference totalScansRef, String currentUserId, int scansToAdd) {
                    // Get the current date
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = dateFormat.format(currentDate);

                    // Create a reference to the document in the scan_dates subcollection
                    DocumentReference scanDateRef = totalScansRef.collection("scannedUsers")
                            .document(currentUserId)
                            .collection("scan_dates")
                            .document(dateStr);

                    // Update the scans count if the document exists for the current date
                    scanDateRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int currentScans = document.getLong("scans").intValue();
                                int newScans = currentScans + scansToAdd;
                                scanDateRef.update("scans", newScans)
                                        .addOnSuccessListener(aVoid -> {
                                            // Update successful, log a message
                                            Log.d(TAG, "Scanned user ID updated successfully in scan_dates");
                                        })
                                        .addOnFailureListener(e -> {
                                            // Update failed, log an error message
                                            Log.e(TAG, "Error updating scanned user ID in scan_dates", e);
                                        });
                            } else {
                                // Document doesn't exist, create it with the scans field
                                Map<String, Object> scannedUserData = new HashMap<>();
                                scannedUserData.put("scans", scansToAdd);
                                scannedUserData.put("timestamp", FieldValue.serverTimestamp());

                                scanDateRef.set(scannedUserData)
                                        .addOnSuccessListener(aVoid -> {
                                            // Set operation successful, log a message
                                            Log.d(TAG, "Scanned user ID stored successfully in scan_dates");
                                        })
                                        .addOnFailureListener(e -> {
                                            // Set operation failed, log an error message
                                            Log.e(TAG, "Error storing scanned user ID in scan_dates", e);
                                        });
                            }
                        } else {
                            // Error getting document, log an error message
                            Log.e(TAG, "Error getting document", task.getException());
                        }
                    });
                }




                // Method to add a recent visit to the user's collection
                private void addRecentVisitToUser(String userId, String documentId) {
                    // Create a map with visit data, including a server timestamp
                    Map<String, Object> visitData = new HashMap<>();
                    visitData.put("timestamp", FieldValue.serverTimestamp());

                    // Add the visit data to the "recent_visits" subcollection under the user's document
                    db.collection("users")
                            .document(userId)
                            .collection("recent_visits")
                            .document(documentId) // Use the same document ID as in the "vigan_establishments" collection
                            .set(visitData)
                            .addOnSuccessListener(aVoid -> {
                                // If the set operation is successful, log a success message
                                Log.d(TAG, "Recent visit added to user's collection");
                            })
                            .addOnFailureListener(e -> {
                                // If the set operation fails, log an error message
                                Log.e(TAG, "Error adding recent visit to user's collection", e);
                            });
                }

                // Inner class to represent a scanned user
                private static class ScannedUser {
                    public String userId;

                    public ScannedUser(String userId) {
                        this.userId = userId;
                    }
                }
            }