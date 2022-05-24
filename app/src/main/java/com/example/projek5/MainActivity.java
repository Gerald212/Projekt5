package com.example.projek5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart, buttonStop;
    private TextView textStatus, textLat, textLng, textAddress, textInfo;

    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    //public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 300;
    public static final int DEFAULT_UPDATE_INTERVAL_LONG = 30;
    //public static final int FAST_UPDATE_INTERVAL_LONG = 15;
    public static final int FAST_UPDATE_INTERVAL_LONG = 3600;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    LocationRequest locationRequest;
    LocationRequest locationRequestLong;
    LocationCallback locationCallBack;
    LocationCallback locationCallBackLong;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStop = (Button)findViewById(R.id.buttonStop);
        textStatus = (TextView)findViewById(R.id.textViewStatus);
        textLat = (TextView)findViewById(R.id.textViewLat);
        textLng = (TextView)findViewById(R.id.textViewLng);
        textAddress = (TextView)findViewById(R.id.textViewAddress);
        textInfo = (TextView)findViewById(R.id.textViewInfo);


        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //chyba nie
        locationRequestLong = new LocationRequest();
        locationRequestLong.setInterval(1000 * DEFAULT_UPDATE_INTERVAL_LONG);
        locationRequestLong.setFastestInterval(1000 * FAST_UPDATE_INTERVAL_LONG);
        locationRequestLong.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequestLong.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //event that is triggerd whenever the update interval is met.
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save the location
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
                Log.i("tag", "callback (co 5 min) - " + location.getLatitude() + " " + location.getLongitude());
            }
        };

        locationCallBackLong = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save the location
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
                Log.i("tag", "callback z opoznieniem (co 1 godz)");
            }
        };

        updateGPS();
    }

    private void updateUIValues(Location location) {
        textLat.setText("Lat: " + String.valueOf(location.getLatitude()));
        textLng.setText("Lng: " + String.valueOf(location.getLongitude()));
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            textAddress.setText("Adres: " + addresses.get(0).getAddressLine(0));
        }
        catch(Exception e){
            textAddress.setText("Adres: -");
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);
                }
            });
        }
        else{
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FINE_LOCATION);
                //Log.e("cccc", String.valueOf(Manifest.permission.ACCESS_FINE_LOCATION)  );
            }
        }
    }

    private void startLocationUpdates() {
        textStatus.setText("Śledzenie włączone");
        textInfo.setText("");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        fusedLocationProviderClient.requestLocationUpdates(locationRequestLong, locationCallBackLong, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        textStatus.setText("Śledzenie wyłączone");
        textInfo.setText("Ostatnia lokalizacja:");
        //textLat.setText("Lat: -");
        //textLng.setText("Lng: -");
        //textAddress.setText("Adres: -");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    public void buttonStartOnClick(View view){
        //textStatus.setText("Śledzenie włączone");
        startLocationUpdates();
    }

    public void buttonStopOnClick(View view){
        //textStatus.setText("Śledzenie wyłączone");
        stopLocationUpdates();
    }

}