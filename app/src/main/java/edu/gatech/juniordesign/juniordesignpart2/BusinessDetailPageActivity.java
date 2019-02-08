package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;


public class BusinessDetailPageActivity extends AppCompatActivity {

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



