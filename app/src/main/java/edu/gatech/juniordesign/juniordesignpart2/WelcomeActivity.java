package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    SharedPreferences shared;
    private static final String EMAIL = "email";
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private String fbuserid;
    private String fbemail;
    private String fbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

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
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();


                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());
                                if (response != null) {
                                    try {
                                        String name = object.getString("name");
                                        String email = object.getString("email");
                                        String id = object.getString("id");
                                        setupProfileInfo(id, name, email);
                                        Log.v("Email = ", " " + email);
                                        Toast.makeText(getApplicationContext(), "Name " + name, Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

                //register or login user here and then go to home page
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "Facebook Login Cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Error logging into Facebook", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupProfileInfo(String id, String name, String email) {
        this.fbuserid = id;
        this.fbname = name;
        this.fbemail = email;
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
     * Facebook Sign In Helper Methods Below:
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
