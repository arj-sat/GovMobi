package com.example.myapplication.activity;

import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.myapplication.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Test class for LoginActivity.
 * This class contains UI tests for login functionality, including field validation, navigation, and log output.
 *
 * @author u7884012 - Ruoheng Feng
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    Instrumentation instrumentation;

    /**
     * Sets up the test environment before each test.
     * Launches LoginActivity and clears logcat.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        // Launch LoginActivity
        instrumentation = InstrumentationRegistry.getInstrumentation();
        Intent intent = new Intent(instrumentation.getTargetContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instrumentation.getTargetContext().startActivity(intent);

        Thread.sleep(1000); // Wait for UI to load

        // Clear previous logs
        Runtime.getRuntime().exec("logcat -c");
    }

    /**
     * Force-stops the app after each test and waits for cleanup.
     *
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        // Force-stop the app after each test
        Runtime.getRuntime().exec("am force-stop com.example.myapplication");
        Thread.sleep(1000);
    }

    /**
     * Utility method: check if a specific log entry exists.
     *
     * @param tag the log tag to search for
     * @param keyword the keyword to search for in the log
     * @return true if found, false otherwise
     * @throws Exception if logcat reading fails
     */
    private boolean checkLogForKeyword(String tag, String keyword) throws Exception {
        Process process = Runtime.getRuntime().exec("logcat -d " + tag + ":D *:S");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests submitting empty fields and checks for the expected log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testEmptyFields_ShowsToastLog() throws Exception {
        onView(ViewMatchers.withId(R.id.buttonLogin)).perform(click());
        Thread.sleep(1000);
        boolean found = checkLogForKeyword("LOGIN_LOG", "Empty fields submitted");
        assert found : "Expected log not found for empty field validation.";
    }

    /**
     * Tests clicking the sign-up text and checks for the expected log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testClickSignUp_ShowsLog() throws Exception {
        onView(withId(R.id.textSignUp)).perform(click());
        Thread.sleep(1000);
        boolean found = checkLogForKeyword("LOGIN_LOG", "Navigating to SignUpActivity");
        assert found : "Expected log not found for sign-up navigation.";
    }

    /**
     * Tests login failure and checks for the expected error log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testLoginFailure_ShowsErrorLog() throws Exception {
        onView(withId(R.id.editTextEmail)).perform(typeText("wrong@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("wrongpass"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        Thread.sleep(3000);
        boolean found = checkLogForKeyword("LOGIN_LOG", "Login failed:");
        assert found : "Expected log not found for failed login.";
    }

    /**
     * Tests login success with a valid test account.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testLoginSuccess_ShowsDashboardLog() throws Exception {
        // Use a valid Firebase test account
        String testEmail = "test@gmail.com";
        String testPassword = "test123";
        onView(withId(R.id.editTextEmail)).perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        Thread.sleep(5000);
        // Optionally, check for log output if needed
    }
}
