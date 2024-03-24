package com.example.epasyaaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Nav_sched extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firestoreDB;
    nav_sched_MyAdapter myAdapter;
    ArrayList<nav_schedData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_sched);

        recyclerView = findViewById(R.id.recycler_view);
        firestoreDB = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new nav_sched_MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        // Call method to retrieve events
        retrieveEvents();
    }

    private void retrieveEvents() {
        firestoreDB.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String date = documentSnapshot.getId();
                        String title = documentSnapshot.getString("title");
                        String description = documentSnapshot.getString("description");

                        // Create a nav_schedData object with retrieved data
                        nav_schedData eventItem = new nav_schedData(date, title, description);
                        list.add(eventItem);
                    }

                    // Notify the adapter that the data has changed
                    myAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                });
    }
}
