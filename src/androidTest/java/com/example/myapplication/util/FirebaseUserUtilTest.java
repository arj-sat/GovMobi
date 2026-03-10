package com.example.myapplication.util;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for FirebaseUserUtil.
 * This class contains unit tests for Firebase user data management functionality.
 * Tests include handling of null user cases and user data fetching.
 *
 * @author u7884012 - Ruoheng Feng
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseUserUtilTest {

    /**
     * Tests the behavior when fetching and saving user data with a null user.
     * Verifies that the operation handles null user gracefully without crashing
     * and does not trigger the completion callback.
     */
    @Test
    public void testFetchAndSaveUserData_nullUser() {
        Context context = ApplicationProvider.getApplicationContext();

        // Should not crash or save when user is null
        FirebaseUserUtil.fetchAndSaveUserData(context, null, () -> {
            // Callback should not be called
            throw new AssertionError("onComplete should not be called for null user.");
        });
    }
}
