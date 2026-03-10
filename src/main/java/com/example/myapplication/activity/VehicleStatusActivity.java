package com.example.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Vehicle;
import com.example.myapplication.structures.AVLTree;
import com.example.myapplication.structures.AVLTreeIterator;
import com.google.firebase.database.*;

/**
 * VehicleStatusActivity allows users to:
 * - Search for public vehicle records using license plates
 * - Browse all indexed public vehicles
 * - View registration, maintenance, and theft status
 * - Share a report of a found vehicle
 *
 * @author u7763790 - Vrushabh Vijay Dhoke
 * @author u8030355 - Shane George Shibu
 */
public class VehicleStatusActivity extends AppCompatActivity {

    public static AVLTree avlTree;

    private EditText searchBar;
    private TextView vehicleName, licensePlate, vinText;
    private ImageView registrationStatusIcon, lastMaintenanceIcon, stolenStatusIcon;
    private TextView registrationStatusText, lastMaintenanceText, stolenStatusText;
    private Button checkDatabaseButton, shareReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_status);
        avlTree = new AVLTree();
        setupUI();
        loadVehiclesFromFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        avlTree = new AVLTree();
        loadVehiclesFromFirebase();
    }

    private void setupUI() {
        searchBar = findViewById(R.id.search_bar);
        ImageView searchIcon = findViewById(R.id.search_icon);

        vehicleName = findViewById(R.id.vehicle_name);
        licensePlate = findViewById(R.id.license_plate);
        vinText = findViewById(R.id.vin);

        LinearLayout registrationStatusLayout = findViewById(R.id.registration_status);
        registrationStatusIcon = (ImageView) registrationStatusLayout.getChildAt(0);
        registrationStatusText = (TextView) registrationStatusLayout.getChildAt(1);

        LinearLayout lastMaintenanceLayout = findViewById(R.id.last_maintenance);
        lastMaintenanceIcon = (ImageView) lastMaintenanceLayout.getChildAt(0);
        lastMaintenanceText = (TextView) lastMaintenanceLayout.getChildAt(1);

        LinearLayout stolenStatusLayout = findViewById(R.id.stolen_status);
        stolenStatusIcon = (ImageView) stolenStatusLayout.getChildAt(0);
        stolenStatusText = (TextView) stolenStatusLayout.getChildAt(1);

        checkDatabaseButton = findViewById(R.id.check_database_button);
        shareReportButton = findViewById(R.id.share_report_button);

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                searchVehicle();
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> {
            searchVehicle();
            Log.d("VEHICLE_LOG", "Search icon clicked");
        });

        checkDatabaseButton.setOnClickListener(v -> {
            Log.d("VEHICLE_LOG", "Check Database button clicked");
            StringBuilder result = new StringBuilder();
            AVLTreeIterator iterator = new AVLTreeIterator(avlTree);

            if (!iterator.hasNext()) {
                result.append("No public vehicles available.");
            } else {
                while (iterator.hasNext()) {
                    Vehicle vehicle = iterator.next();
                    result.append("• ").append(vehicle.getVehicleName())
                            .append(" (").append(vehicle.getLicensePlate()).append(")\n");
                }
            }

            new AlertDialog.Builder(VehicleStatusActivity.this)
                    .setTitle("All Public Vehicles")
                    .setMessage(result.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });


        shareReportButton.setOnClickListener(v -> {
            Log.d("VEHICLE_LOG", "Share Report button clicked");
            shareReport();
        });
    }

    private void loadVehiclesFromFirebase() {
        DatabaseReference publicVehicleRef = FirebaseDatabase.getInstance().getReference("public_vehicle");

        publicVehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(VehicleStatusActivity.this, "No public vehicle data found", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot vehicleSnap : snapshot.getChildren()) {
                    Vehicle vehicle = vehicleSnap.getValue(Vehicle.class);
                    if (vehicle != null && "Public".equalsIgnoreCase(vehicle.getVisibility())) {
                        avlTree.insert(vehicle);
                    }
                }

                Log.d("VEHICLE_LOG", "Loaded AVL tree from Firebase");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VehicleStatusActivity.this, "Failed to load vehicle data", Toast.LENGTH_SHORT).show();
                Log.e("VEHICLE_LOG", "Firebase load error: " + error.getMessage());
            }
        });
    }

    private void searchVehicle() {
        String licensePlateQuery = searchBar.getText().toString().trim();
        if (licensePlateQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a License Plate to search", Toast.LENGTH_SHORT).show();
            Log.d("VEHICLE_LOG", "Search aborted: Empty license plate input");
            return;
        }

        Vehicle foundVehicle = avlTree.searchByLicensePlate(licensePlateQuery);
        if (foundVehicle == null) {
            Toast.makeText(this, "No vehicle found for this License Plate", Toast.LENGTH_SHORT).show();
            Log.d("VEHICLE_LOG", "No match found for: " + licensePlateQuery);
            clearVehicleDetails();
        } else {
            Log.d("VEHICLE_LOG", "Match found for: " + licensePlateQuery);
            populateVehicleDetails(foundVehicle);
        }
    }

    private void populateVehicleDetails(Vehicle vehicle) {
        vehicleName.setText(vehicle.getVehicleName());
        licensePlate.setText("License Plate: " + vehicle.getLicensePlate());
        vinText.setText("VIN: " + vehicle.getVin());

        if ("VALID".equalsIgnoreCase(vehicle.getRegistrationStatus())) {
            registrationStatusText.setText("VALID\nExpires on " + vehicle.getRegistrationExpiry());
            registrationStatusText.setTextColor(Color.GREEN);
            registrationStatusIcon.setImageResource(android.R.drawable.btn_star_big_on);
            registrationStatusIcon.setColorFilter(Color.GREEN);
        } else {
            registrationStatusText.setText("EXPIRED\nExpired on " + vehicle.getRegistrationExpiry());
            registrationStatusText.setTextColor(Color.RED);
            registrationStatusIcon.setImageResource(android.R.drawable.stat_notify_error);
            registrationStatusIcon.setColorFilter(Color.RED);
        }

        if ("VALID".equalsIgnoreCase(vehicle.getLastMaintenanceStatus())) {
            lastMaintenanceText.setText("VALID\n" + vehicle.getLastMaintenance());
            lastMaintenanceText.setTextColor(Color.GREEN);
            lastMaintenanceIcon.setImageResource(android.R.drawable.stat_sys_data_bluetooth);
            lastMaintenanceIcon.setColorFilter(Color.GREEN);
        } else {
            lastMaintenanceText.setText("INVALID\n" + vehicle.getLastMaintenance());
            lastMaintenanceText.setTextColor(Color.RED);
            lastMaintenanceIcon.setImageResource(android.R.drawable.stat_notify_error);
            lastMaintenanceIcon.setColorFilter(Color.RED);
        }

        if ("NOT STOLEN".equalsIgnoreCase(vehicle.getStolenStatus())) {
            stolenStatusText.setText("NOT STOLEN\n" + vehicle.getLastKnownLocation());
            stolenStatusText.setTextColor(Color.GREEN);
            stolenStatusIcon.setImageResource(android.R.drawable.btn_star_big_on);
            stolenStatusIcon.setColorFilter(Color.GREEN);
        } else {
            stolenStatusText.setText("REPORTED STOLEN\n" + vehicle.getLastKnownLocation());
            stolenStatusText.setTextColor(Color.parseColor("#FFA500")); // Orange
            stolenStatusIcon.setImageResource(android.R.drawable.stat_sys_warning);
            stolenStatusIcon.setColorFilter(Color.parseColor("#FFA500"));
        }
    }

    private void clearVehicleDetails() {
        vehicleName.setText("-");
        licensePlate.setText("-");
        vinText.setText("-");
        registrationStatusText.setText("-");
        lastMaintenanceText.setText("-");
        stolenStatusText.setText("-");
    }

    private void shareReport() {
        String report = "Vehicle Status Report\n"
                + "Vehicle: " + vehicleName.getText() + "\n"
                + "License Plate: " + licensePlate.getText() + "\n"
                + "VIN: " + vinText.getText() + "\n"
                + "Registration Status: " + registrationStatusText.getText() + "\n"
                + "Last Maintenance: " + lastMaintenanceText.getText() + "\n"
                + "Stolen Status: " + stolenStatusText.getText();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, report);
        startActivity(Intent.createChooser(shareIntent, "Share Vehicle Status Report"));
    }
}
