package com.example.myapplication.util;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for resetting test account data in Firebase.
 * This class provides functionality to reset both license and vehicle data for test@gmail.com,
 * maintaining a consistent test environment with:
 * - 2 licenses (Provisional 2 and Full License)
 * - 3 vehicles (BMW, MINI, and BYD)
 *
 * @author u7884012 - Ruoheng Feng
 */
public class ResetTestAccount {

    /** Tag used for logging reset operations */
    private static final String TAG = "ResetTestAccount";

    /**
     * Resets the test account data in Firebase.
     * Performs the following operations:
     * 1. Authenticates with Firebase using test account credentials
     * 2. Resets the license data (2 licenses with different types and statuses)
     * 3. Resets the vehicle data (3 vehicles with different visibility settings)
     * 
     * The method handles both successful and failed login attempts,
     * logging appropriate messages for each case.
     */
    public static void runReset() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("test@gmail.com", "test123")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) return;

                        String uid = user.getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference("Userdata")
                                .child(uid);

                        // ===== Reset licenses =====
                        DatabaseReference licensesRef = userRef.child("licenses");
                        licensesRef.removeValue().addOnSuccessListener(aVoid -> {
                            Map<String, Object> license1 = new HashMap<>();
                            license1.put("address", "123 Main Street");
                            license1.put("dob", "01/01/2000");
                            license1.put("expiryDate", "01-01-2027");
                            license1.put("issueDate", "01-01-2024");
                            license1.put("licenseNumber", "1234567890");
                            license1.put("licenseType", "Provisional 2");
                            license1.put("name", "TestUser");
                            license1.put("status", "active");
                            license1.put("vehicleType", "LMV");

                            Map<String, Object> license2 = new HashMap<>();
                            license2.put("address", "456 Second Ave");
                            license2.put("dob", "05/06/1995");
                            license2.put("expiryDate", "15-08-2028");
                            license2.put("issueDate", "15-08-2023");
                            license2.put("licenseNumber", "9876543210");
                            license2.put("licenseType", "Full License");
                            license2.put("name", "TestUser");
                            license2.put("status", "inactive");
                            license2.put("vehicleType", "HMV");

                            Map<String, Object> newLicenses = new HashMap<>();
                            newLicenses.put("license1", license1);
                            newLicenses.put("license2", license2);

                            licensesRef.updateChildren(newLicenses);
                            Log.d(TAG, "Licenses updated");
                        });

                        // ===== Reset vehicles =====
                        DatabaseReference vehiclesRef = userRef.child("vehicles");
                        vehiclesRef.removeValue().addOnSuccessListener(aVoid -> {
                            Map<String, Object> vehicle1 = new HashMap<>();
                            vehicle1.put("vehicleName", "BMW");
                            vehicle1.put("licensePlate", "BMW-123");
                            vehicle1.put("vin", "98765678");
                            vehicle1.put("registrationStatus", "VALID");
                            vehicle1.put("registrationExpiry", "12-12-2025");
                            vehicle1.put("lastMaintenance", "01-05-2024");
                            vehicle1.put("lastMaintenanceStatus", "VALID");
                            vehicle1.put("stolenStatus", "NOT STOLEN");
                            vehicle1.put("lastKnownLocation", "Google Building 43, 43 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
                            vehicle1.put("visibility", "Public");

                            Map<String, Object> vehicle2 = new HashMap<>();
                            vehicle2.put("vehicleName", "MINI");
                            vehicle2.put("licensePlate", "TYS-975");
                            vehicle2.put("vin", "483294809");
                            vehicle2.put("registrationStatus", "VALID");
                            vehicle2.put("registrationExpiry", "12-12-2025");
                            vehicle2.put("lastMaintenance", "01-05-2024");
                            vehicle2.put("lastMaintenanceStatus", "VALID");
                            vehicle2.put("stolenStatus", "NOT STOLEN");
                            vehicle2.put("lastKnownLocation", "Google Building 43, 43 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
                            vehicle2.put("visibility", "Private");

                            Map<String, Object> vehicle3 = new HashMap<>();
                            vehicle3.put("vehicleName", "BYD");
                            vehicle3.put("licensePlate", "BYD-782");
                            vehicle3.put("vin", "8312093");
                            vehicle3.put("registrationStatus", "VALID");
                            vehicle3.put("registrationExpiry", "12-12-2025");
                            vehicle3.put("lastMaintenance", "01-05-2024");
                            vehicle3.put("lastMaintenanceStatus", "VALID");
                            vehicle3.put("stolenStatus", "NOT STOLEN");
                            vehicle3.put("lastKnownLocation", "Google Building 43, 43 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
                            vehicle3.put("visibility", "Public");

                            // Firebase push creates unique keys
                            vehiclesRef.push().setValue(vehicle1);
                            vehiclesRef.push().setValue(vehicle2);
                            vehiclesRef.push().setValue(vehicle3);

                            Log.d(TAG, "Vehicles reset successfully");
                        });

                    } else {
                        Log.e(TAG, "Login failed: " + task.getException());
                    }
                });
    }
}
