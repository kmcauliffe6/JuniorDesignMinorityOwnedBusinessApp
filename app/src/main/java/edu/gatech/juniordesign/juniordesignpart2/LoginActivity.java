package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private static UserLoginTask mAuthTask = null;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = "Google Log In";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private GoogleSignInClient mGoogleSignInClient;

    // Setting up variables to have user login only need to happen once
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        shared = getSharedPreferences("login", MODE_PRIVATE);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        shared = getSharedPreferences("login",MODE_PRIVATE);

        if (shared.getBoolean("logged",false)) {
            Log.i("LoginActivity", "onPostExecute Success");
            //got to main activity if login succeeds

            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();

            User currentUser = new User(
                    shared.getString("email", ""),
                    shared.getString("firstName", ""),
                    shared.getString("lastName", ""),
                    shared.getBoolean("admin", false),
                    shared.getString("entity", ""));

            model.setCurrentUser(currentUser);

            Guest.setGuestUser(false);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email.toLowerCase(), password);
            try {
                boolean success = mAuthTask.execute((Void) null).get();
                if (success) {
                    Log.i("LoginActivity", "onPostExecute Success");
                    //got to main activity if login succeeds
                    Guest.setGuestUser(false);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    shared.edit().putBoolean("logged",true).apply();

                    //Get current instance of the database
                    DatabaseModel.checkInitialization();
                    DatabaseModel model = DatabaseModel.getInstance();
                    User currentUser = model.getCurrentUser();

                    shared.edit().putString("firstName", currentUser.getFirstName()).apply();
                    shared.edit().putString("lastName", currentUser.getLastName()).apply();
                    shared.edit().putString("email", currentUser.getEmail()).apply();
                    shared.edit().putBoolean("admin", currentUser.getAdmin()).apply();
                    shared.edit().putString("entity", currentUser.getEntity()).apply();

                    finish();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid Credentials. \n  Please Try Again.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } catch (Exception ignored) {
                Log.e("LoginActivity", "error line 117");
            }
        }
    }

    private boolean isEmailValid (String email){
        return email.contains("@");
    }

    private boolean isPasswordValid (String password){
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.login(mEmail, mPassword);
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
