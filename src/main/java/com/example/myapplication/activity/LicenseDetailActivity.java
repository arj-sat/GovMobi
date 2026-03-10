package com.example.myapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserUtil;
import com.example.myapplication.util.LocalDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying detailed information about a license.
 * This activity provides a detailed view of license information in a table format,
 * including features for:
 * - Displaying license properties in a formatted table
 * - Deleting licenses from both local storage and Firebase
 * - Synchronizing changes with the server
 *
 * @author u7884012 - Ruoheng Feng
 */
public class LicenseDetailActivity extends AppCompatActivity {

    /**
     * Initializes the activity and sets up the license detail view.
     * Creates a table layout to display license information and sets up the delete button.
     * The license data is received as a JSON string from the intent and displayed in a formatted table.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                          this Bundle contains the data it most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_detail);

        TableLayout table = findViewById(R.id.license_detail_table);
        String json = getIntent().getStringExtra("license_json");

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> licenseMap = new Gson().fromJson(json, mapType);

        // List of license properties in the desired display order
        List<String> orderedKeys = Arrays.asList(
                "licenseNumber",
                "licenseType",
                "vehicleType",
                "name",
                "dob",
                "address",
                "issueDate",
                "expiryDate",
                "status"
        );

        for (String key : orderedKeys) {
            if (!licenseMap.containsKey(key)) continue;

            TableRow row = new TableRow(this);

            TextView keyView = new TextView(this);
            keyView.setText(key);
            keyView.setTextSize(16f);
            keyView.setTextColor(Color.DKGRAY);
            keyView.setPadding(8, 8, 16, 8);

            TextView valueView = new TextView(this);
            valueView.setText(formatValue(licenseMap.get(key)));
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
            android.view.View dividerLine = new android.view.View(this);
            dividerLine.setLayoutParams(params);
            dividerLine.setBackgroundColor(Color.LTGRAY);
            divider.addView(dividerLine);
            table.addView(divider);
        }

        Button btnDelete = findViewById(R.id.btn_delete_license);
        btnDelete.setOnClickListener(v -> {
            String licenseNumberToDelete = String.valueOf(licenseMap.get("licenseNumber"));

            Map<String, Object> fullData = LocalDataUtil.loadUserMapFromTempJson(this);
            if (fullData != null && fullData.containsKey("licenses")) {
                Map<String, Object> licenses = (Map<String, Object>) fullData.get("licenses");

                String keyToRemove = null;
                for (Map.Entry<String, Object> entry : licenses.entrySet()) {
                    Map<String, Object> license = (Map<String, Object>) entry.getValue();
                    if (licenseNumberToDelete.equals(String.valueOf(license.get("licenseNumber")))) {
                        keyToRemove = entry.getKey();
                        break;
                    }
                }

                if (keyToRemove != null) {
                    licenses.remove(keyToRemove);
                    LocalDataUtil.saveUserMapToTempJson(this, fullData);
                    FirebaseUserUtil.uploadUserData(this, fullData, () -> {
                        Toast.makeText(this, "License deleted and synced", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                } else {
                    Toast.makeText(this, "License not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            double num = ((Number) value).doubleValue();
            if (Math.floor(num) == num) {
                return String.format("%.0f", num);
            }
        }
        return String.valueOf(value);
    }
}
