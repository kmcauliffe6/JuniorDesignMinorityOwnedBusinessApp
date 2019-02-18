package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
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

}
