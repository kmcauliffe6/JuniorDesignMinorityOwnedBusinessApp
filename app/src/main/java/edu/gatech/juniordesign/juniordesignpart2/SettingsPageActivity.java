package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;

public class SettingsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspage);
    }

    public void goToTermsActivity (View view){
        Intent intent = new Intent (this, TermsActivity.class);
        startActivity(intent);
    }

    public void goToLogoutActivity (View view) {
        Intent intent = new Intent (this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void goToAreYouSureActivity (View view) {
        Intent intent = new Intent (this, AreYouSureActivity.class);
        startActivity(intent);
    }

}
