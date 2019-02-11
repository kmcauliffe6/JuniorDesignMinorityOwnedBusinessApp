package edu.gatech.juniordesign.juniordesignpart2;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusinessListActivity extends AppCompatActivity {

    private static BusinessListRetrevial mAuthTask = null;
    private static DatabaseModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
            }
        } catch (Exception e)
        {

        }

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

    private static class BusinessListRetrevial extends AsyncTask<Void, Void, Boolean> {

        private final String category;

        BusinessListRetrevial(String category) {
            this.category = category;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.getBusinessList(category);
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
