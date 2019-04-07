package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class WelcomeActivity extends AppCompatActivity {

    SharedPreferences shared;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = "Google Log In";
    private GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared = getSharedPreferences("login",MODE_PRIVATE);

        if (shared.getBoolean("logged",false)) {
            Log.i("LoginActivity", "onPostExecute Success");
            //got to main activity if login succeeds
            Guest.setGuestUser(false);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_welcome);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        //sign in user through Google
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //google sign in here
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        Log.w(TAG, "sign in button clicked");
                        googleSignIn();
                        break;
                    // ...
                }
            }
        });
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
        Guest g = new Guest();
        g.setGuestUser(true);
        Intent intent = new Intent (this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Google and Facebook Sign In Helper Methods Below:
     */
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        //update UI depending on result of login
        if (account != null) {
            Log.w(TAG, "successful login");
            Guest.setGuestUser(false);
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            String userEmail = account.getEmail();
            String firstName = account.getGivenName();
            String lastName = account.getFamilyName();
            //register user here
            Context context = getApplicationContext();
            CharSequence text = "Login With Google Worked!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        } else {
            //google authentication failed
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            Context context = getApplicationContext();
            CharSequence text = "Login with Google Failed. Please try again";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        }
    }
}
