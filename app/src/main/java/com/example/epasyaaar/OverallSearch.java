    package com.example.epasyaaar;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageButton;
    import android.widget.PopupMenu;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.SearchView;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.ArrayList;
    import java.util.List;

    public class OverallSearch extends AppCompatActivity {

        private RecyclerView recyclerView;
        private OverallSearch_Adapter adapter;

        private FirebaseFirestore firestoreDB;

        private ArrayList<OverallSearch_Data> list;

        private SearchView searchView;

        ImageButton filter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_overall_search);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            searchView = findViewById(R.id.overallSearch);
            searchView.clearFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    // Assuming you have a variable to store the selected category
                    String selectedCategory = "All"; // Default category

                    // Call filterList with both newText and selectedCategory
                    filterList(newText, selectedCategory);
                    return true;
                }

            });

            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            firestoreDB = FirebaseFirestore.getInstance();
            list = new ArrayList<>();
            adapter = new OverallSearch_Adapter(this, list); // Initialize the adapter
            recyclerView.setAdapter(adapter);

            retrieveViganEstablishments("All");

            filter = findViewById(R.id.filter);
            PopupMenu popupMenu = new PopupMenu(this, filter);
            popupMenu.inflate(R.menu.filter_menu);

            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            popupMenu.setOnMenuItemClickListener(item -> {
                String category = "";
                if (item.getItemId() == R.id.filter_Hotel) {
                    category = "Hotel";
                } else if (item.getItemId() == R.id.filter_TouristSpot) {
                    category = "Tourist Spot";
                } else if (item.getItemId() == R.id.filter_Restaurant) {
                    category = "Restaurant";
                } else if (item.getItemId() == R.id.filter_SouvenirS) {
                    category = "Souvenir Shop";
                }
                retrieveViganEstablishments(category);
                return true;
            });


        }

        private void filterList(String newText, String category) {
            List<OverallSearch_Data> filteredList = new ArrayList<>();
            for (OverallSearch_Data item : list) {
                if (("All".equals(category) || category == null || category.equals(item.getCategory())) &&
                        item.getName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
            }
            adapter.setFilteredList(filteredList);
            adapter.notifyDataSetChanged();
        }


        private void retrieveViganEstablishments(String category) {
            firestoreDB.collection("vigan_establishments")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (DocumentSnapshot est_document : task.getResult()) {
                                if (est_document.exists()) {
                                    String name = est_document.getString("Name");
                                    String photo = est_document.getString("Photo");
                                    String cat = est_document.getString("Category");
                                    String documentId2 = est_document.getId();


                                    OverallSearch_Data establishment = new OverallSearch_Data(name, photo, cat, documentId2);
                                    list.add(establishment);
                                }
                            }
                            filterList("", category); // Filter the list based on the selected category
                        } else {
                            // Handle error
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    });
        }





    }
