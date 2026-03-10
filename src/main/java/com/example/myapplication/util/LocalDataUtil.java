package com.example.myapplication.util;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.model.License;
import com.example.myapplication.model.SimpleVehicle;
import com.example.myapplication.model.Vehicle;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;

/**
 * Utility class for managing local data storage and retrieval.
 * This class provides methods for handling JSON-based local storage of user data,
 * including vehicles and licenses. It supports operations for:
 * - Loading vehicle data from local JSON storage
 * - Loading user data maps
 * - Converting vehicle data to simplified formats
 * - Managing license information
 * - Saving user data to local storage
 *
 * @author u7884012 - Ruoheng Feng
 */
public class LocalDataUtil {

    /**
     * Loads vehicle data from the local temporary JSON file.
     * Reads vehicle information from 'temp_userdata.json' and converts it to Vehicle objects.
     *
     * @param context The application context used for file operations
     * @return List of Vehicle objects loaded from the local file, empty list if file doesn't exist or error occurs
     */
    public static List<Vehicle> loadVehiclesFromTempJson(Context context) {
        List<Vehicle> vehicleList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), "temp_userdata.json");
            if (!file.exists()) {
                Log.w("LocalDataUtil", "temp_userdata.json not found.");
                return vehicleList;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
            reader.close();

            JsonObject jsonObject = JsonParser.parseString(builder.toString()).getAsJsonObject();
            JsonObject vehiclesJson = jsonObject.getAsJsonObject("vehicles");

            if (vehiclesJson != null) {
                for (Map.Entry<String, JsonElement> entry : vehiclesJson.entrySet()) {
                    Vehicle vehicle = new Gson().fromJson(entry.getValue(), Vehicle.class);
                    vehicleList.add(vehicle);
                }
            }
        } catch (Exception e) {
            Log.e("LocalDataUtil", "Error reading local vehicle data: " + e.getMessage());
        }

        return vehicleList;
    }

    /**
     * Loads the complete user data map from the local temporary JSON file.
     *
     * @param context The application context used for file operations
     * @return Map containing all user data, or null if file doesn't exist or error occurs
     */
    public static Map<String, Object> loadUserMapFromTempJson(Context context) {
        try {
            FileInputStream fis = context.openFileInput("temp_userdata.json");
            InputStreamReader reader = new InputStreamReader(fis);
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> result = new Gson().fromJson(reader, type);
            reader.close();
            return result;
        } catch (Exception e) {
            Log.e("LocalDataUtil", "Failed to load user map from JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts vehicle data to a simplified format for display purposes.
     * Creates SimpleVehicle objects with basic vehicle information.
     *
     * @param context The application context used for file operations
     * @return List of SimpleVehicle objects containing basic vehicle information
     */
    public static List<SimpleVehicle> loadSimpleVehicles(Context context) {
        List<SimpleVehicle> simpleList = new ArrayList<>();
        List<Vehicle> vehicleList = loadVehiclesFromTempJson(context);

        for (Vehicle v : vehicleList) {
            String rego = v.getLicensePlate() != null ? v.getLicensePlate() : "UNKNOWN";
            String status = v.getRegistrationStatus() != null ? v.getRegistrationStatus() : "UNKNOWN";
            String expiry = v.getRegistrationExpiry() != null ? v.getRegistrationExpiry() : "UNKNOWN";

            simpleList.add(new SimpleVehicle(rego, status, expiry));
        }

        return simpleList;
    }

    /**
     * Loads license information from the local temporary JSON file.
     * Reads and parses license data, creating License objects with relevant information.
     *
     * @param context The application context used for file operations
     * @return List of License objects loaded from the local file, empty list if file doesn't exist or error occurs
     */
    public static List<License> loadLicenses(Context context) {
        List<License> licenseList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), "temp_userdata.json");
            if (!file.exists()) return licenseList;

            String json = new String(Files.readAllBytes(file.toPath()));
            JSONObject root = new JSONObject(json);
            JSONObject licensesJson = root.optJSONObject("licenses");

            if (licensesJson != null) {
                Iterator<String> keys = licensesJson.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject obj = licensesJson.optJSONObject(key);
                    if (obj != null) {
                        License license = new License();
                        license.setLicenseId(key);
                        license.setLicenseType(obj.optString("licenseType", "UNKNOWN"));
                        license.setLicenseNumber(obj.optString("licenseNumber", ""));
                        license.setExpiryDate(obj.optString("expiryDate", ""));
                        license.setStatus(obj.optString("status", ""));
                        licenseList.add(license);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return licenseList;
    }

    /**
     * Saves user data map to the local temporary JSON file.
     * Converts the provided map to JSON format and writes it to local storage.
     *
     * @param context The application context used for file operations
     * @param map The user data map to be saved
     */
    public static void saveUserMapToTempJson(Context context, Map<String, Object> map) {
        try {
            FileOutputStream fos = context.openFileOutput("temp_userdata.json", Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(map).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
