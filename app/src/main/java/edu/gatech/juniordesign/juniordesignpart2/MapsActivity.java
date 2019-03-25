package edu.gatech.juniordesign.juniordesignpart2;

import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] addresses = DatabaseModel.getInstance().getAddresses();
        for (String add : addresses) {
            Geocoder gc = new Geocoder(this);
            if(gc.isPresent()){
                List<Address> list = new ArrayList<>();
                try {
                    list = gc.getFromLocationName(add, 1);
                } catch (Exception e){
                    Log.e("Maps", e.getMessage());
                    continue;
                }
                if (list.size() > 0) {
                    Address address = list.get(0);
                    double lat = address.getLatitude();
                    double lng = address.getLongitude();
                    LatLng loc = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(loc));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }
            }
        }

        //noinspection MagicNumber, desired zoom for map
        int zoom = 11;



    }
}
