package com.example.projek5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart, buttonStop;
    private TextView textStatus, textLat, textLng, textAddress;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;
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


        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //event that is triggerd whenever the update interval is met.
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save the location
                Location location = locationResult.getLastLocation();
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
        };
    }

    public void buttonStartOnClick(View view){
        textStatus.setText("Śledzenie włączone");
    }

    public void buttonStopOnClick(View view){
        textStatus.setText("Śledzenie wyłączone");
    }

}