package com.example.myapplication.parser;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Tokenizer.
 * This class contains unit tests for verifying the functionality of the Tokenizer class,
 * including text splitting, whitespace handling, case conversion, and special character processing.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class TokenizerTest {

    /**
     * Tests basic text splitting functionality.
     * Verifies that a simple space-separated string is correctly tokenized.
     */
    @Test
    public void testSimpleSplit() {
        List<String> tokens = Tokenizer.tokenize("check status abc123");
        assertArrayEquals(new String[]{"check", "status", "abc123"}, tokens.toArray());
    }

    /**
     * Tests handling of multiple consecutive spaces.
     * Verifies that multiple spaces between words are properly handled and collapsed.
     */
    @Test
    public void testMultipleSpaces() {
        List<String> tokens = Tokenizer.tokenize("   check    status   abc123   ");
        assertArrayEquals(new String[]{"check", "status", "abc123"}, tokens.toArray());
    }

    /**
     * Tests case conversion of tokens.
     * Verifies that uppercase letters are converted to lowercase in the output tokens.
     */
    @Test
    public void testCapitalLetters() {
        List<String> tokens = Tokenizer.tokenize("ABC123 Expires");
        assertArrayEquals(new String[]{"abc123", "expires"}, tokens.toArray());
    }

    /**
     * Tests handling of empty string input.
     * Verifies that an empty string returns a list containing a single empty string,
     * as this is the behavior of split("\\s+") on empty input.
     */
    @Test
    public void testEmptyString() {
        List<String> tokens = Tokenizer.tokenize("");
        assertEquals(1, tokens.size());
        assertEquals("", tokens.get(0));  // 因为 split("\\s+") 对空字符串会返回一个包含空字符串的列表
    }

    /**
     * Tests handling of string containing only whitespace.
     * Verifies that a string of only spaces returns a list containing a single empty string,
     * consistent with the behavior of split("\\s+").
     */
    @Test
    public void testOnlySpaces() {
        List<String> tokens = Tokenizer.tokenize("     ");
        assertEquals(1, tokens.size());
        assertEquals("", tokens.get(0));  // 同样会返回一个空字符串
    }

    /**
     * Tests handling of special whitespace characters.
     * Verifies that newlines and tabs are treated as word separators.
     */
    @Test
    public void testNewlinesAndTabs() {
        List<String> tokens = Tokenizer.tokenize("check\nstatus\tabc123");
        assertArrayEquals(new String[]{"check", "status", "abc123"}, tokens.toArray());
    }
}
