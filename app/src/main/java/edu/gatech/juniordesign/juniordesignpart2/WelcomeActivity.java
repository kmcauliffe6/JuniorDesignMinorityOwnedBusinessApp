package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * goes to LoginActivity
     * @param view the current view
     *
     */
    public void goToLoginActivity (View view) {
        Intent intent = new Intent (this, LoginActivity.class);
        startActivity(intent);
    }

    /** goes to Registration Activity
     * @param view the current view
     */
    public void goToRegistrationActivity (View view) {
        Intent intent = new Intent (this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * goes to MainActivity when user selects continue as guest
     * @param view the current view
     *
     */
    public void goToMainActivity (View view) {
        Intent intent = new Intent (this,MainActivity.class);
        startActivity(intent);
    }
}
