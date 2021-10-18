package com.example.authapp3.boundary;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.APIClient;
import com.example.authapp3.control.DirectionFinder;
import com.example.authapp3.control.DirectionFinderListener;
import com.example.authapp3.control.GoogleMapAPI;
import com.example.authapp3.entity.EVChargingLocation;
import com.example.authapp3.entity.EVChargingPrice;
import com.example.authapp3.entity.PlaceResult;
import com.example.authapp3.entity.PlaceResults;
import com.example.authapp3.entity.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchLocation extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private EditText mSearchText;
    View mapView;

    private List<EVChargingLocation> evChargingLocationList = new ArrayList<>();
    private List<Marker> evStationMarkerList = new ArrayList<>(), evRentalMarkerList = new ArrayList<>();
    private Marker tempMarker;
    private List<EVChargingPrice> evChargingPriceList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private AlertDialog dialog;
    private int close1, close2;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker destinationMarker;
    LocationRequest mLocationRequest;
    private static Address address;
    private FloatingActionButton showEVChargingBtn, showEVRentalBtn, routeTo;
    private boolean showEVCharging = true, showEVRental = true, doubleClickMarker = false, initialiseMap = true;

    private final int PROXIMITY_RADIUS = 500;
    private String evModel;
    private double distancePerCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mSearchText = (EditText) findViewById(R.id.editTextTextPostalAddress);

        mDatabase = FirebaseDatabase.getInstance().getReference("EVChargingStations");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < evStationMarkerList.size(); i++) {
                    evStationMarkerList.get(i).remove();
                }
                evStationMarkerList.clear();
                evChargingPriceList.clear();
                evChargingLocationList.clear();
                DataSnapshot location = snapshot.child("Location");
                DataSnapshot price = snapshot.child("Price");
                for (DataSnapshot postPrice : price.getChildren())
                {
                    EVChargingPrice evChargingPrice = postPrice.getValue(EVChargingPrice.class);
                    evChargingPriceList.add(evChargingPrice);
                }
                for (DataSnapshot postLocation : location.getChildren()) {
                    EVChargingLocation evChargingLocation = postLocation.getValue(EVChargingLocation.class);
                    evChargingLocationList.add(evChargingLocation);
                }
                for (int y = 0; y < evChargingLocationList.size(); y++) {
                    LatLng temp = new LatLng(evChargingLocationList.get(y).getLatitude(), evChargingLocationList.get(y).getLongitude());
                    String companyPrice = "";
                    String company = evChargingLocationList.get(y).getCompany();

                    for (int x = 0; x < evChargingPriceList.size(); x++) {
                        if (company.equals(evChargingPriceList.get(x).getCompanyName())) {
                            companyPrice = evChargingPriceList.get(x).getPrice();
                        }
                    }
                    String title = evChargingLocationList.get(y).getStationName();
                    String snippet = "Company: " + company + "\nPrice: " + companyPrice + "\nClick Me For Nearby Amenities";
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(temp).title(title).snippet(snippet);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evstation",100 , 100)));
                    tempMarker = mMap.addMarker(markerOptions);
                    tempMarker.setVisible(!showEVCharging);
                    evStationMarkerList.add(tempMarker);

                    if (evChargingLocationList.get(y).isHasRental()) {
                        snippet = "Company: " + company + "\nClick Me For Nearby Amenities";
                        markerOptions.snippet(snippet);
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("bluecar",100 , 100)));
                        tempMarker = mMap.addMarker(markerOptions);
                        tempMarker.setVisible(!showEVRental);
                        evRentalMarkerList.add(tempMarker);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        showEVChargingBtn = findViewById(R.id.showEVChargingBtn);
        showEVChargingBtn.setOnClickListener(view -> {
            for (int i = 0; i < evStationMarkerList.size(); i++) {
                evStationMarkerList.get(i).setVisible(showEVCharging);
            }
            if (showEVCharging) {
                showEVChargingBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00CC00")));
            } else {
                showEVChargingBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            }
            showEVCharging = !showEVCharging;
        });
        showEVRentalBtn = findViewById(R.id.showEVRentalBtn);
        showEVRentalBtn.setOnClickListener(view -> {
            for (int i = 0; i < evRentalMarkerList.size(); i++) {
                evRentalMarkerList.get(i).setVisible(showEVRental);
            }
            if (showEVRental) {
                showEVRentalBtn.setImageTintList(ColorStateList.valueOf(Color.BLUE));
            } else {
                showEVRentalBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            }
            showEVRental = !showEVRental;
        });
        evModel = getIntent().getStringExtra("evModel");
        DatabaseReference evReference = FirebaseDatabase.getInstance().getReference("EVModels");
        if (evModel != null) {
            evReference.child("DistancePerCharge").child(evModel).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    distancePerCharge = snapshot.getValue(Double.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else { distancePerCharge = 1;
        }

        /*Transition*/
        Transition exitTrans = new Fade();
        exitTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setExitTransition(exitTrans);

        Transition reenterTrans = new Fade();
        reenterTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setReenterTransition(reenterTrans);




        /*BOTTOM NAVIGATION BAR*/

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent = new Intent(SearchLocation.this, HomePage.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchLocation.this,bottomNavigationView ,"BottomBar");
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.ic_navigate:
                        break;
                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(SearchLocation.this, ProfileActivity.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(SearchLocation.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(SearchLocation.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(SearchLocation.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent2.putExtra("evModel",(String) evModel);
                        startActivity(intent2, options2.toBundle());
                        break;
                }
                return false;
            }
        });
        /*BOTTOM NAV BAR END*/
    }
    public Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });
    }

