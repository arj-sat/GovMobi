package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.License;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This activity displays the list of licenses for the currently logged-in user.
 * License data is read from a locally stored JSON file (`temp_userdata.json`)
 * and each license is shown as a card with basic info like ID, type, and status.
 * Users can tap on a license card to view more detailed information.
 *
 * License objects are parsed from the JSON file and displayed dynamically using
 * `LayoutInflater`. The app supports color-coded status and shows different
 * views based on data state.
 *
 * @author Janvi Rajendra Nandre
 */
public class MyLicensesActivity extends AppCompatActivity {

    private LinearLayout licenseListContainer;
    private EditText searchBar;
    private List<License> allLicenses = new ArrayList<>();
    private List<Map<String, Object>> licenseRawData = new ArrayList<>();

    private static final String TAG = "LICENSE_LOG";

    /**
     * Initializes the license screen and sets up the search bar and license list.
     *
     * @param savedInstanceState Bundle for restoring state if applicable.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_licenses);

        licenseListContainer = findViewById(R.id.licenses_container);
//        searchBar = findViewById(R.id.search_bar);

        // Attach live search filtering
//        searchBar.addTextChangedListener(new android.text.TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterLicenses(s.toString());
//            }
//            @Override public void afterTextChanged(android.text.Editable s) {}
//        });

        loadLicensesFromLocal();
    }

    /**
     * Loads license data from the local temp JSON file and parses it into License objects.
     * Also stores raw maps for use when passing detailed data.
     */
    private void loadLicensesFromLocal() {
        try {
            FileInputStream fis = openFileInput("temp_userdata.json");
            InputStreamReader reader = new InputStreamReader(fis);
            Map<String, Object> userData = new Gson().fromJson(reader, Map.class);
            reader.close();

            Map<String, Object> licensesRaw = (Map<String, Object>) userData.get("licenses");
            allLicenses.clear();
            licenseRawData.clear();

            for (Map.Entry<String, Object> entry : licensesRaw.entrySet()) {
                Map<String, Object> licenseMap = (Map<String, Object>) entry.getValue();

                License license = new License();
                license.setLicenseId(entry.getKey());
                license.setLicenseType((String) licenseMap.get("licenseType"));

                Object rawLicenseNumber = licenseMap.get("licenseNumber");
                String licenseNumber = rawLicenseNumber instanceof Number
                        ? String.format("%.0f", ((Number) rawLicenseNumber).doubleValue())
                        : String.valueOf(rawLicenseNumber);
                license.setLicenseNumber(licenseNumber);

                license.setExpiryDate((String) licenseMap.get("expiryDate"));
                license.setStatus((String) licenseMap.get("status"));

                allLicenses.add(license);
                licenseRawData.add(licenseMap);
            }

            populateLicenseCards(allLicenses);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load licenses", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error reading temp_userdata.json: " + e.getMessage());
        }
    }

    /**
     * Displays each license as a card in the UI.
     * Clicking a card opens the detail screen with full data.
     *
     * @param licenses The list of licenses to display.
     */
    private void populateLicenseCards(List<License> licenses) {
        licenseListContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < licenses.size(); i++) {
            License license = licenses.get(i);
            Map<String, Object> raw = licenseRawData.get(i);

            View card = inflater.inflate(R.layout.item_license_card, licenseListContainer, false);

            TextView type = card.findViewById(R.id.license_type);
            TextView licenseId = card.findViewById(R.id.license_id);
            TextView status = card.findViewById(R.id.license_status);
            ImageView icon = card.findViewById(R.id.license_icon);
            Button btnDelete = card.findViewById(R.id.btn_delete);

            type.setText(license.getLicenseType());
            licenseId.setText("ID: " + license.getLicenseNumber());
            status.setText(license.getStatus());

            if ("active".equalsIgnoreCase(license.getStatus())) {
                status.setTextColor(0xFF008000); // green
            } else {
                status.setTextColor(0xFFFF0000); // red
            }

            icon.setImageResource(R.drawable.ic_license);
            btnDelete.setVisibility(View.GONE); // Reserved for future delete feature

            // Navigate to License Detail screen
            card.setOnClickListener(v -> {
                String licenseJson = new Gson().toJson(raw);
                Intent intent = new Intent(MyLicensesActivity.this, LicenseDetailActivity.class);
                intent.putExtra("license_json", licenseJson);
                startActivity(intent);
            });

            licenseListContainer.addView(card);
        }
    }

    /**
     * Filters the license list based on the search query and refreshes the displayed cards.
     *
     * @param query The search text entered by the user.
     */
    private void filterLicenses(String query) {
        List<License> filtered = new ArrayList<>();
        List<Map<String, Object>> filteredRaw = new ArrayList<>();

        for (int i = 0; i < allLicenses.size(); i++) {
            License l = allLicenses.get(i);
            if (l.getLicenseType().toLowerCase().contains(query.toLowerCase()) ||
                    l.getLicenseNumber().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(l);
                filteredRaw.add(licenseRawData.get(i));
            }
        }

        // Update displayed list
        licenseRawData = filteredRaw;
        populateLicenseCards(filtered);
    }
}
