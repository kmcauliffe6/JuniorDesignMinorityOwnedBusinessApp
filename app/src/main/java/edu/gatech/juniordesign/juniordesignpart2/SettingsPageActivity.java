package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class SettingsPageActivity extends AppCompatActivity {

    private static UserDeleteTask mAuthTask = null;
    private static DatabaseModel model;

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
                DatabaseModel.checkInitialization();
                model = DatabaseModel.getInstance();
                mAuthTask = new UserDeleteTask(model.getCurrentUser());
                try {
                    boolean success = mAuthTask.execute((Void) null).get();
                    if (success) {
                        model.clearCurrentUser();
                        goToWelcomeActivity(view);
                        dialog.dismiss();
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Issue connecting to the database, Try again later. If this problem persists please contact support";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } catch (Exception e)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Unknown Error Occured, Try again later. If this problem persists please contact support";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
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

    private static class UserDeleteTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;

        UserDeleteTask(User user) {
            mEmail = user.getEmail();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.removeUser(mEmail);
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
