package com.example.myapplication.parser;

import java.util.Arrays;
import java.util.List;

/**
 * Tokenizer splits user input strings into lowercase word tokens.
 * Used as a preprocessing step before query parsing.
 *
 * For example: "check status ABC123" → ["check", "status", "abc123"]
 *
 * This class is intentionally simple and stateless.
 *
 * @author
 * u8030355 - Shane George Shibu
 */
public class Tokenizer {

    /**
     * Splits input into a list of lowercase words.
     *
     * @param input Raw string from user.
     * @return List of tokens (words).
     */
    public static List<String> tokenize(String input) {
        return Arrays.asList(input.trim().toLowerCase().split("\\s+"));
    }
}
