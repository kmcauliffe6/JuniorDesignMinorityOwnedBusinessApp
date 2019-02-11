package edu.gatech.juniordesign.juniordesignpart2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

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
    }
}
