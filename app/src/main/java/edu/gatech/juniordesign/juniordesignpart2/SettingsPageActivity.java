package edu.gatech.juniordesign.juniordesignpart2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

    public void goToWelcomeActivity (View view) {
        Intent intent = new Intent (this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void confirmLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Account Deletion");
        builder.setMessage("Are you sure you want to delete your account? Your account cannot be recovered after deletion");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                //TODO Ben: connect to the database here to delete the current user's account
                goToWelcomeActivity(view);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
