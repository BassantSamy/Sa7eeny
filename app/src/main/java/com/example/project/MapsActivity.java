package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener ,
        LocationListener ,
        GoogleMap.OnMarkerClickListener ,
        GoogleMap.OnMarkerDragListener
{


    EditText tf_location;
    static GoogleMap mMap;
    private GoogleApiClient client ;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    double latitude,longitude;
    static Double end_latitude = null, end_longitude = null;
    static String destinationDuration, destinationDistance;
    AutoCompleteTextView autoCompleteTextView;
    int tSec = 0 ;
    String lastWord;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        end_longitude= null ;
        end_latitude = null ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autoCompleteTextView=findViewById(R.id.TF_location);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(MapsActivity.this,android.R.layout.simple_list_item_1));




    }

    protected synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(MapsActivity.this)
                .addConnectionCallbacks(MapsActivity.this)
                .addOnConnectionFailedListener(MapsActivity.this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }



    public void onClick(View v)
    {

        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


        switch(v.getId())
        {
            case R.id.B_search:
               // tf_location = findViewById(R.id.TF_location);
                String location = autoCompleteTextView.getText().toString();
                List<Address> addressList;
                MarkerOptions mo = new MarkerOptions();

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address myAddress = addressList.get(i);
                                LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                                mo.position(latLng);
                                mo.title("Your Search Result");

                                float results[] = new float[10];
                                end_latitude = myAddress.getLatitude() ;
                                end_longitude=  myAddress.getLongitude();
                                //Location.distanceBetween(latitude,longitude, myAddress.getLatitude(), myAddress.getLongitude() , results);

                                mo.snippet("Click To Button to make this your destination");

                                mMap.addMarker(mo);
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


            case R.id.B_to:

                /////Get Duration/////
//                dataTransfer = new Object[3];
//                String url = getDirectionsUrl();
//                GetDirectionsData getDirectionsData = new GetDirectionsData();
//                dataTransfer[0] = mMap;
//                dataTransfer[1] = url;
//                //https://api.tomtom.com/routing/1/calculateRoute/30.037335,31.4776983:29.971841299999994,31.0166975/json?avoid=unpavedRoads&key=Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy
//                //https://api.tomtom.com/routing/1/calculateRoute/30.037335,30.037335:29.971841299999994,31.0166975/json?avoid=unpavedRoads&key=Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy
//
//                dataTransfer[2] = new LatLng(end_latitude, end_longitude);
//                getDirectionsData.execute(dataTransfer);

                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(end_latitude, end_longitude));
                markerOptions.draggable(true);
                markerOptions.title("Your Destination");

                mMap.addMarker(markerOptions);


                Intent in = getIntent();
                Bundle extras = in.getExtras();
                tSec = extras.getInt("time");

                toEntry entry = new toEntry(tSec , new LatLng(end_latitude, end_longitude) , lastWord );
                int index = repeated(lastWord);
                if (index == -1)
                MainActivity.toList.add(entry);
                else
                    MainActivity.toList.set(index, entry);

                break ;


        }

    }

    int repeated (String id )
    {
        for (int i=0 ;i<MainActivity.toList.size() ;i++)
        {
            if (MainActivity.toList.get(i).id.equals(id))
            {
                return i ;
            }
        }
        return -1 ;
    }



    private void B_generic(String name)
    {
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


        //mMap.clear();
        Object dataTransfer[] = new Object[2];

        String url = getUrl(latitude, longitude, name);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,dataTransfer);

        //getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(MapsActivity.this, "Showing Nearby "+name+"s", Toast.LENGTH_SHORT).show();
    }

    private String getDirectionsUrl()
    {

        int versionNumber = 1;
        //https://api.tomtom.com/routing/1/calculateRoute/52.50931%2C13.42936%3A52.50274%2C13.43872/json?avoid=unpavedRoads&key=Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy
        StringBuilder googleDirectionsUrl = new StringBuilder("https://api.tomtom.com/routing/");
        googleDirectionsUrl.append(versionNumber+"/calculateRoute/");
        googleDirectionsUrl.append(latitude+","+longitude);
        googleDirectionsUrl.append(":"+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("/json?");
        googleDirectionsUrl.append("avoid=unpavedRoads&key="+"Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy");

        Log.d("MapsActivity", "Directionsurl = "+googleDirectionsUrl.toString());


        return googleDirectionsUrl.toString();
    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {
        //https://api.tomtom.com/search/2/nearbySearch/.JSON?key=Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy&lat=30.0368938&lon=31.4794608&categorySet=7315
        // 'https://api.tomtom.com/search/2/nearbySearch/.JSON?key=<Your_API_Key>&lat=<lat>&lon=<lon>&categories=<mapcodes>
        StringBuilder googlePlaceUrl = new StringBuilder("https://api.tomtom.com/search/2/nearbySearch/.JSON?key=");
        googlePlaceUrl.append("Ed5gKvYT62zrd71bvzOvAgDuyuJIVVQy");
        googlePlaceUrl.append("&lat="+latitude);
        googlePlaceUrl.append("&lon="+longitude);
        String cat = getCategorySet(nearbyPlace);
        googlePlaceUrl.append("&categorySet="+cat);


        Log.d("MapsActivity", "Nearurl = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    String getCategorySet (String nearbyPlace)
    {
        String googlePlacesData;
        JSONObject jsonObject;
        String id = null;

        googlePlacesData = loadJSONFromAsset(getApplicationContext());

        try {
            jsonObject =getCategoryId (nearbyPlace ,  googlePlacesData);
            if (jsonObject!= null)
            id = jsonObject.getString("id")   ;
            else id= "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
        
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("categories");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    JSONObject getCategoryId (String search , String json) throws JSONException {
        String name;
        JSONObject searchObject = null;
        JSONObject currObject;

        JSONObject all = new JSONObject(json);
        JSONArray array = all.getJSONArray("poiCategories");


        for (int i = 0; i < array.length(); i++) {
            currObject = array.getJSONObject(i);
            name = currObject.getString("name");
            name = name.replace("\n", "");

            if(name.equals(search))
            {
                searchObject = currObject;
                return searchObject;


            }
        }

        return searchObject;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng;
        MarkerOptions markerOptions;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastlocation = location;

        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }

        Intent in = getIntent();
        Bundle extras = in.getExtras();
        String type= extras.getString("name");

        lastWord = type.substring(type.lastIndexOf(" ")+1);
        lastWord =lastWord.toLowerCase();


        if (MainActivity.toList.size() >=2 && type.equals("none") ) //editing addAlarm Page
        {

            end_latitude = MainActivity.toList.get(repeated("destination")).latLng.latitude;
            end_longitude = MainActivity.toList.get(repeated("destination")).latLng.longitude;



        }
        else {
            if (!type.equals("none") && repeated(lastWord)!= -1) //editing tasks page
            {
                end_latitude = MainActivity.toList.get(repeated(lastWord)).latLng.latitude;
                end_longitude = MainActivity.toList.get(repeated(lastWord)).latLng.longitude;



            }
        }


        if (end_latitude == null && end_longitude==null ) { //not editing
            latLng = new LatLng(location.getLatitude(), location.getLongitude()); //get current lat and lng
            markerOptions = new MarkerOptions(); //set marker to current location

            markerOptions.position(latLng);
            markerOptions.title("Current Location");

            if (type.equals("none"))
            {
                end_longitude= longitude;
                end_latitude = latitude;
            }
        }
        else
        {
            latLng = new LatLng(end_latitude, end_longitude); //get destination lat and lng
            markerOptions = new MarkerOptions(); //setmarker to previous destination

            markerOptions.position(latLng);
            markerOptions.title("Previous Destination");

        }




        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentLocationmMarker = mMap.addMarker(markerOptions);

        ///move camera to that location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }



        if (!type.equals("none"))
        {

            B_generic(lastWord);

        }
        else
        {
            toEntry entry = new toEntry(0 , new LatLng(latitude, longitude), "currentLocation" );
            int index = repeated("currentLocation");
            if (index == -1) {
                MainActivity.toList.add(entry);
            }
            else
                MainActivity.toList.set(index, entry);
            lastWord = "destination";

        }

    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);

    }
}
