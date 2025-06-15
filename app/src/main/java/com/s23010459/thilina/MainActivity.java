package com.s23010459.thilina;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull; // Keep this if you plan to use @NonNull annotations
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException; // Import IOException
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;

    private Button btnShowLocation;
    private EditText addressInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainConstrainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_internal_fragment);
        mapFragment.getMapAsync(this);


        addressInput = findViewById(R.id.addressInput);
        btnShowLocation = findViewById(R.id.btnShowLocation);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputLocation = addressInput.getText().toString().trim(); // Get text and trim space

                if (userInputLocation.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (myMap == null) {
                    Toast.makeText(MainActivity.this, "Map is not ready yet", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed if the map isn't initialized
                }

                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addressList;

                try {

                    addressList = geocoder.getFromLocationName(userInputLocation, 1);

                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                        myMap.clear(); // Clear previous markers
                        myMap.addMarker(new MarkerOptions().position(location).title(userInputLocation));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f)); // camera smooth zoom

                    } else {
                        Toast.makeText(MainActivity.this, "Address not found. Please try another one.", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) { // Catch Exception
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error finding location. Check your network connection or the address.", Toast.LENGTH_LONG).show();
                } catch (IllegalArgumentException e) { // Catch if address is null or empty
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Invalid address provided.", Toast.LENGTH_LONG).show();
                }
            }
        });



        //Navigation for the Temp check screen
        Button toAlert = findViewById(R.id.btnToAlert);
        toAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login button click
                Intent intent = new Intent(MainActivity.this, TempAlert.class);
                startActivity(intent);
                //disable the back stack
                //finish();
            }
        });
        //




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        //Set an initial location and zoom level for the map
        LatLng initialLocation = new LatLng(0, 0); // defalut map view
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 2.0f)); // Low zoom to show a large area
        myMap.getUiSettings().setZoomControlsEnabled(true);

        //prompt the user to search
        Toast.makeText(this, "Enter an address to search.", Toast.LENGTH_SHORT).show();
    }
}