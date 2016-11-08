package com.example.lanayusuf.remimemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

import java.util.List;

public class EventMap extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Map and list of events
    //TODO: list event names which are called from database
    //TODO: get lat and long coordinates from address tagged in event description and put marker on map

    private GoogleMap mMap;
    String[] eventNames = new String[10];
    String[] eventLocations = new String[10];
    int counter = 0;

    String[] realEventNames;

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




        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //code from http://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address/27834110#27834110
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
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
                    if(location != null){
                        LatLng latLong = new LatLng(location.getLatitude(),location.getLongitude());

                        //add marker on current location
                        markerLocation = mMap.addMarker(new MarkerOptions().position(latLong).title("ME!"));

                        //move map/camera to current location
                        CameraPosition myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                    }

                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        LatLng latLong;
        CameraPosition myPosition;
        //user clicks on event which moves the map to the location marker for that event
        switch (position)
        {
            case 0:
                latLong = getLocationFromAddress(this, eventLocations[0]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[0]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 1:
                latLong = getLocationFromAddress(this, eventLocations[1]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[1]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 2:
                latLong = getLocationFromAddress(this, eventLocations[2]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[2]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 3:
                latLong = getLocationFromAddress(this, eventLocations[3]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[3]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 4:
                latLong = getLocationFromAddress(this, eventLocations[4]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[4]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 5:
                latLong = getLocationFromAddress(this, eventLocations[5]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[5]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 6:
                latLong = getLocationFromAddress(this, eventLocations[6]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[6]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 7:
                latLong = getLocationFromAddress(this, eventLocations[7]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[7]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 8:
                latLong = getLocationFromAddress(this, eventLocations[8]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[8]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 9:
                latLong = getLocationFromAddress(this, eventLocations[9]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[9]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                break;
            case 10:
                latLong = getLocationFromAddress(this, eventLocations[10]);
                mMap.addMarker(new MarkerOptions().position(latLong).title(eventNames[10]));
                myPosition = new CameraPosition.Builder().target(latLong).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
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

        if (EventDBHandler.getInstance().isDatabaseExists()) {

            //fill map list with event names that have an associated location
            //add marker on map for each event
            EventRemimemo event;
            List<EventRemimemo> eventRemimemoList = EventDBHandler.getInstance().queryEvents("High");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                if (event.getEventLocation().length() != 0) {
                    eventNames[counter] = event.getEventName();
                    eventLocations[counter] = event.getEventLocation();
                    counter++;
                    LatLng latLong = getLocationFromAddress(this, event.getEventLocation());
                    if (latLong != null) {
                        mMap.addMarker(new MarkerOptions().position(latLong).title(event.getEventName()));
                    }
                }
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("Low");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                if (event.getEventLocation().length() != 0) {
                    eventNames[counter] = event.getEventName();
                    eventLocations[counter] = event.getEventLocation();
                    counter++;
                    LatLng latLong = getLocationFromAddress(this, event.getEventLocation());
                    if (latLong != null) {
                        mMap.addMarker(new MarkerOptions().position(latLong).title(event.getEventName()));
                    }
                }
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("None");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                if (event.getEventLocation().length() != 0) {
                    eventNames[counter] = event.getEventName();
                    eventLocations[counter] = event.getEventLocation();
                    counter++;
                    LatLng latLong = getLocationFromAddress(this, event.getEventLocation());
                    if (latLong != null) {
                        mMap.addMarker(new MarkerOptions().position(latLong).title(event.getEventName()));
                    }
                }
            }

            if (counter != 0) {
                realEventNames = new String[counter];
                for(int i = 0; i < counter; i++){
                    realEventNames[i] = eventNames[i];
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.map_list_view, realEventNames);
                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
            }
        }
    }
}
