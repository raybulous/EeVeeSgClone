package com.example.authapp3.boundary;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.authapp3.R;
import com.example.authapp3.entity.EV;
import com.example.authapp3.entity.EVChargingLocation;
import com.example.authapp3.entity.EVChargingPrice;
import com.example.authapp3.entity.Route;
import com.example.authapp3.control.DirectionFinder;
import com.example.authapp3.control.DirectionFinderListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Navigate extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath, btnNearby;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseUser evLocation;
    private List<Address> list;
    private List<LatLng> latlng;
    //private MarkerOptions options = new MarkerOptions();
    private List<String> addressList = new ArrayList<>();
    private List<String> stationNameList = new ArrayList<>();
    private List<String> companyList = new ArrayList<>();
    private Address address;
    private List<EVChargingLocation> evChargingLocationList = new ArrayList<>();
    private List<Marker> evStationMarkerList = new ArrayList<>();
    private Marker tempMarker;
    private List<EVChargingPrice> evChargingPriceList = new ArrayList<>();

    //BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.evstation);
   /* private Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.evstation);
    private Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);*/

    public Bitmap resizeBitmap(String drawableName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        btnNearby = (Button) findViewById(R.id.btnNearby);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(v -> sendRequest());

        btnNearby.setOnClickListener(view -> startActivity(new Intent(Navigate.this, nearby.class)));

        // To Set EV Markers
        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("EVChargingStations");
        databaseReference.child("0").child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address1 = (String)snapshot.getValue();
                Log.d(TAG,"This is my address" + address1);
                Log.d(TAG, "THIS IS ADDRESS");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        mDatabase = FirebaseDatabase.getInstance().getReference("EVChargingStations");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iteration method would not clear all the markers - can consider.
                mMap.clear(); // If value is changed - Remove everything to have real live updates.
                evStationMarkerList.clear();
                evChargingPriceList.clear();
                evChargingLocationList.clear();
                DataSnapshot location = snapshot.child("Location");
                DataSnapshot price = snapshot.child("Price");
                for (DataSnapshot postp : price.getChildren())
                {
                    EVChargingPrice evChargingPrice = postp.getValue(EVChargingPrice.class);
                    evChargingPriceList.add(evChargingPrice);
                }

                for (DataSnapshot post : location.getChildren()) {
                    EVChargingLocation evChargingLocation = post.getValue(EVChargingLocation.class);
                    evChargingLocationList.add(evChargingLocation);
                }
                Geocoder geocoder = new Geocoder(Navigate.this);

                for (int y = 0; y < evChargingLocationList.size(); y++) {
                    LatLng temp = new LatLng(evChargingLocationList.get(y).getLatitude(), evChargingLocationList.get(y).getLongitude());
                    //position.add(temp);
                    String Cprice = "";
                    String company = evChargingLocationList.get(y).getCompany();



                    for (int x = 0; x < evChargingPriceList.size(); x++)
                    {
                        if (company.equals(evChargingPriceList.get(x).getCompanyName())) {
                            Cprice = evChargingPriceList.get(x).getPrice();

                        }

                    }
                    String snippet = company + " " + Cprice;
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(temp).title(evChargingLocationList.get(y).getStationName()).snippet(snippet);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evstation",100 , 100)));
                    tempMarker = mMap.addMarker(markerOptions);
                    evStationMarkerList.add(tempMarker);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
                        Intent intent = new Intent(Navigate.this, HomePage.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Navigate.this,bottomNavigationView ,"BottomBar");
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.ic_navigate:
                        break;
                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(Navigate.this, ProfileActivity.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(Navigate.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(Navigate.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(Navigate.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2, options2.toBundle());
                        break;
                }

                return false;
            }
        });



        /*BOTTOM NAV BAR END*/
    }
/*
    private void geoLocate( String text ){
        Log.d(TAG,"geoLocate: geolocationg");{

            String searchString = text;

            Geocoder geocoder = new Geocoder(Navigate.this);
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

                *//*LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                Log.d(TAG,"Placeinfo: Place address: "+ Place.Field.ADDRESS);

                nearbybtn = (Button) findViewById(R.id.nearbybtn);
                nearbybtn.setOnClickListener(view -> {
                    Intent intent = new Intent(SearchLocation.this, nearbyDestination.class);
                    startActivity(intent);
                });
                *//*

            }
        }
    }*/
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }

    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng hougangmall = new LatLng(1.3726, 103.8937);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hougangmall, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Hougang Mall")
                .position(hougangmall)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
        "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.getText());
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.getText());

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}