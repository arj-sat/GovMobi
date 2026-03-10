package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Vehicle;
import com.example.myapplication.util.FirebaseUserUtil;
import com.example.myapplication.util.LocalDataUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * MyVehiclesActivity displays a list of vehicles associated with the current user.
 * Vehicles are loaded from locally cached JSON and shown as cards.
 * Users can view details, add new vehicles, or (optionally) delete them.
 *
 * Clicking a vehicle opens VehicleDetailActivity. The add button opens AddVehicleActivity.
 *
 * Deletion is implemented but commented out in the UI for future enablement.
 *
 * @author u7763790 - Vrushabh
 */
public class MyVehiclesActivity extends AppCompatActivity {

    private LinearLayout vehicleListContainer;
    //private EditText searchBar;
    private Button btnAddVehicle;
    private List<Vehicle> allVehicles = new ArrayList<>();

    /**
     * Initializes the UI, sets click listeners, and loads vehicles.
     *
     * @param savedInstanceState Previous state if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);

        vehicleListContainer = findViewById(R.id.vehicle_list_container);
        //searchBar = findViewById(R.id.search_bar);
        btnAddVehicle = findViewById(R.id.btn_add_vehicle);

        btnAddVehicle.setOnClickListener(v -> {
            startActivity(new Intent(this, AddVehicleActivity.class));
        });

        loadVehicles();
    }

    /**
     * Loads vehicles from the local JSON cache and displays them.
     */
    private void loadVehicles() {
        allVehicles = LocalDataUtil.loadVehiclesFromTempJson(this);
        populateVehicleCards(allVehicles);
    }

    /**
     * Populates the vehicle card views based on a list of vehicles.
     * Each card is clickable to view detailed information.
     *
     * @param vehicles List of Vehicle objects to display.
     */
    private void populateVehicleCards(List<Vehicle> vehicles) {
        vehicleListContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int index = 0; index < vehicles.size(); index++) {
            Vehicle vehicle = vehicles.get(index);
            int currentIndex = index;

            // Filter out unknown visibility types
            if (!"Public".equalsIgnoreCase(vehicle.getVisibility()) &&
                    !"Private".equalsIgnoreCase(vehicle.getVisibility())) {
                continue;
            }

            View card = inflater.inflate(R.layout.item_vehicle_card, vehicleListContainer, false);

            TextView plate = card.findViewById(R.id.text_plate);
            TextView model = card.findViewById(R.id.text_model);
            TextView status = card.findViewById(R.id.text_status);
            ImageView icon = card.findViewById(R.id.image_vehicle);
//            Button btnDelete = card.findViewById(R.id.btn_delete);

            plate.setText(vehicle.getLicensePlate());
            model.setText(vehicle.getVehicleName());
            String visibility = vehicle.getVisibility();
            status.setText(visibility);

            if ("Public".equalsIgnoreCase(vehicle.getVisibility())) {
                status.setTextColor(0xFF008000); // Green
            } else {
                status.setTextColor(0xFFFFA500); // Orange
            }

            icon.setImageResource(R.drawable.ic_vehicle);

//            btnDelete.setOnClickListener(v -> deleteVehicleAt(currentIndex));

            card.setOnClickListener(v -> {
                String vehicleJson = new Gson().toJson(vehicle);
                Intent intent = new Intent(MyVehiclesActivity.this, VehicleDetailActivity.class);
                intent.putExtra("vehicle_json", vehicleJson);
                startActivity(intent);
            });

            vehicleListContainer.addView(card);
        }
    }

    /**
     * Deletes a vehicle from Firebase based on its position in the list.
     * On success, refreshes local cache and UI.
     *
     * @param position Index of the vehicle to be deleted in allVehicles list.
     */
    private void deleteVehicleAt(int position) {
        if (position >= 0 && position < allVehicles.size()) {
            Vehicle vehicleToDelete = allVehicles.get(position);
            String regoToDelete = vehicleToDelete.getLicensePlate();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = user.getUid();
            DatabaseReference vehiclesRef = FirebaseDatabase.getInstance()
                    .getReference("Userdata").child(uid).child("vehicles");

            vehiclesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot vehicleSnap : snapshot.getChildren()) {
                        Vehicle v = vehicleSnap.getValue(Vehicle.class);
                        if (v != null && regoToDelete.equals(v.getLicensePlate())) {
                            vehicleSnap.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(MyVehiclesActivity.this, "Vehicle deleted", Toast.LENGTH_SHORT).show();

                                        // Refresh user data from Firebase
                                        FirebaseUserUtil.fetchAndSaveUserData(MyVehiclesActivity.this, user, () -> {
                                            loadVehicles();
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MyVehiclesActivity.this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                            return;
                        }
                    }
                    Toast.makeText(MyVehiclesActivity.this, "Vehicle not found", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyVehiclesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
