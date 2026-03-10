package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserUtil;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.location.Geocoder;
import android.location.Address;
import java.util.List;
import java.util.Locale;

/**
 * Activity to allow users to add a new vehicle with details.
 * Uses GPS to retrieve current address and store it as the vehicle's last known location.
 * Stores vehicle info under the current user's Firebase node and optionally under the public vehicle list.
 *
 * @author u7763790 - Vrushabh Vijay Dhoke
 * @author u7991805 -Janvi Rajendra Nandre
 */
public class AddVehicleActivity extends AppCompatActivity {

    private EditText etVehicleName, etLicensePlate, etVin;
    private RadioGroup radioGroupVisibility;
    private Button btnSave;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentLocation = "Unknown";

    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private static final String TAG = "ADD_VEHICLE";

    /**
     * Initializes UI and location services, and sets up button click listeners.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!isLocationServiceEnabled()) {
            Toast.makeText(this, "Please enable Location Services (GPS) in settings", Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        etVehicleName = findViewById(R.id.etVehicleName);
        etLicensePlate = findViewById(R.id.etLicensePlate);
        etVin = findViewById(R.id.etVin);
        radioGroupVisibility = findViewById(R.id.radioGroupVisibility);
        btnSave = findViewById(R.id.btnSaveVehicle);

        btnSave.setOnClickListener(v -> saveVehicle());
    }

    /**
     * Requests location permission if not already granted.
     * @author u7991805 -Janvi Rajendra Nandre
     */
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }
    }

    /**
     * Retrieves current address using GPS coordinates.
     * Updates the `currentLocation` string with a human-readable address.
     * @author u7991805 -Janvi Rajendra Nandre
     */
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (!isLocationServiceEnabled()) {
            Toast.makeText(this, "Please enable GPS in Settings", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                new CancellationTokenSource().getToken()
        ).addOnSuccessListener(location -> {
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        currentLocation = address.getAddressLine(0);
                    } else {
                        currentLocation = "Location unavailable";
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Geocoder exception: " + e.getMessage());
                    currentLocation = "Location error";
                }
            } else {
                Log.w(TAG, "Current location is null");
                currentLocation = "Location not available";
            }

            Log.d(TAG, "Resolved Location: " + currentLocation);
        });
    }

    /**
     * Callback for location permission result.
     *  @author u7991805 -Janvi Rajendra Nandre
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isLocationServiceEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    /**
     * Saves the vehicle information to Firebase under the current user.
     * Also stores in the public vehicle list if marked "Public".
     */
    private void saveVehicle() {
        String name = etVehicleName.getText().toString().trim();
        String plate = etLicensePlate.getText().toString().trim();
        String vin = etVin.getText().toString().trim();

        if (name.isEmpty() || plate.isEmpty() || vin.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("Unknown".equals(currentLocation) || currentLocation.contains("Location")) {
            Toast.makeText(this, "Please wait while we retrieve your current location", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference vehicleRef = FirebaseDatabase.getInstance()
                .getReference("Userdata")
                .child(user.getUid())
                .child("vehicles");

        String vehicleId = vehicleRef.push().getKey();

        Map<String, Object> vehicleMap = new HashMap<>();
        vehicleMap.put("vehicleName", name);
        vehicleMap.put("licensePlate", plate);
        vehicleMap.put("vin", vin);
        vehicleMap.put("registrationStatus", "VALID");
        vehicleMap.put("registrationExpiry", "12-12-2025");
        vehicleMap.put("lastMaintenance", "01-05-2024");
        vehicleMap.put("lastMaintenanceStatus", "VALID");
        vehicleMap.put("stolenStatus", "NOT STOLEN");
        vehicleMap.put("lastKnownLocation", currentLocation);

        int selectedVisibilityId = radioGroupVisibility.getCheckedRadioButtonId();
        String visibility = (selectedVisibilityId == R.id.radioPublic) ? "Public" : "Private";
        vehicleMap.put("visibility", visibility);

        // Get selected visibility from radio group

        if (vehicleId != null) {
            vehicleRef.child(vehicleId).setValue(vehicleMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Vehicle added successfully", Toast.LENGTH_SHORT).show();

                        if ("Public".equalsIgnoreCase(visibility)) {
                            DatabaseReference publicRef = FirebaseDatabase.getInstance()
                                    .getReference("public_vehicle")
                                    .child(vehicleId);
                            publicRef.setValue(vehicleMap)
                                    .addOnSuccessListener(aVoid1 -> Log.d(TAG, "Added to public_vehicle"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Public add failed: " + e.getMessage()));
                        }

                        FirebaseUserUtil.fetchAndSaveUserData(this, user, () -> {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("refresh", true);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Firebase error: " + e.getMessage());
                    });
        }
    }
}
