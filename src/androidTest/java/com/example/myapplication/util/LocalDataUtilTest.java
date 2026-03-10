package com.example.myapplication.util;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.example.myapplication.model.License;
import com.example.myapplication.model.Vehicle;
import com.example.myapplication.model.SimpleVehicle;

import org.junit.Before;
import org.junit.Test;
import java.io.FileOutputStream;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for LocalDataUtil.
 * This class contains unit tests for local data storage and retrieval functionality.
 * Tests include loading vehicles, user data, and licenses from temporary JSON files.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class LocalDataUtilTest {

    private Context context;

    /**
     * Sets up the test environment before each test.
     * Initializes the application context.
     */
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    /**
     * Tests loading vehicles from an empty temporary JSON file.
     * Verifies that an empty list is returned when the vehicles object is empty.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testLoadVehiclesFromTempJson_emptyFile() throws Exception {
        writeToTempUserdata("{\"vehicles\":{}}");
        List<Vehicle> vehicles = LocalDataUtil.loadVehiclesFromTempJson(context);
        assertTrue(vehicles.isEmpty());
    }

    /**
     * Tests loading user data from temporary JSON file.
     * Verifies that user data is correctly parsed and returned as a map.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testLoadUserMapFromTempJson() throws Exception {
        writeToTempUserdata("{\"user\": {\"name\": \"test\"}}");
        Map<String, Object> result = LocalDataUtil.loadUserMapFromTempJson(context);
        assertNotNull(result);
        assertTrue(result.containsKey("user"));
    }

    /**
     * Tests loading simple vehicles with default fallback values.
     * Verifies that null values are replaced with default values.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testLoadSimpleVehicles_defaultFallbacks() throws Exception {
        writeToTempUserdata("{\"vehicles\":{\"1\":{\"licensePlate\":null,\"registrationStatus\":null,\"registrationExpiry\":null}}}");
        List<SimpleVehicle> simpleVehicles = LocalDataUtil.loadSimpleVehicles(context);
        assertEquals("UNKNOWN", simpleVehicles.get(0).getRego());
    }

    /**
     * Tests loading licenses from temporary JSON file.
     * Verifies that license data is correctly parsed and returned as a list.
     *
     * @throws Exception if file operations fail
     */
    @Test
    public void testLoadLicenses() throws Exception {
        String data = "{\"licenses\":{\"abc123\":{\"licenseType\":\"Learner\",\"licenseNumber\":\"LN123\",\"expiryDate\":\"2025\",\"status\":\"active\"}}}";
        writeToTempUserdata(data);
        List<License> licenses = LocalDataUtil.loadLicenses(context);
        assertEquals(1, licenses.size());
        assertEquals("Learner", licenses.get(0).getLicenseType());
    }

    /**
     * Helper method to write test data to temporary user data file.
     *
     * @param content JSON content to write to the file
     * @throws Exception if file operations fail
     */
    private void writeToTempUserdata(String content) throws Exception {
        FileOutputStream fos = context.openFileOutput("temp_userdata.json", Context.MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();
    }
}
