package edu.gatech.juniordesign.juniordesignpart2;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessListActivity extends AppCompatActivity {

    private static BusinessListRetrevial mAuthTask = null;
    private static DatabaseModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        //TODO: Austin needs the category to be saved on the main page this is for debug
        model.setSelectedCategory( "Construction" ); //Category is Construction for now
        mAuthTask = new BusinessListRetrevial();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//                ArrayList<BusinessListItem> business_list_data = new ArrayList<>();
//                BusinessListItem b1 = new BusinessListItem(12, "Matt LLC", "4.3", "Construction", "Lighting");
//                business_list_data.add(b1);
//                BusinessListItem b2 = new BusinessListItem(12, "Da Biznit", "2.7", "Distributor");
//                business_list_data.add(b2);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, model.getBusinessList());
                mRecyclerView.setAdapter(adapter);
            }
        } catch (Exception e)
        {
            Log.e("BusinessList", e.getMessage());
        }


    }

    private static class BusinessListRetrevial extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryBusinessList();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
