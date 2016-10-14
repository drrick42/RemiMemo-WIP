package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventMap extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private GoogleMap mMap;
    String[] continents = {"North America", "South America", "Europe", "Asia", "Africa", "Antarctica", "Australia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_and_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        //ListView
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.map_list_view, continents);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        switch (position)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                LatLng europe = new LatLng(54.5260, 15.2551);
                mMap.addMarker(new MarkerOptions().position(europe).title("Europe"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(europe));
                break;
            case 3:
                LatLng asia = new LatLng(34.0479, 100.6197);
                mMap.addMarker(new MarkerOptions().position(asia).title("Asia"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(asia));
                break;
            case 4:
                LatLng africa = new LatLng(8.7832, 34.5085);
                mMap.addMarker(new MarkerOptions().position(africa).title("Africa"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(africa));
                break;
            case 5:
                break;
            case 6:
                break;
        }
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
    }
}
