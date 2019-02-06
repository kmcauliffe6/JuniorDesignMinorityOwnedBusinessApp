package edu.gatech.juniordesign.juniordesignpart2;

import android.support.constraint.Placeholder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TabHost;
import android.widget.TextView;

public class BusinessDetailPageActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private BusinessDetailPageActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail_page);

        //set up tabs section for reviews, photos, and about the owner pages
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



