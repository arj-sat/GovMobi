package com.example.myapplication.parser;

/**
 * Query is a lightweight structure representing a parsed user query.
 * It includes a type (intent) and a value (target keyword or rego).
 *
 * Used by Parser and Evaluator for natural language understanding and response logic.
 *
 * Examples:
 * type = "rego_status", value = "YTX123"
 * type = "fines", value = "abc987"
 *
 * @author
 * u8030355 - Shane George Shibu
 */
public class Query {
    String type;
    String value;

    /**
     * Constructs a new query.
     *
     * @param type  Intent/type of query (e.g., "expiry_date", "fines").
     * @param value Keyword or license plate being queried.
     */
    public Query(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns the primary search term or license plate.
     *
     * @return value to be evaluated.
     */
    public String getTarget() {
        return value;
    }
}
