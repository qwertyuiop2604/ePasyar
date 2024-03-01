package com.example.epasyaaar;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> mData;
    private List<String> mFilteredData;

    public CustomAdapter(Context context, List<String> data) {
        super(context, android.R.layout.simple_dropdown_item_1line, data);
        mData = data;
        mFilteredData = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return mFilteredData.size();
    }

    @Override
    public String getItem(int position) {
        return mFilteredData.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(mData);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (String item : mData) {
                        if (item.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredData.clear();
                if (results.values != null) {
                    mFilteredData.addAll((List<String>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}
