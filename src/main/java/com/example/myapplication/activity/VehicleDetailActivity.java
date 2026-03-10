package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.util.LocalDataUtil;
import com.example.myapplication.util.FirebaseUserUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying detailed information about a vehicle.
 * This activity provides a detailed view of vehicle information in a table format,
 * including features for:
 * - Displaying vehicle properties in a formatted table
 * - Deleting vehicles from both local storage and Firebase
 * - Synchronizing changes with the server
 *
 * @author u7884012 - Ruoheng Feng
 */
public class VehicleDetailActivity extends AppCompatActivity {

    /**
     * Initializes the activity and sets up the vehicle detail view.
     * Creates a table layout to display vehicle information and sets up the delete button.
     * The vehicle data is received as a JSON string from the intent and displayed in a formatted table.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                          this Bundle contains the data it most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        TableLayout table = findViewById(R.id.vehicle_detail_table);
        String json = getIntent().getStringExtra("vehicle_json");

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> vehicleMap = new Gson().fromJson(json, mapType);

        // Updated list of keys to match Vehicle.java model properties
        List<String> orderedKeys = Arrays.asList(
                "vehicleName",
                "licensePlate",
                "vin",
                "registrationStatus",
                "registrationExpiry",
                "lastMaintenance",
                "lastMaintenanceStatus",
                "stolenStatus",
                "lastKnownLocation",
                "visibility"
        );

        for (String key : orderedKeys) {
            if (!vehicleMap.containsKey(key)) continue;

            TableRow row = new TableRow(this);

            TextView keyView = new TextView(this);
            // Format the key for better display (e.g., "vehicleName" -> "Vehicle Name")
            keyView.setText(formatKeyName(key));
            keyView.setTextSize(16f);
            keyView.setTextColor(Color.DKGRAY);
            keyView.setPadding(8, 8, 16, 8);

            TextView valueView = new TextView(this);
            valueView.setText(formatValue(vehicleMap.get(key)));
            valueView.setTextSize(16f);
            valueView.setTextColor(Color.BLACK);
            valueView.setPadding(8, 8, 8, 8);

            row.addView(keyView);
            row.addView(valueView);
            table.addView(row);

            TableRow divider = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 1);
            params.span = 2;
            View dividerLine = new View(this);
            dividerLine.setLayoutParams(params);
            dividerLine.setBackgroundColor(Color.LTGRAY);
            divider.addView(dividerLine);
            table.addView(divider);
        }

        Button btnDelete = findViewById(R.id.btn_delete_vehicle);
        btnDelete.setOnClickListener(v -> {
            String plateToDelete = String.valueOf(vehicleMap.get("licensePlate"));

            Map<String, Object> fullData = LocalDataUtil.loadUserMapFromTempJson(this);
            if (fullData != null && fullData.containsKey("vehicles")) {
                Map<String, Object> vehicles = (Map<String, Object>) fullData.get("vehicles");

                String keyToRemove = null;
                for (Map.Entry<String, Object> entry : vehicles.entrySet()) {
                    Map<String, Object> data = (Map<String, Object>) entry.getValue();
                    if (plateToDelete.equals(String.valueOf(data.get("licensePlate")))) {
                        keyToRemove = entry.getKey();
                        break;
                    }
                }

                if (keyToRemove != null) {
                    vehicles.remove(keyToRemove);
                    LocalDataUtil.saveUserMapToTempJson(this, fullData);
                    FirebaseUserUtil.uploadUserData(this, fullData, () -> {
                        Toast.makeText(this, "Vehicle deleted and synced", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                } else {
                    Toast.makeText(this, "Vehicle not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Formats a camelCase key name into a Title Case string with spaces.
     * For example, "vehicleName" becomes "Vehicle Name".
     *
     * @param key The camelCase key to be formatted
     * @return The formatted string in Title Case with spaces
     */
    private String formatKeyName(String key) {
        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(key.charAt(0)));

        for (int i = 1; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(' ').append(c);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Formats a value for display in the table.
     * Handles number formatting to remove decimal places for whole numbers.
     *
     * @param value The value to be formatted
     * @return The formatted string representation of the value
     */
    private String formatValue(Object value) {
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            if (Math.floor(d) == d) return String.format("%.0f", d);
        }
        return String.valueOf(value);
    }
}