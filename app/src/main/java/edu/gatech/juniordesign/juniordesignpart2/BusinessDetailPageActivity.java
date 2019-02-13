package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.ToggleButton;


public class BusinessDetailPageActivity extends AppCompatActivity {
    private int businessID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail_page);

        TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
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

        final ToggleButton tb = findViewById(R.id.favoriteButton);
        tb.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                        Log.v("*TOGGLE CHECK CHANGED*",String.valueOf(isChecked));

                        if(!isChecked){
                            Log.d("Favorites Button", "buttton is checked");
                            //save this business to favorites
                            if(Guest.isGuestUser()) {
                                Guest g = new Guest();
                                g.saveGuestFavorite(BusinessDetailPageActivity.this, businessID);
                            } else {
                                //TODO: save this favorite to the current users favs
                            }
                        }else{
                            //remove this business from favorites
                            if (Guest.isGuestUser()) {
                                Guest g = new Guest();
                                g.removeGuestFavorite(BusinessDetailPageActivity.this, businessID);
                            }


                        }
                    }
                });


        businessID = 100; //TODO connect this to selected businessID
    }
    /** info needed:
     * business name
     * business type
     * rating
     * reviews
     * favorites
     * photos
     * about the owners
     */

}



