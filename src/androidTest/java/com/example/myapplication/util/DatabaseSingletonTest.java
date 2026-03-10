package com.example.myapplication.util;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.model.Vehicle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test class for DatabaseSingleton.
 * This class contains unit tests for local and Firebase vehicle data storage and retrieval.
 * Tests include saving/loading vehicles locally, handling missing files, and Firebase operations.
 *
 * @author u7884012 - Ruoheng Feng
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseSingletonTest {

    private Context context;
    private DatabaseSingleton db;

    /**
     * Sets up the test environment before each test.
     * Initializes the application context and DatabaseSingleton instance.
     * Also deletes the local vehicles.json file to ensure test consistency.
     */
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        db = DatabaseSingleton.getInstance(context);

        // Remove local JSON file to ensure consistent test state
        File file = new File(context.getFilesDir(), "vehicles.json");
        if (file.exists()) file.delete();
    }

    /**
     * Tests saving and loading vehicles locally.
     * Verifies that saved vehicles can be loaded correctly from local storage.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testSaveAndLoadLocalVehicles() throws Exception {
        Vehicle v1 = new Vehicle("Toyota", "ABC123", "VIN001", "Valid",
                "2025-01-01", "2024-01-01", "OK", "No", "ACT", "Public");
        Vehicle v2 = new Vehicle("Honda", "XYZ999", "VIN002", "Expired",
                "2023-01-01", "2023-06-01", "Poor", "Yes", "NSW", "Private");

        List<Vehicle> original = Arrays.asList(v1, v2);

        db.saveLocalVehicles(original);
        List<Vehicle> result = db.loadLocalVehicles();

        assertEquals(2, result.size());
        assertEquals("ABC123", result.get(0).getLicensePlate());
        assertEquals("XYZ999", result.get(1).getLicensePlate());
    }

    /**
     * Tests loading vehicles when the local file is missing.
     * Verifies that an empty list is returned if the file does not exist.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testLoadWhenFileMissing_ReturnsEmptyList() throws Exception {
        File file = new File(context.getFilesDir(), "vehicles.json");
        if (file.exists()) file.delete();

        List<Vehicle> result = db.loadLocalVehicles();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests saving vehicles to Firebase (success path).
     * Verifies that the onSuccess callback is triggered upon successful save.
     *
     * @throws Exception if interrupted while waiting for callback
     */
    @Test
    public void testSaveVehiclesToFirebase_SuccessPath() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        List<Vehicle> vehicles = Collections.singletonList(
                new Vehicle("Mazda", "TEST123", "VIN999", "Valid",
                        "2027-05-05", "2026-05-05", "Good", "No", "QLD", "Public")
        );

        db.saveVehiclesToFirebase(vehicles, new DatabaseSingleton.OnSaveCompleteListener() {
            /**
             * Called when save is successful.
             */
            @Override
            public void onSuccess() {
                assertTrue(true); // Save succeeded
                latch.countDown();
            }

            /**
             * Called when save fails.
             * @param error Error message
             */
            @Override
            public void onError(String error) {
                fail("Save failed: " + error);
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    /**
     * Tests loading vehicles from Firebase (success path).
     * Verifies that the onVehiclesLoaded callback is triggered and returns a non-null list.
     *
     * @throws Exception if interrupted while waiting for callback
     */
    @Test
    public void testLoadVehiclesFromFirebase_SuccessPath() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        db.loadVehiclesFromFirebase(new DatabaseSingleton.OnVehiclesLoadedListener() {
            /**
             * Called when vehicles are loaded from Firebase.
             * @param vehicles List of loaded vehicles
             */
            @Override
            public void onVehiclesLoaded(List<Vehicle> vehicles) {
                assertNotNull(vehicles);
                assertTrue(vehicles.size() >= 0); // Allow 0, but must not be null
                latch.countDown();
            }

            /**
             * Called when loading vehicles fails.
             * @param error Error message
             */
            @Override
            public void onError(String error) {
                fail("Load failed: " + error);
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}
