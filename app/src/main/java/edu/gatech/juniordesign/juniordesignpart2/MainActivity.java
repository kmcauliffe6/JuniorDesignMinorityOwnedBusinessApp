package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToBusinessListActivity (View view){
        Intent intent = new Intent (this, BusinessListActivity.class);
        Button clicked = (Button) view;
        String category = clicked.getText().toString();
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void goToSettingsPageActivity (View view){
        Intent intent = new Intent (this, SettingsPageActivity.class);
        startActivity(intent);
    }
    
    public void goToProfilePageActivity (View view) {
        Intent intent = new Intent (this, ProfilePageActivity.class);
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

}
