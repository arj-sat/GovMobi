package com.example.myapplication.util;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for IdGenerator.
 * This class contains unit tests for verifying the functionality of the IdGenerator class,
 * specifically testing the uniqueness and validity of generated IDs.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class IdGeneratorTest {

    /**
     * Tests the generation of unique IDs.
     * Verifies that:
     * - Generated IDs are not null
     * - Each generated ID is unique (no duplicates)
     * - The generator can produce a large number of unique IDs (1000)
     */
    @Test
    public void testGenerateUniqueIds() {
        IdGenerator gen = new IdGenerator();
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String id = gen.getid();
            assertNotNull(id);
            assertFalse(ids.contains(id));
            ids.add(id);
        }
    }
}
