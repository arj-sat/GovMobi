package com.example.myapplication.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test class for Parser.
 * This class contains unit tests for verifying the functionality of the Parser class,
 * including query parsing, tokenization, and suggestion generation for various types of queries.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class ParserTest {

    /** Set of known registration numbers used for testing query suggestions */
    private final Set<String> knownRegos = new HashSet<>(Arrays.asList("ABC123", "XYZ789"));

    /**
     * Tests the parsing of status query requests.
     * Verifies that the parser correctly identifies and suggests a registration status query.
     */
    @Test
    public void testStatusQuery() {
        List<String> tokens = Tokenizer.tokenize("check status for abc123");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertEquals("rego_status", queries.get(0).getType());
    }

    /**
     * Tests the parsing of expiry date query requests.
     * Verifies that the parser correctly identifies and suggests an expiry date query.
     */
    @Test
    public void testExpiryQuery() {
        List<String> tokens = Tokenizer.tokenize("when does abc123 expire");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertEquals("expiry_date", queries.get(0).getType());
    }

    /**
     * Tests the parsing of fines/penalty query requests.
     * Verifies that the parser correctly identifies and suggests a fines query.
     */
    @Test
    public void testFinesQuery() {
        List<String> tokens = Tokenizer.tokenize("penalty for abc123");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertEquals("fines", queries.get(0).getType());
    }

    /**
     * Tests the fallback to license information query.
     * Verifies that the parser correctly suggests a license info query when appropriate.
     */
    @Test
    public void testLicenseFallback() {
        List<String> tokens = Tokenizer.tokenize("driver license details");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertEquals("license_info", queries.get(0).getType());
    }

    /**
     * Tests the detection of introduction/instruction requests.
     * Verifies that the parser correctly identifies introduction-related keywords.
     */
    @Test
    public void testIntroDetection() {
        assertTrue(Parser.isIntroRequest("intro"));
        assertTrue(Parser.isIntroRequest("instruction"));
        assertFalse(Parser.isIntroRequest("rego"));
    }

    /**
     * Tests the handling of unknown or unrecognized input.
     * Verifies that the parser returns an empty list of suggestions for unrecognized input.
     */
    @Test
    public void testUnknownInputReturnsEmpty() {
        List<String> tokens = Tokenizer.tokenize("hello banana moon");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertTrue(queries.isEmpty());
    }

    /**
     * Tests the default query suggestions for registration number only input.
     * Verifies that the parser suggests all relevant queries (status, expiry, fines)
     * when only a registration number is provided.
     */
    @Test
    public void testPlateOnlyTriggersDefaultSuggestions() {
        List<String> tokens = Tokenizer.tokenize("ABC123");
        List<Query> queries = Parser.suggestQueries(tokens, knownRegos);
        assertEquals(3, queries.size());
        assertEquals("rego_status", queries.get(0).getType());
        assertEquals("expiry_date", queries.get(1).getType());
        assertEquals("fines", queries.get(2).getType());
    }
}
