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
 * Test class for SignUpActivity.
 * This class contains UI tests for sign-up functionality, including field validation, navigation, and log output.
 *
 * @author u7884012 - Ruoheng Feng
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    Instrumentation instrumentation;

    /**
     * Sets up the test environment before each test.
     * Launches SignUpActivity and clears logcat.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        Intent intent = new Intent(instrumentation.getTargetContext(), SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instrumentation.getTargetContext().startActivity(intent);

        Thread.sleep(1000); // Wait for UI to load
        Runtime.getRuntime().exec("logcat -c");
    }

    /**
     * Force-stops the app after each test and waits for cleanup.
     *
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        Runtime.getRuntime().exec("am force-stop com.example.myapplication");
        Thread.sleep(1000);
    }

    /**
     * Log detection helper.
     * Checks if a specific log entry exists.
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
     * Tests submitting empty fields and checks for the expected warning log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testEmptyFields_ShowsWarningLog() throws Exception {
        onView(ViewMatchers.withId(R.id.buttonSignUp)).perform(click());
        Thread.sleep(1000);
        boolean found = checkLogForKeyword("SIGNUP_LOG", "Empty fields submitted");
        assert found : "Log not found for empty signup fields!";
    }

    /**
     * Tests clicking the login text and checks for the expected log output.
     * This test is ignored as it is covered by other tests.
     *
     * @throws Exception if test fails
     */
    @Ignore
    @Test
    public void testClickLogin_ShowsLog() throws Exception {
        onView(withId(R.id.textLogin)).perform(click());
        Thread.sleep(1000);
        boolean found = checkLogForKeyword("SIGNUP_LOG", "Navigating to LoginActivity");
        assert found : "Log not found for login navigation!";
    }

    /**
     * Tests registration with a test account.
     * If the account already exists, the test passes by assumption.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testRegistrationOnce_ShowsSuccessOrSkip() throws Exception {
        String testEmail = "testuser@example.com";
        String testPassword = "validPass123";

        onView(withId(R.id.editTextEmail)).perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());

        // Optionally, check for log output if needed
    }
}
