package edu.gatech.juniordesign.juniordesignpart2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static DatabaseModel model;
    private ArrayList<String> categories = new ArrayList<>();
    private static MainActivity.CategoriesRetrieval mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();

        mAuthTask = new CategoriesRetrieval();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                RecyclerView mRecyclerView = findViewById(R.id.recycler_view_categories);
                mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));


                this.categories.add("SEE ALL");
                for (int x = 0; x < model.getCategoryList().size(); x++) {
                    this.categories.add(model.getCategoryList().get(x));
                }

                if (this.categories == null) {
                    throw new Exception("returned categories were null");
                }
                Log.d("Main Activity", Integer.toString(this.categories.size()));
                CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(MainActivity.this, this.categories);
                mRecyclerView.setAdapter(adapter);
                Log.d("Main Activity", "setting up recycler view");
            }

        } catch (Exception e) {
            Log.e("CategoryList", "error setting up categories list");
        }
    }
/*
    public void goToBusinessListActivity(View view) {
        Intent intent = new Intent(this, BusinessListActivity.class);
        Button clicked = (Button) view;
        String category = clicked.getText().toString();
        Log.i("Main", "set category: " + category);
        model.setSelectedCategory(category);
        intent.putExtra("category", category);
        startActivity(intent);
    }
*/
    public void goToSettingsPageActivity(View view) {
        Intent intent = new Intent(this, SettingsPageActivity.class);
        startActivity(intent);
    }

    public void goToProfilePageActivity(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // set up ActionBar with settings and profile icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingsButton) {
            goToSettingsPageActivity(getWindow().getDecorView().getRootView());
        }

        if (id == R.id.profilePicButton) {
            goToProfilePageActivity(getWindow().getDecorView().getRootView());
        }
        return super.onOptionsItemSelected(item);
    }

    private static class CategoriesRetrieval extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.getCategories();
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
