package edu.gatech.juniordesign.juniordesignpart2;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProfilePageActivity extends AppCompatActivity {
    private static DatabaseModel model;
    private static numFavoritesReviews numTask = null;
    private static int numFavoritesResults = 0;
    private static int numReviewsResults = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec ts = tabhost.newTabSpec("Favorites");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Favorites");
        tabhost.addTab(ts);
        ts = tabhost.newTabSpec("Reviews");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Reviews");
        tabhost.addTab(ts);

        TextView name = findViewById(R.id.user_first_name);
        TextView numFavorites = findViewById(R.id.user_num_favorites);
        TextView numReviews = findViewById(R.id.user_num_reviews);

        if (Guest.isGuestUser()) {
            name.setText("Welcome Guest");


            String yourFilePath = ProfilePageActivity.this.getFilesDir() + "/" + "guest_favorites";
            File favoritesFile = new File(yourFilePath);
            ArrayList<String> favorites = new ArrayList<>();
            try {
                Scanner scanner = new Scanner(favoritesFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    favorites.add(line);
                }
            } catch (FileNotFoundException e) {
                Log.e("Guest Saves", "File Not Found" + ProfilePageActivity.this.getFilesDir());
            }

            numFavorites.setText(Integer.toString(favorites.size()) + " Favorites");
            numReviews.setText("0 Reviews");
        } else {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            User current = model.getCurrentUser();
            if (current == null) {
                SharedPreferences shared = getSharedPreferences("login", MODE_PRIVATE);
                User currentUser = new User(
                        shared.getString("email", ""),
                        shared.getString("firstName", ""),
                        shared.getString("lastName", ""),
                        shared.getBoolean("admin", false),
                        shared.getString("entity", ""));
                current = currentUser;
                model.setCurrentUser(currentUser);
            }
            String firstName = current.getFirstName();
            String lastName = current.getLastName();
            name.setText(firstName + " " + lastName);
            numTask = new numFavoritesReviews();
            try {
                numTask.execute((Void) null).get();
            } catch (Exception e){
                Log.e("UserFavoritesTab", e.getMessage());
            }

            numFavorites.setText(Integer.toString(numFavoritesResults) + " Favorites");

            numReviews.setText(Integer.toString(numReviewsResults) + " Reviews");
        }
    }

    private static class numFavoritesReviews extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            int[] results = model.queryNumFavoritesAndReviews();
            numFavoritesResults = results[0];
            numReviewsResults = results[1];
            Log.i("numFavoritesReviews", results + ": " + numFavoritesResults + ", " + numReviewsResults );
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            numTask = null;
        }

        @Override
        protected void onCancelled() {
            numTask = null;
        }
    }
}
