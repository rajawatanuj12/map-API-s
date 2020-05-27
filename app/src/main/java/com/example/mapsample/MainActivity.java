/** Q 1.)Create an android app and show the latitude and longitude of user's entered location by using google location api.
 * Also show the physical address in the map and check all necessary run time permission.
 */

package com.example.mapsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button last,current;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = findViewById(R.id.button);
        last = findViewById(R.id.button2);
        Button get = findViewById(R.id.getlocation);

        requestPermissions();

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            TextView tV = findViewById(R.id.textView);
                            tV.setText(location.toString());
                        }

                    }
                });
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                        MapsActivity.LatLngFetched = latLng;
                        startActivity(intent);
                    }
                }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getLocation();

                    }
                });
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
            });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,200,this);
    }

    @Override
    public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            MapsActivity.LatLngFetched = latLng;
            startActivity(intent);
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
}
