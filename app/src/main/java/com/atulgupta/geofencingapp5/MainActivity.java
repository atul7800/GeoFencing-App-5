package com.atulgupta.geofencingapp5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location deviceCurrentLocation;
    private LocationRequest locationRequest;
    private double latitude, longitude;
    LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //permission granted
            //getting the device location
            getTheDeviceLastLocation();
        } else {
            //permission is not granted
            //asking for the permission
            askLocationPermission();
        }
    }



    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permission is not granted so request for the permission
            //requesting for the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //requesting for the permission by showing a dialog box
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("This app needs Location access permission to work properly, please grant Location access permission")
                        .setPositiveButton("OK, Grant permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1111);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .show();

                Log.d(TAG, "Permission granted 1");

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1111);
                Log.d(TAG, "Permission granted 2");
            }

        } else {
            // permission is already granted so no action needed
            Log.d(TAG, "no action needed");

        }

    }



    private void getTheDeviceLastLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null)
                {
                  //we have a location
                    deviceCurrentLocation = location;

                  Log.d(TAG, "onSuccess : Latitude  = " + location.getLatitude());
                  Log.d(TAG, "onSuccess : Longitude = " + location.getLongitude());

                } else {
                    Log.d(TAG, "Opps! Location null..");
                }

            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure : " + e.getLocalizedMessage());
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1111)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //permission is granted
                getTheDeviceLastLocation();

            } else {
                //permission is not granted hence we are requesting for permission again
                //asking for permission
                askLocationPermission();
            }
        }
    }







}
