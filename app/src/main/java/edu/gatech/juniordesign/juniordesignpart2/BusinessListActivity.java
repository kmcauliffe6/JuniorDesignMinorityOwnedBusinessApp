package edu.gatech.juniordesign.juniordesignpart2;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusinessListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        List<String[]> businesses = new ArrayList<>();
        String[] business = {"Business", "Details", "5.0"};
        businesses.add(0, business);
        businesses.add(1, business);
        businesses.add(2, business);
        businesses.add(3, business);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, /*TODO get list of dummy data*/ businesses);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mRecyclerView.setAdapter(adapter);
        }
    }
}
