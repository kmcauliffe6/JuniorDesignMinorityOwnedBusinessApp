package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // TODO delete these buttons once the home page is finished

    public void goToSettingsPageActivity (View view){
        Intent intent = new Intent (this, SettingsPageActivity.class);
        startActivity(intent);
    }
    // TODO delete this buttons once the home page is finished
    public void goToBusinessListActivity (View view){
        Intent intent = new Intent (this, BusinessListActivity.class);
        startActivity(intent);
    }

    public void goToBusinessDetailPageActivity (View view){
        Intent intent = new Intent (this, BusinessDetailPageActivity.class);
        startActivity(intent);
    }


}
