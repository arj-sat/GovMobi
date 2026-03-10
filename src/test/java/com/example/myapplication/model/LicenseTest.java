package com.example.myapplication.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for License model.
 * This class contains unit tests for verifying the functionality of the License class,
 * including default constructor, setters, getters, and full constructor initialization.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class LicenseTest {

    private License license;

    /**
     * Sets up a new License instance before each test.
     * Initializes a fresh License object using the default constructor.
     */
    @Before
    public void setUp() {
        license = new License();
    }

    /**
     * Tests the default constructor and setter methods.
     * Verifies that all license properties can be set and retrieved correctly:
     * - License ID
     * - License Type
     * - License Number
     * - Expiry Date
     * - Status
     * - User ID
     */
    @Test
    public void testDefaultConstructorAndSetters() {
        license.setLicenseId("L123");
        license.setLicenseType("Full");
        license.setLicenseNumber("ABC-456");
        license.setExpiryDate("2025-12-31");
        license.setStatus("Active");
        license.setUserId("U789");

        assertEquals("L123", license.getLicenseId());
        assertEquals("Full", license.getLicenseType());
        assertEquals("ABC-456", license.getLicenseNumber());
        assertEquals("2025-12-31", license.getExpiryDate());
        assertEquals("Active", license.getStatus());
        assertEquals("U789", license.getUserId());
    }

    /**
     * Tests the full constructor initialization.
     * Verifies that a License object can be created with all required parameters
     * and that the userId field remains null as it's not part of the constructor.
     */
    @Test
    public void testFullConstructor() {
        License l = new License("L321", "Learner", "XYZ-999", "2024-10-10", "Suspended");

        assertEquals("L321", l.getLicenseId());
        assertEquals("Learner", l.getLicenseType());
        assertEquals("XYZ-999", l.getLicenseNumber());
        assertEquals("2024-10-10", l.getExpiryDate());
        assertEquals("Suspended", l.getStatus());
        assertNull(l.getUserId()); // not in constructor
    }
}
