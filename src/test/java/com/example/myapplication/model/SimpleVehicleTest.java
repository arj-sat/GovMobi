package com.example.myapplication.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SimpleVehicle model.
 * This class contains unit tests for verifying the functionality of the SimpleVehicle class,
 * including constructor initialization and getter methods for vehicle properties.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class SimpleVehicleTest {

    /**
     * Tests the constructor and getter methods of SimpleVehicle.
     * Verifies that all vehicle properties are correctly initialized and can be retrieved:
     * - Registration number (Rego)
     * - Vehicle status
     * - Expiry date
     */
    @Test
    public void testConstructorAndGetters() {
        SimpleVehicle vehicle = new SimpleVehicle("ABC123", "Active", "2026-01-01");

        assertEquals("ABC123", vehicle.getRego());
        assertEquals("Active", vehicle.getStatus());
        assertEquals("2026-01-01", vehicle.getExpiry());
    }
}
