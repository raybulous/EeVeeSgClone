package com.example.authapp3.boundary;

import android.Manifest;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.authapp3.R;
import com.example.authapp3.control.APIClient;
import com.example.authapp3.control.GoogleMapAPI;
import com.example.authapp3.control.PlacesListAdapter;
import com.example.authapp3.entity.PlacesResults;
import com.example.authapp3.entity.PlacesResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class nearbyEV extends AppCompatActivity {

    private ListView listViewPlaces;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        initView();
    }
    private void initView() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 1, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }

        listViewPlaces = findViewById(R.id.listViewPlaces);

    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            String key = getText(R.string.google_maps_key).toString();
            double Lat = getIntent().getDoubleExtra("markerLat",0);
            String Lat_string = Double.toString(Lat);
            double Long = getIntent().getDoubleExtra("markerLong", 0);
            String Long_string = Double.toString(Long);
            String nearbyEVLocation = Lat_string  + "," + Long_string;
            int radius = 3000;

            GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
            googleMapAPI.getNearBy(nearbyEVLocation, radius,  "tourist_attraction", key).enqueue(new Callback<PlacesResults>() {
                @Override
                public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                    if (response.isSuccessful()) {
                        List<PlacesResult> placesResults = response.body().getResults();
                        PlacesListAdapter placesListAdapter = new PlacesListAdapter(getApplicationContext(), placesResults);
                        listViewPlaces.setAdapter(placesListAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PlacesResults> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}