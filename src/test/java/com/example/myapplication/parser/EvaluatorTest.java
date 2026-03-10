package com.example.myapplication.parser;

import com.example.myapplication.model.SimpleVehicle;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test class for Evaluator.
 * This class contains unit tests for verifying the functionality of the Evaluator class,
 * including query evaluation for vehicle registration status, fines, and error handling.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class EvaluatorTest {

    /**
     * Creates a mock vehicle map for testing purposes.
     * Contains two vehicles: one expired and one valid.
     *
     * @return Map containing test vehicle data
     */
    private Map<String, SimpleVehicle> mockVehicleMap() {
        Map<String, SimpleVehicle> map = new HashMap<>();
        map.put("ABC123", new SimpleVehicle("ABC123", "Expired", "2024-12-01"));
        map.put("XYZ789", new SimpleVehicle("XYZ789", "Valid", "2025-06-30"));
        return map;
    }

    /**
     * Tests the evaluation of registration status query.
     * Verifies that the evaluator correctly returns the status of a vehicle.
     */
    @Test
    public void testRegoStatus() {
        String result = Evaluator.evaluate(new Query("rego_status", "abc123"), mockVehicleMap());
        assertEquals("Status: Expired", result);
    }

    /**
     * Tests the evaluation of fines query for an expired vehicle.
     * Verifies that the evaluator returns a message containing fines information.
     */
    @Test
    public void testFinesForExpiredVehicle() {
        String result = Evaluator.evaluate(new Query("fines", "abc123"), mockVehicleMap());
        assertTrue(result.contains("Fines"));
    }

    /**
     * Tests the evaluation of fines query for a valid vehicle.
     * Verifies that the evaluator returns a message indicating no fines.
     */
    @Test
    public void testFinesForValidVehicle() {
        String result = Evaluator.evaluate(new Query("fines", "xyz789"), mockVehicleMap());
        assertTrue(result.contains("No fines"));
    }

    /**
     * Tests the handling of invalid query types.
     * Verifies that the evaluator returns an appropriate error message.
     */
    @Test
    public void testInvalidQueryType() {
        String result = Evaluator.evaluate(new Query("unknown", "abc123"), mockVehicleMap());
        assertTrue(result.contains("didn't understand"));
    }

    /**
     * Tests the handling of non-existent vehicle queries.
     * Verifies that the evaluator returns an appropriate "not found" message.
     */
    @Test
    public void testVehicleNotFound() {
        String result = Evaluator.evaluate(new Query("rego_status", "doesnotexist"), mockVehicleMap());
        assertTrue(result.contains("Vehicle not found"));
    }

    /**
     * Tests the handling of null query objects.
     * Verifies that the evaluator returns an "Invalid query" message.
     */
    @Test
    public void testNullQuery() {
        String result = Evaluator.evaluate(null, new HashMap<>());
        assertEquals("Invalid query.", result);
    }

    /**
     * Tests the handling of queries with null values.
     * Verifies that the evaluator returns an "Invalid query" message.
     */
    @Test
    public void testNullQueryValue() {
        String result = Evaluator.evaluate(new Query("rego_status", null), new HashMap<>());
        assertEquals("Invalid query.", result);
    }
}
