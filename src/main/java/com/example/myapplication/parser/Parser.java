package com.example.myapplication.parser;

import java.util.*;

/**
 * Parser analyzes tokenized user input and returns a list of structured {@link Query} objects.
 * It supports detecting license plates and interpreting natural language for status, expiry, and fines.
 * Also handles fallback suggestions when a plate is not mentioned.
 *
 * Used in smart search functionality to convert plain text input into actionable intents.
 *
 * Example input: "check expiry for abc123" → [Query(type="expiry_date", value="ABC123")]
 *
 * @author
 * u7884012 - Ruoheng Feng
 * u8030355 - Shane George Shibu
 */


public class Parser {

    /** Keywords related to vehicles, used for fallback logic */
    public static final List<String> VEHICLE_KEYWORDS = Arrays.asList(
            "vehicle", "car", "rego", "registration", "plate", "license plate"
    );

    /** Keywords related to licenses, used for license info lookup */
    public static final List<String> LICENSE_KEYWORDS = Arrays.asList(
            "license", "driver", "driving", "expiry", "valid"
    );

    /**
     * Suggests a list of queries based on the user's input tokens and known vehicle plates.
     *
     * @param tokens          List of words from user input (lowercased and split).
     * @param availableRegos  Set of known registration numbers (uppercase).
     * @return List of relevant Query objects for evaluation.
     */
    public static List<Query> suggestQueries(List<String> tokens, Set<String> availableRegos) {
        List<Query> suggestions = new ArrayList<>();
        String plate = null;

        // Identify a license plate pattern (e.g., ABC123 or ABC-123)
        for (String token : tokens) {
            if (token.matches("[a-zA-Z]{3}-?[0-9]{3}")) {
                plate = token.toUpperCase();
                break;
            }
        }

        String joined = String.join(" ", tokens).toLowerCase();

        // Plate-specific queries
        for (String keyword : LICENSE_KEYWORDS) {
            if (joined.contains(keyword)) {
                suggestions.add(new Query("license_info", "default"));
                break;
            }
        }

        if (plate != null) {
            if (joined.contains("status") || joined.contains("rego") || joined.contains("registration")) {
                suggestions.add(new Query("rego_status", plate));
            }

            if (joined.contains("expiry") || joined.contains("expire")) {
                suggestions.add(new Query("expiry_date", plate));
            }

            if (joined.contains("fine") || joined.contains("penalty")) {
                suggestions.add(new Query("fines", plate));
            }

            final String plateFinal = plate;
            boolean alreadyHasPlate = suggestions.stream().anyMatch(q -> q.getTarget().equals(plateFinal));
            if (!alreadyHasPlate) {
                suggestions.add(new Query("rego_status", plate));
                suggestions.add(new Query("expiry_date", plate));
                suggestions.add(new Query("fines", plate));
            }
        } else {
            for (String keyword : VEHICLE_KEYWORDS) {
                if (joined.contains(keyword)) {
                    for (String rego : availableRegos) {
                        suggestions.add(new Query("default", rego));
                    }
                    break;
                }
            }
        }

        return suggestions;
    }

    public static boolean isIntroRequest(String input) {
        String lowered = input.toLowerCase();
        return lowered.contains("intro") || lowered.contains("help") || lowered.contains("instruction");
    }

}
