package com.example.myapplication.parser;

import com.example.myapplication.model.SimpleVehicle;

import java.util.Map;

/**
 * Evaluator processes structured {@link Query} objects against a provided map of vehicles.
 * Supports basic questions like status lookup, expiry checks, and mock fine calculation.
 *
 * This class simulates a basic rule-based response engine.
 *
 * @author
 * u8030355 - Shane George Shibu
 */
public class Evaluator {

    /**
     * Evaluates a query against the given vehicle map.
     *
     * @param query      A structured query object (type + value).
     * @param vehicleMap A map of rego strings to SimpleVehicle objects.
     * @return A formatted response string based on query type.
     */
    public static String evaluate(Query query, Map<String, SimpleVehicle> vehicleMap) {
        if (query == null || query.value == null) {
            return "Invalid query.";
        }

        String key = query.value.toUpperCase();
        SimpleVehicle vehicle = vehicleMap.get(key);

        if (vehicle == null) {
            return "Vehicle not found: " + query.value;
        }

        switch (query.type) {
            case "rego_status":
                return "Status: " + vehicle.getStatus();

            case "expiry_date":
                return "Expiry: " + vehicle.getExpiry();

            case "fines":
                if ("Expired".equalsIgnoreCase(vehicle.getStatus())) {
                    return "Fines for " + vehicle.getRego() + ": $200 (late registration)";
                } else {
                    return "No fines found for " + vehicle.getRego() + ".";
                }

            default:
                return "Sorry, I didn't understand that.";
        }
    }
}
