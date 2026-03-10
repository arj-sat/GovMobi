package com.example.myapplication.adapter;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for LicenseAdapter's view binding logic.
 * This class contains unit tests for verifying the correct binding of license data to view components.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class LicenseAdapterLogicTest {

    /**
     * Tests the binding logic of license data to view components.
     * Verifies that the adapter correctly formats and sets text for name, license type, and ID fields.
     */
    @Test
    public void testBindToViewsLogic() {
        FakeTextView tvName = new FakeTextView();
        FakeTextView tvType = new FakeTextView();
        FakeTextView tvId = new FakeTextView();

        Map<String, String> data = Map.of(
                "name", "Bob",
                "licenseType", "Learner",
                "randomId", "XYZ-007"
        );

        LicenseAdapter.bindToFakeViews(tvName, tvType, tvId, data);

        assertEquals("Name: Bob", tvName.getText());
        assertEquals("License Type: Learner", tvType.getText());
        assertEquals("Request ID: XYZ-007", tvId.getText());
    }
}
