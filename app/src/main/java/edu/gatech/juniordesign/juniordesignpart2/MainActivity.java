package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private ArrayList<String> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        DatabaseModel.checkInitialization();
        DatabaseModel model = DatabaseModel.getInstance();
        /*ArrayList<String> catagories = model.getCatagories();
        for (String catagory: catagories) {
            Log.i("ben", catagory);
        }
        mCategories = catagories;*/

         mCategories = new ArrayList<String>(14);
         mCategories.add("Attorney");
         mCategories.add("Restaurant");
         mCategories.add("Accountant");
         mCategories.add("Non-Profit");
         mCategories.add("Publisher");
         mCategories.add("Business Management/Consulting");
         mCategories.add("Bank");
         mCategories.add("Financial Consulting");
         mCategories.add("Media");
         mCategories.add("Marketing");
         mCategories.add("Legal");
         mCategories.add("Printing");
         mCategories.add("Shopping");
         mCategories.add("Grocery");

        //mAdapter = new CategoryAdapter(this, mCategories);
        //mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goToSettingsPageActivity (View view){
        Intent intent = new Intent (this, SettingsPageActivity.class);
        startActivity(intent);
    }
    /**
     @Override
     public void onItemClick(int position) {
     Intent intent = new Intent(this, BusinessesActivity.class);
     startActivity(intent);
     }
     */
}
