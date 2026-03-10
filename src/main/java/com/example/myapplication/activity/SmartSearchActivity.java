package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.License;
import com.example.myapplication.model.SimpleVehicle;
import com.example.myapplication.parser.*;
import com.example.myapplication.util.LocalDataUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * SmartSearchActivity enables users to search across instructions, licenses, and vehicle details.
 * The input query is parsed into structured search types using a tokenizer and parser.
 * Results are displayed in a list with the ability to navigate to detailed views.
 * Users can filter results dynamically using category buttons.
 *
 * Search supports:
 * - Vehicle rego-based lookups
 * - License queries
 * - Instructional FAQs
 *
 * Data is loaded from local cache (`temp_userdata.json`) and assets (`instructions.json`).
 *
 * @author u7884012 - Ruoheng Feng
 * @author u8030355 - Shane George Shibu
 */
public class SmartSearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchInput;
    private Button searchButton, clearButton;
    private Button filterVehiclesButton, filterLicensesButton, filterInstructionsButton;
    private ListView resultListView;
    private ArrayAdapter<String> suggestionAdapter;

    private static Map<String, String> instructionMap = new HashMap<>();
    private List<String> suggestionsList;
    private Map<String, SimpleVehicle> localVehicleMap = new HashMap<>();
    private List<License> localLicenseList = new ArrayList<>();

    private String activeFilter = null; // Can be "Vehicles", "Licenses", "Instructions", or null for all

    /**
     * Initializes the SmartSearch UI, loads local data, and sets up listeners.
     *
     * @param savedInstanceState State bundle from system restoration, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this layout is correct
        setContentView(R.layout.activity_smart_search); // Ensure layout is activity_main.xml
        TextView searchHintText = findViewById(R.id.searchHintText);
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        resultListView = findViewById(R.id.resultListView);
        filterVehiclesButton = findViewById(R.id.filterVehiclesButton);
        filterLicensesButton = findViewById(R.id.filterLicensesButton);
        filterInstructionsButton = findViewById(R.id.filterInstructionsButton);

        // Filter button toggling logic
        View.OnClickListener filterClickListener = v -> {
            Button clicked = (Button) v;
            String type = clicked.getText().toString();
            activeFilter = type.equals(activeFilter) ? null : type;
            updateFilterButtonStates();
            searchButton.performClick(); // Trigger auto search on filter change
        };

        filterVehiclesButton.setOnClickListener(filterClickListener);
        filterLicensesButton.setOnClickListener(filterClickListener);
        filterInstructionsButton.setOnClickListener(filterClickListener);

        updateFilterButtonStates();

        // Load instruction dataset
        if (instructionMap.isEmpty()) {
            instructionMap = loadInstructionsFromAssets(this);
        }

        // Setup autocomplete
        suggestionsList = new ArrayList<>(instructionMap.keySet());
        suggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suggestionsList);
        searchInput.setAdapter(suggestionAdapter);

        loadLocalData();

        /**
         * Handles search execution logic.
         * Parses input into tokens and queries, applies active filters, and updates result list.
         */
        searchButton.setOnClickListener(v -> {
            String input = searchInput.getText().toString().trim().toLowerCase();
            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter a query", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> resultItems = new ArrayList<>();
            List<String> tokens = Tokenizer.tokenize(input);
            List<Query> queries = Parser.suggestQueries(tokens, localVehicleMap.keySet());

            Set<String> regos = new HashSet<>();
            boolean foundLicenseQuery = false;

            for (Query q : queries) {
                if ("license_info".equalsIgnoreCase(q.getType())) {
                    foundLicenseQuery = true;
                } else if (q.getTarget() != null) {
                    regos.add(q.getTarget());
                }
            }

            // Instructions
            if ((activeFilter == null || "Instructions".equals(activeFilter)) && Parser.isIntroRequest(input)) {
                resultItems.addAll(prefixWith("Instruction", instructionMap.keySet()));
            } else if (activeFilter == null || "Instructions".equals(activeFilter)) {
                resultItems.addAll(prefixWith("Instruction", searchInstructionKeywords(input)));
            }

            // Vehicles
            if (activeFilter == null || "Vehicles".equals(activeFilter)) {
                for (String rego : regos) {
                    resultItems.add("[Vehicle] View details for " + rego);
                }
            }

            // Licenses
            if (foundLicenseQuery && (activeFilter == null || "Licenses".equals(activeFilter))) {
                for (License license : localLicenseList) {
                    resultItems.add("[License] View details: " + license.getLicenseType());
                }
            }

            if (resultItems.isEmpty()) {
                resultItems.add("No results found.");
                searchHintText.setVisibility(View.VISIBLE);
            } else {
                searchHintText.setVisibility(View.GONE);
            }

            resultListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultItems));
            resultListView.setVisibility(View.VISIBLE);

            // Handle click actions for each result type
            resultListView.setOnItemClickListener((adapterView, view1, i, l) -> {
                String selected = (String) adapterView.getItemAtPosition(i);
                handleResultClick(selected);
            });
        });

        clearButton.setOnClickListener(v -> {
            searchInput.setText("");
            resultListView.setVisibility(View.GONE);
        });
    }

    /**
     * Highlights the selected filter button and unselects others.
     */
    private void updateFilterButtonStates() {
        filterVehiclesButton.setSelected("Vehicles".equals(activeFilter));
        filterLicensesButton.setSelected("Licenses".equals(activeFilter));
        filterInstructionsButton.setSelected("Instructions".equals(activeFilter));
    }

    /**
     * Loads instructions from `assets/instructions.json`.
     *
     * @param context Current app context.
     * @return Map of keyword-instruction pairs.
     */
    private Map<String, String> loadInstructionsFromAssets(Context context) {
        Map<String, String> instructions = new HashMap<>();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("instructions.json")));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                instructions.put(key.toLowerCase(), jsonObject.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instructions;
    }

    /**
     * Searches instruction keys for fuzzy matches.
     *
     * @param input Search query.
     * @return List of matching keywords.
     */
    private List<String> searchInstructionKeywords(String input) {
        List<String> matches = new ArrayList<>();
        for (String key : instructionMap.keySet()) {
            if (key.contains(input)) {
                matches.add(key);
            }
        }
        return matches;
    }

    /**
     * Displays a simple dialog with instruction content.
     *
     * @param title   Title of the dialog.
     * @param message Content of the instruction.
     */
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Loads local cached vehicle and license data from `temp_userdata.json`.
     */
    private void loadLocalData() {
        try {
            List<SimpleVehicle> vehicles = LocalDataUtil.loadSimpleVehicles(this);
            for (SimpleVehicle v : vehicles) {
                localVehicleMap.put(v.getRego().toUpperCase(), v);
            }

            localLicenseList = LocalDataUtil.loadLicenses(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles click actions for a given result item.
     *
     * @param selected The clicked item string.
     */
    private void handleResultClick(String selected) {
        if (selected.startsWith("[Instruction] ")) {
            String keyword = selected.replace("[Instruction] ", "").trim();
            showDialog("Instruction", instructionMap.getOrDefault(keyword.toLowerCase(), "No content."));
        } else if (selected.startsWith("[Vehicle] View details for ")) {
            String rego = selected.replace("[Vehicle] View details for ", "").trim().toUpperCase();
            Map<String, Object> fullData = LocalDataUtil.loadUserMapFromTempJson(this);
            if (fullData != null && fullData.containsKey("vehicles")) {
                Map<String, Object> vehicles = (Map<String, Object>) fullData.get("vehicles");
                for (Map.Entry<String, Object> entry : vehicles.entrySet()) {
                    Map<String, Object> vehicleMap = (Map<String, Object>) entry.getValue();
                    if (rego.equalsIgnoreCase(String.valueOf(vehicleMap.get("licensePlate")))) {
                        String fullJson = new Gson().toJson(vehicleMap);
                        Intent intent = new Intent(SmartSearchActivity.this, VehicleDetailActivity.class);
                        intent.putExtra("vehicle_json", fullJson);
                        startActivity(intent);
                        return;
                    }
                }
            }
            Toast.makeText(this, "Vehicle not found", Toast.LENGTH_SHORT).show();

        } else if (selected.startsWith("[License] View details")) {
            if (!localLicenseList.isEmpty()) {
                String licenseNumber = localLicenseList.get(0).getLicenseNumber();
                Map<String, Object> fullData = LocalDataUtil.loadUserMapFromTempJson(this);
                if (fullData != null && fullData.containsKey("licenses")) {
                    Map<String, Object> licenses = (Map<String, Object>) fullData.get("licenses");
                    for (Map.Entry<String, Object> entry : licenses.entrySet()) {
                        Map<String, Object> licenseMap = (Map<String, Object>) entry.getValue();
                        String number = formatLicenseNumber(licenseMap.get("licenseNumber"));
                        if (licenseNumber.equals(number)) {
                            String fullJson = new Gson().toJson(licenseMap);
                            Intent intent = new Intent(SmartSearchActivity.this, LicenseDetailActivity.class);
                            intent.putExtra("license_json", fullJson);
                            startActivity(intent);
                            return;
                        }
                    }
                }
                Toast.makeText(this, "License not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, selected, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Converts raw license number to string, removing decimals if necessary.
     */
    private String formatLicenseNumber(Object raw) {
        if (raw instanceof Number) {
            double d = ((Number) raw).doubleValue();
            return String.format("%.0f", d);
        } else {
            return String.valueOf(raw);
        }
    }

    /**
     * Utility to prefix strings with a label like [Instruction].
     */
    private List<String> prefixWith(String label, Collection<String> items) {
        List<String> result = new ArrayList<>();
        for (String item : items) {
            result.add("[" + label + "] " + item);
        }
        return result;
    }
}
