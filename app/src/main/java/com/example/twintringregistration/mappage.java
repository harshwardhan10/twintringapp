package com.example.twintringregistration;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class mappage extends AppCompatActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Chronometer cmTimer;

    LocationManager locationManager;
    private GoogleMap mMap;
    LatLng latLng1, latLng2;
    int i=0, time, timegap, prevtime;
    float [] results = new float[3];
    float totaldist = 0;
    float avg, curr1, curr2;
    EditText distance, avgSpeed, currSpeed2;
    Button start, stop, reset;
    LocationListener locationListener;

    Boolean resume = false;
    long elapsedTime;
    String TAG = "TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappage);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(mappage.this);

        cmTimer = findViewById(R.id.cmTimer);

        reset = findViewById(R.id.reset);
        reset.setVisibility(View.INVISIBLE);

        distance = findViewById(R.id.distance);
        distance.setVisibility(View.INVISIBLE);

        avgSpeed = findViewById(R.id.avgSpeed);
        avgSpeed.setVisibility(View.INVISIBLE);

        currSpeed2 = findViewById(R.id.currSpeed2);
        currSpeed2.setVisibility(View.INVISIBLE);

        stop = findViewById(R.id.stop);
        stop.setVisibility(View.INVISIBLE);

        start = findViewById(R.id.start);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    time = ((int)minutes * 60) + (int)seconds;

                    DecimalFormat df = new DecimalFormat("0.00");


                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;

                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.INVISIBLE);
                distance.setVisibility(View.VISIBLE);
                avgSpeed.setVisibility(View.VISIBLE);
                currSpeed2.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                totaldist = 0;
                fetchLastLocation();
                start.setEnabled(false);
                stop.setEnabled(true);

                if (!resume) {
                    cmTimer.setBase(SystemClock.elapsedRealtime());
                    cmTimer.start();
                } else {
                    cmTimer.start();
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setVisibility(View.INVISIBLE);
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null)
                        {
                            currentLocation = location;
                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                            supportMapFragment.getMapAsync(mappage.this);
                        }

                    }
                });
                mMap.addMarker(new MarkerOptions().position(latLng1).title("Your route ends here"));
                endGPS();
                reset.setVisibility(View.VISIBLE);
                start.setEnabled(true);
                stop.setEnabled(false);
                cmTimer.stop();
//                cmTimer.setText("");
                resume = true;
                start.setText("Start");
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                i=0;
                reset.setVisibility(View.INVISIBLE);
                distance.setVisibility(View.INVISIBLE);
                avgSpeed.setVisibility(View.INVISIBLE);
                currSpeed2.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                cmTimer.stop();
                cmTimer.setText("00:00");
                resume = false;
                stop.setEnabled(false);
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                while (i == 0){

                    latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                    i=1;
                }
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng2 = new LatLng(latitude, longitude);
                Polyline line = mMap.addPolyline(new PolylineOptions().add(latLng1, latLng2).width(20).color(Color.BLACK));

                Location.distanceBetween(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude, results);
                totaldist += results[0];

                DecimalFormat df = new DecimalFormat("0.00");
                timegap = time - prevtime;
                avg =  (totaldist/(float)time) * 3.6f;
                curr2 = (results[0]/(float)timegap) * 3.6f;
                avgSpeed.setText(df.format(avg)+ "km/hr");
                currSpeed2.setText(df.format(curr2)+ "km/hr");
                prevtime = time;
                String disText = " Dist- " + df.format(totaldist) + "m";
                distance.setText(disText);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude,longitude, 1);
                    String str = addressList.get(0).getLocality()+ ",";
                    str += addressList.get(0).getCountryName();
                    while (i == 1) {
                        mMap.addMarker(new MarkerOptions().position(latLng2).title(str));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 16));
                            }
                        }, 1000);

                        i = 2;
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));

                    latLng1 = latLng2;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
//
//    public void onClick(View v) {
//        switch(v.getId()) {
//            case R.id.start:
//                start.setEnabled(false);
//                stop.setEnabled(true);
//
//                if (!resume) {
//                    cmTimer.setBase(SystemClock.elapsedRealtime());
//                    cmTimer.start();
//                } else {
//                    cmTimer.start();
//                }
//                break;
//
//            case R.id.stop:
//                start.setEnabled(true);
//                stop.setEnabled(false);
//                cmTimer.stop();
//                cmTimer.setText("");
//                resume = true;
//                start.setText("Start");
//                break;
//
//            case R.id.reset:
//                cmTimer.stop();
//                cmTimer.setText("00:00");
//                resume = false;
//                stop.setEnabled(false);
//                break;
//        }
//    }




    private void fetchLastLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }



        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 22, locationListener);
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000 , 22, locationListener);
        }


    }

    public void endGPS(){
        try {
            locationManager.removeUpdates(locationListener);
            locationManager=null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

//    public void time (){
//        String secs, mins, timE;
//        timE = (String) cmTimer.getText();
//
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You Are Here");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
//        googleMap.addMarker(markerOptions);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case REQUEST_CODE:
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    fetchLastLocation();
//                }
//                break;
//        }
//    }
}
