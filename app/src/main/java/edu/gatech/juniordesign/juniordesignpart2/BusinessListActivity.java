package edu.gatech.juniordesign.juniordesignpart2;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String[]> businesses = new ArrayList<>();
        String[] business1 = {"Business", "Details", "5.0", "ID"};
        String[] business2 = {"Business", "Details", "3.5", "ID"};
        businesses.add(0, business1);
        businesses.add(1, business2);
        businesses.add(2, business1);
        businesses.add(3, business2);
        //TODO: Get data from database based off filters and category
        // replace new arraylist with call to db method to get list
        ArrayList<BusinessListItem> business_list_data = new ArrayList<>();
        BusinessListItem b1 = new BusinessListItem(12, "Matt LLC", "4.3", "Construction");
        business_list_data.add(b1);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, business_list_data);
        mRecyclerView.setAdapter(adapter);
    }
}
