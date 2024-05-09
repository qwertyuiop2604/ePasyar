package com.example.epasyaaar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Nav_visits extends AppCompatActivity {
    private static final String TAG = "Nav_visits";
    private RecyclerView recyclerView;
    private nav_visits_MyAdapter adapter;

    private FirebaseFirestore firestoreDB;

    private ArrayList<nav_visitsData> list;

    Button btnRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_visits);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestoreDB = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new nav_visits_MyAdapter(this, list); // Initialize the adapter
        recyclerView.setAdapter(adapter);

        retrieveRecentVisits();
    }


    private void retrieveRecentVisits() {
        firestoreDB.collection("users").document (FirebaseAuth.getInstance().getUid())
                .collection("recent_visits")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId(); // Get the document ID
                            Object timestampObj = document.get("timestamp");
                            long timestamp;
                            if (timestampObj instanceof com.google.firebase.Timestamp) {
                                timestamp = ((com.google.firebase.Timestamp) timestampObj).getSeconds();
                            } else {
                                Log.e(TAG, "Timestamp field is not a valid timestamp");
                                continue; // Skip this document
                            }

                            // Fetch name and photo URL from the "vigan_establishments" collection using the document ID
                            firestoreDB.collection("vigan_establishments").document(documentId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot vTsDocument = task1.getResult();
                                            if (vTsDocument.exists()) {
                                                String name = vTsDocument.getString("Name");
                                                String photoUrl = vTsDocument.getString("Photo");
                                                String category = vTsDocument.getString("Category");

                                                nav_visitsData data = new nav_visitsData(documentId, name, photoUrl, timestamp, category);
                                                list.add(data);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.e(TAG, "Document does not exist in vigan_establishment collection for document ID: " + documentId);
                                            }
                                        } else {
                                            Log.e(TAG, "Error getting document from v_ts collection", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "Error getting recent visits: ", task.getException());
                    }
                });
    }


}