// TODO (Done): FOR MC you can use this getAddress to get searched location

    private void geoLocate(){
        Log.d(TAG,"geoLocate: geolocationg");{

            String searchString = mSearchText.getText().toString();

            Geocoder geocoder = new Geocoder(SearchLocation.this);
            List<Address> list = new ArrayList<>();
            try{
                list = geocoder.getFromLocationName(searchString, 1);

            }catch (IOException e){
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
            }

            if(list.size() > 0){
                Address address = list.get(0);

                Log.d(TAG, "geoLocate: found a location: " + address.toString());
                //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
                this.address = address;

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                if (destinationMarker != null) {
                    destinationMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.snippet(address.getAddressLine(0));

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                destinationMarker = mMap.addMarker(markerOptions);
                Log.d(TAG,"Placeinfo: Place address: "+ Place.Field.ADDRESS);
                setMarkerTitle(searchString, destinationMarker);
                /*Working on bit above - Ray*/
                /*will be removed DO NOT REMOVE-MC*/
                routeTo = (FloatingActionButton) findViewById(R.id.routeTo);
                routeTo.setVisibility(View.VISIBLE);
                routeTo.setOnClickListener(view -> {
                    String location = ((TextView) findViewById(R.id.locationInfo)).getText().toString();

                    AlertDialog.Builder diaglogBuilder = new AlertDialog.Builder(this);
                    final View dropDownView = getLayoutInflater().inflate(R.layout.popup_scrolldownlayout, null);

                    ((TextView) dropDownView.findViewById(R.id.destTitle)).setText(destinationMarker.getTitle());
                    ((TextView) dropDownView.findViewById(R.id.destAddress)).setText(destinationMarker.getSnippet());
                    sendRequest(location, destinationMarker,1,dropDownView);
                    close1 = 0;
                    close2 = 0;
                    double close1dist = Double.POSITIVE_INFINITY, close2dist = Double.POSITIVE_INFINITY;
                    for (int i = 0; i < evChargingLocationList.size(); i++) {
                        double lat_a = destinationMarker.getPosition().latitude;
                        double lng_a = destinationMarker.getPosition().longitude;
                        double lat_b = evChargingLocationList.get(i).getLatitude();
                        double lng_b = evChargingLocationList.get(i).getLongitude();
                        double distance = getDistance(lat_a,lng_a,lat_b,lng_b);
                        if (distance < close1dist) {
                            close2 = close1;
                            close1 = i;
                            close2dist = close1dist;
                            close1dist = distance;
                        } else if (distance < close2dist) {
                            close2 = i;
                            close2dist = distance;
                        }
                    }
                    ((TextView) dropDownView.findViewById(R.id.ev1Title)).setText(evChargingLocationList.get(close1).getStationName());
                    ((TextView) dropDownView.findViewById(R.id.ev1Address)).setText(evChargingLocationList.get(close1).getAddress());
                    sendRequest(location,evStationMarkerList.get(close1),2,dropDownView);
                    ((TextView) dropDownView.findViewById(R.id.ev2Title)).setText(evChargingLocationList.get(close2).getStationName());
                    ((TextView) dropDownView.findViewById(R.id.ev2Address)).setText(evChargingLocationList.get(close2).getAddress());
                    sendRequest(location,evStationMarkerList.get(close2),3,dropDownView);

                    Button ev1Button = dropDownView.findViewById(R.id.ev1Button);
                    ev1Button.setOnClickListener(view12 -> startNavigation(close1));
                    Button ev2Button = dropDownView.findViewById(R.id.ev2Button);
                    ev2Button.setOnClickListener(view1 -> startNavigation(close2));

                    diaglogBuilder.setView(dropDownView);
                    dialog = diaglogBuilder.create();
                    dialog.show();
                });

            } else {
                Toast.makeText(this, this.getResources().getString(R.string.no_search_result), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String reverseGeoLocate(Marker marker){
        Geocoder geocoder = new Geocoder(SearchLocation.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        String address = "";
        if(list.size() > 0){
            address = list.get(0).getAddressLine(0);
        }
        return address;
    }

    private String reverseGeoLocate(String location) {
        String[] locations = location.split(",", 2);
        double lat = Double.parseDouble(locations[0]);
        double lng = Double.parseDouble(locations[1]);
        Geocoder geocoder = new Geocoder(SearchLocation.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        String address = "";
        if(list.size() > 0){
            address = list.get(0).getAddressLine(0);
        }
        return address;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        /* To move the mylocation button*/
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }



        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        /*
        //To set Padding, Might be useful later on
        int sizeInDp = 150;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        googleMap.setPadding(0, dpAsPixels, 0, 0); // Might use eventually*/

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        init();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View infoWindow = SearchLocation.this.getLayoutInflater().inflate(R.layout.infowindow_layout, null);

                TextView infoWindowTitle = infoWindow.findViewById(R.id.infoWIndowStationName);
                TextView infoWindowSnippet = infoWindow.findViewById(R.id.infoWindowSnippet);

                infoWindowTitle.setText(marker.getTitle());
                infoWindowSnippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(SearchLocation.this, nearbyEV.class);
            intent.putExtra("markerLat", ((double) marker.getPosition().latitude));
            intent.putExtra("markerLong", ((double) marker.getPosition().longitude));
            startActivity(intent);
        });
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        mLastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        ((TextView) findViewById(R.id.locationInfo)).setText(lat + "," + lng);

        if (initialiseMap) {
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            new Handler().postDelayed(() -> {
                initialiseMap = false;
            }, 4000);
        }

        /*
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        */
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed.", Toast.LENGTH_LONG).show();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    //search function

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyB4jNfDzwogpxS5Q3lZKNdVlGzA_ipiOCQ");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private void sendRequest(String currentMarker, Marker destinationMarker, int option, View view) {
        String origin = reverseGeoLocate(currentMarker);
        String destination = reverseGeoLocate(destinationMarker);
        try {
            new DirectionFinder(this, origin, destination, option, view).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes, int option, View view) {
        for (Route route : routes) {
            switch (option) {
                case 1:
                    ((TextView) view.findViewById(R.id.destDistance)).setText(route.distance.getText());
                    ((TextView) view.findViewById(R.id.destCharge)).setText(getEstimateChargeString(route.distance.getText()));
                    ((TextView) view.findViewById(R.id.destTime)).setText(route.duration.getText());
                    break;
                case 2:
                    ((TextView) view.findViewById(R.id.ev1Distance)).setText(route.distance.getText());
                    ((TextView) view.findViewById(R.id.ev1Charge)).setText(getEstimateChargeString(route.distance.getText()));
                    ((TextView) view.findViewById(R.id.ev1Time)).setText(route.duration.getText());
                    break;
                case 3:
                    ((TextView) view.findViewById(R.id.ev2Distance)).setText(route.distance.getText());
                    ((TextView) view.findViewById(R.id.ev2Charge)).setText(getEstimateChargeString(route.distance.getText()));
                    ((TextView) view.findViewById(R.id.ev2Time)).setText(route.duration.getText());
                    break;
            }
        }
    }

    private String getEstimateChargeString(String distanceString) {
        Double distance, estimatedCharge;
        String[] distanceSplit;

        distanceSplit = distanceString.split(" ", 2);
        distance = Double.parseDouble(distanceSplit[0]);
        if (distanceSplit[1].equals("km")) {
            estimatedCharge = distance / distancePerCharge;
        } else {
            estimatedCharge = (distance / 1000) / distancePerCharge;
        }
        BigDecimal estimatedChargeTruncated = new BigDecimal(String.valueOf(estimatedCharge)).setScale(2, BigDecimal.ROUND_FLOOR);
        return estimatedChargeTruncated.toString() + "%";
    }

    public double getDistance (double lat_a, double lng_a, double lat_b, double lng_b ) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;
        int meterConversion = 1609;
        return distance * meterConversion;
    }

    private void setMarkerTitle(String address, Marker marker) {
        String key = getText(R.string.google_maps_key).toString();
        String inputtype = "textquery";
        String fields = "geometry,icon,name,photos,formatted_address,place_id";

        GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
        googleMapAPI.findPlace(address, inputtype,  fields, key).enqueue(new Callback<PlaceResults>() {
            @Override
            public void onResponse(Call<PlaceResults> call, retrofit2.Response<PlaceResults> response) {
                if (response.isSuccessful()) {
                    List<PlaceResult> results = response.body().getResults();
                    marker.setTitle(results.get(0).getName());
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PlaceResults> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startNavigation(int x) {
        Double lat = evChargingLocationList.get(x).getLatitude();
        Double lng = evChargingLocationList.get(x).getLongitude();
        String coordinates = lat.toString() + "," + lng.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+coordinates+"&mode=d"));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}


