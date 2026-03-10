package com.example.myapplication.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for Vehicle model.
 * This class contains unit tests for verifying the functionality of the Vehicle class,
 * including constructors (default, full, and map-based), getters, setters, and data conversion.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class VehicleTest {

    private Vehicle vehicle;

    /**
     * Sets up a new Vehicle instance before each test.
     * Initializes a fresh Vehicle object using the default constructor.
     */
    @Before
    public void setUp() {
        vehicle = new Vehicle();
    }

    /**
     * Tests the default constructor initialization.
     * Verifies that the default visibility is set to "Private".
     */
    @Test
    public void testDefaultConstructor() {
        assertEquals("Private", vehicle.getVisibility());
    }

    /**
     * Tests the full constructor initialization with all parameters.
     * Verifies that all vehicle properties are correctly set:
     * - Vehicle name
     * - License plate
     * - VIN
     * - Registration status and expiry
     * - Maintenance information
     * - Stolen status
     * - Location
     * - Visibility
     */
    @Test
    public void testFullConstructor() {
        Vehicle v = new Vehicle(
                "Car", "ABC123", "VIN001", "Active", "2025-12-31",
                "2024-10-10", "OK", "No", "City Center", "Public"
        );

        assertEquals("Car", v.getVehicleName());
        assertEquals("ABC123", v.getLicensePlate());
        assertEquals("VIN001", v.getVin());
        assertEquals("Active", v.getRegistrationStatus());
        assertEquals("2025-12-31", v.getRegistrationExpiry());
        assertEquals("2024-10-10", v.getLastMaintenance());
        assertEquals("OK", v.getLastMaintenanceStatus());
        assertEquals("No", v.getStolenStatus());
        assertEquals("City Center", v.getLastKnownLocation());
        assertEquals("Public", v.getVisibility());
    }

    /**
     * Tests the map-based constructor initialization.
     * Verifies that a Vehicle object can be created from a Map containing all required properties.
     */
    @Test
    public void testMapConstructor() {
        Map<String, Object> map = new HashMap<>();
        map.put("vehicleName", "Truck");
        map.put("licensePlate", "XYZ999");
        map.put("vin", "VIN999");
        map.put("registrationStatus", "Expired");
        map.put("registrationExpiry", "2022-01-01");
        map.put("lastMaintenance", "2021-12-12");
        map.put("lastMaintenanceStatus", "Failed");
        map.put("stolenStatus", "Yes");
        map.put("lastKnownLocation", "Highway");
        map.put("visibility", "Hidden");

        Vehicle v = new Vehicle(map);
        assertEquals("Truck", v.getVehicleName());
        assertEquals("XYZ999", v.getLicensePlate());
        assertEquals("VIN999", v.getVin());
        assertEquals("Expired", v.getRegistrationStatus());
        assertEquals("2022-01-01", v.getRegistrationExpiry());
        assertEquals("2021-12-12", v.getLastMaintenance());
        assertEquals("Failed", v.getLastMaintenanceStatus());
        assertEquals("Yes", v.getStolenStatus());
        assertEquals("Highway", v.getLastKnownLocation());
        assertEquals("Hidden", v.getVisibility());
    }

    /**
     * Tests all getter and setter methods.
     * Verifies that each property can be set and retrieved correctly.
     */
    @Test
    public void testGettersAndSetters() {
        vehicle.setVehicleName("Bike");
        vehicle.setLicensePlate("BIKE777");
        vehicle.setVin("VIN777");
        vehicle.setRegistrationStatus("Pending");
        vehicle.setRegistrationExpiry("2026-06-06");
        vehicle.setLastMaintenance("2025-01-01");
        vehicle.setLastMaintenanceStatus("Good");
        vehicle.setStolenStatus("Unknown");
        vehicle.setLastKnownLocation("Garage");
        vehicle.setVisibility("Internal");

        assertEquals("Bike", vehicle.getVehicleName());
        assertEquals("BIKE777", vehicle.getLicensePlate());
        assertEquals("VIN777", vehicle.getVin());
        assertEquals("Pending", vehicle.getRegistrationStatus());
        assertEquals("2026-06-06", vehicle.getRegistrationExpiry());
        assertEquals("2025-01-01", vehicle.getLastMaintenance());
        assertEquals("Good", vehicle.getLastMaintenanceStatus());
        assertEquals("Unknown", vehicle.getStolenStatus());
        assertEquals("Garage", vehicle.getLastKnownLocation());
        assertEquals("Internal", vehicle.getVisibility());
    }

    /**
     * Tests the toMap conversion method.
     * Verifies that a Vehicle object can be correctly converted to a Map representation.
     * Note: This test is only valid if the toMap() method is implemented in Vehicle.java.
     */
    @Test
    public void testToMap() {
        Vehicle v = new Vehicle(
            "Car", "ABC123", "VIN001", "Active", "2025-12-31",
            "2024-10-10", "OK", "No", "City Center", "Public"
        );

        Map<String, Object> map = v.toMap();
        assertEquals("Car", map.get("vehicleName"));
        assertEquals("ABC123", map.get("licensePlate"));
        assertEquals("VIN001", map.get("vin"));
        assertEquals("City Center", map.get("lastKnownLocation"));
        assertEquals("Public", map.get("visibility"));
    }
}
