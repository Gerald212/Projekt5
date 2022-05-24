package com.example.projek5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart, buttonStop;
    private TextView textStatus;


    LocationRequest locationRequest;
    LocationCallback locationCallBack;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        textStatus = findViewById(R.id.textViewStatus);
    }

    public void buttonStartOnClick(View view){
        textStatus.setText("Śledzenie włączone");
    }

    public void buttonStopOnClick(View view){
        textStatus.setText("Śledzenie wyłączone");
    }

}