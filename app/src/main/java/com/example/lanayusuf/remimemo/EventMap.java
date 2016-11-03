package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.DialogFragment;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventMap extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Map and list of events
    //TODO: list event names which are called from database
    //TODO: get lat and long coordinates from address tagged in event description and put marker on map

    private GoogleMap mMap;
    String[] continents = {"North America", "South America", "Europe", "Asia", "Africa", "Antarctica", "Australia"};

    protected GoogleApiClient mGoogleApiClient;
    protected Marker markerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_and_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnBack = (Button)findViewById(R.id.btnBackPriority);
        btnBack.setOnClickListener(this);

        Button btnMyLocation = (Button)findViewById(R.id.btnCurrentPos);
        btnMyLocation.setOnClickListener(this);

        //ListView
        //TODO: populate array with events from database (those that have location tags)
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.map_list_view, continents);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Event Map", "Connection failed");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("Event Map", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBackPriority:
                //bring to Main Options screen
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.btnCurrentPos:
                //Sensor Requirement
                //On map, zoom in to user's current location, and add marker


                //check phone's permission for location services
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //dialog to tell user to enable location services
                    DialogFragment permission = new PermissionDialog();
                    permission.show(getSupportFragmentManager(), "Location Permission");

                }else{
                    if(markerLocation != null){
                        markerLocation.remove();
                    }

                    //get current location
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LatLng latLong = new LatLng(location.getLatitude(),location.getLongitude());

                    //add marker on current location
                    markerLocation = mMap.addMarker(new MarkerOptions().position(latLong).title("ME!"));

                    //move map/camera to current location
                    CameraPosition myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        //user clicks on event which moves the map to the location marker for that event
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
