package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BusinessDetailPageActivity extends AppCompatActivity {
    private int businessID;
    private static BusinessDetailRetrieval mAuthTask = null;
    private static DatabaseModel model;
    private static BusinessObject b_o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail_page);

        // set up database model
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();

        //get the businessID of the selected business
        businessID = getIntent().getExtras().getInt("business_id");
        //set the model businessSelected to businessID
        model.setSelectedBusiness(businessID);
        mAuthTask = new BusinessDetailRetrieval();

        try {
            boolean success = mAuthTask.execute((Void) null).get();
                if (success) {
                    b_o = model.getSelectedBusinessObject();
                }
        } catch (Exception e) {
            Log.e("BusinessDetails", e.getMessage());
        }

        //set up tabs
        TabHost tabhost = (TabHost) findViewById(R.id.tabhost);
        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("Reviews");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Reviews");
        tabhost.addTab(ts);

        ts = tabhost.newTabSpec("Photos");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Photos");
        tabhost.addTab(ts);
        ts= tabhost.newTabSpec("About The Owner");
        ts.setContent(R.id.tab3);
        ts.setIndicator("About The Owner");
        tabhost.addTab(ts);

        //set up business-specific text fields on the page
        //TODO get business specfic text here
        /** info needed:
         * business name
         * business type
         * rating
         * reviews
         * favorites
         * photos
         * about the owners
         */
        TextView businessName = findViewById(R.id.BusinessName);
        TextView businessCategory = findViewById(R.id.BusinessCategory);
        TextView additionalDetails = findViewById(R.id.BusinessDetails);
        RatingBar reviewStars = findViewById(R.id.reviewStars);
        String ratingNum;

        if (b_o != null) {
            if (b_o.getName() != null) {
                businessName.setText(b_o.getName());
            }
            if (b_o.getCategory() != null) {
                businessCategory.setText(b_o.getCategory());
            }
            if (b_o.getRating() != null) {
                ratingNum = b_o.getRating();
                reviewStars.setRating(Float.valueOf(ratingNum));
            }
        }

        //Set up review and favorite buttons
        Button reviewsButton = findViewById(R.id.reviewButton);
        if (Guest.isGuestUser()) {
            reviewsButton.setEnabled(false); //disable reviews for guest users
        }

        ToggleButton tb = findViewById(R.id.favoriteButton);
        //check if favorite button should be checked
        boolean emptyHeart = true;
        if (Guest.isGuestUser()) {
            String yourFilePath = BusinessDetailPageActivity.this.getFilesDir() + "/" + "guest_favorites";
            File favoritesFile = new File(yourFilePath);
            try {
                Scanner scanner = new Scanner(favoritesFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.trim().compareTo(Integer.toString(businessID)) == 0) {
                        emptyHeart = false;
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e("Guest Saves", "File Not Found");
            }
        }
        tb.setChecked(emptyHeart);

        tb.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                        Log.v("*TOGGLE CHECK CHANGED*",String.valueOf(isChecked));
                        if(!isChecked){
                            Log.d("Favorites Button", "button is a full heart");
                            //save this business to favorites
                            if(Guest.isGuestUser()) {
                                Guest g = new Guest();
                                g.saveGuestFavorite(BusinessDetailPageActivity.this, businessID);
                            } else {
                                //TODO: save this favorite to the current users favorites in the database
                            }
                        }else{
                            //remove this business from favorites
                            Log.d("Favorites Button", "button is empty");
                            if (Guest.isGuestUser()) {
                                Guest g = new Guest();
                                g.removeGuestFavorite(BusinessDetailPageActivity.this, businessID);
                            } else {
                                //TODO: remove this favorite from the current users favorites in the database
                            }
                        }
                    }
                });
    }

    private static class BusinessDetailRetrieval extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryBusinessDetails();
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



