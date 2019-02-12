package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class ProfilePageActivity extends AppCompatActivity {

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

        DatabaseModel.checkInitialization();
        DatabaseModel model = DatabaseModel.getInstance();

        TextView name = findViewById(R.id.user_first_name);
        User current = model.getCurrentUser();
        String firstName = current.getFirstName();
        String lastName = current.getLastName();
        name.setText(firstName + " " + lastName);

        TextView numFavorites = findViewById(R.id.user_num_favorites);
        int num = 32;   //TODO: get the current users number of favorites
        numFavorites.setText(Integer.toString(num) + " Favorites");

        TextView numReviews = findViewById(R.id.user_num_reviews);
        num = 6; //TODO: get the current users number of reviews
        numReviews.setText(Integer.toString(num) + " Reviews");

    }
}
