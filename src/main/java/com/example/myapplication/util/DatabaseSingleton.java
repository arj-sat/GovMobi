package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.model.Vehicle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * DatabaseSingleton - A singleton class that manages both Firebase and local JSON database operations.
 * This class provides centralized access to vehicle, user, license request data, supporting both online (Firebase) and offline (JSON file) storage.
 * Implements the singleton pattern to ensure only one instance exists throughout the application.
 * @author  u8007015 Arjun Satish
 */
public class DatabaseSingleton {
    private static DatabaseSingleton db_inst;
    private DatabaseReference db_ref;
    private File jsonFile;
    private Gson gson;

    private DatabaseSingleton(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db_ref = db.getReference("vehicles");
        jsonFile = new File(context.getFilesDir(), "vehicles.json");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static synchronized DatabaseSingleton getInstance(Context context) {
        if (db_inst == null) {
            db_inst = new DatabaseSingleton(context.getApplicationContext());
        }
        return db_inst;
    }

    // Getter for database reference
    public DatabaseReference getDBReference() {
        return db_ref;
    }


    /**
     * Loads vehicles from local JSON file storage.
     * Used as a fallback when offline or for caching purposes.
     *
     * @return List of Vehicle objects loaded from local storage
     * @throws IOException If there's an error reading the JSON file
     */
    // Load vehicles from local JSON file (fallback/cache)
    public List<Vehicle> loadLocalVehicles() throws IOException {
        if (!jsonFile.exists()) {
            return new ArrayList<>();
        }
        FileReader reader = new FileReader(jsonFile);
        List<Vehicle> vehicles = gson.fromJson(reader, new TypeToken<List<Vehicle>>(){}.getType());
        reader.close();
        return vehicles != null ? vehicles : new ArrayList<>();
    }

    // Save vehicles to local JSON file
    public void saveLocalVehicles(List<Vehicle> vehicles) throws IOException {
        FileWriter writer = new FileWriter(jsonFile);
        gson.toJson(vehicles, writer);
        writer.close();
    }

    // Load vehicles from Firebase with a callback
    public void loadVehiclesFromFirebase(final OnVehiclesLoadedListener listener) {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Vehicle> vehicles = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    if (vehicle != null) {
                        vehicles.add(vehicle);
                    }
                }
                listener.onVehiclesLoaded(vehicles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Save vehicles to Firebase
    // Save vehicles to Firebase
    public void saveVehiclesToFirebase(List<Vehicle> vehicles, final OnSaveCompleteListener listener) {
        // Clear existing data
        db_ref.removeValue((error, ref) -> {
            if (error != null) {
                listener.onError(error.getMessage());
                return;
            }
            // Add new data
            for (int i = 0; i < vehicles.size(); i++) {
                final int index = i; // make a final copy
                db_ref.child(String.valueOf(index)).setValue(vehicles.get(index), (error1, ref1) -> {
                    if (error1 != null) {
                        listener.onError(error1.getMessage());
                    } else if (index == vehicles.size() - 1) {
                        listener.onSuccess();
                    }
                });
            }
        });
    }


    // Callback interfaces
    public interface OnVehiclesLoadedListener {
        void onVehiclesLoaded(List<Vehicle> vehicles);
        void onError(String error);
    }

    public interface OnSaveCompleteListener {
        void onSuccess();
        void onError(String error);
    }
}