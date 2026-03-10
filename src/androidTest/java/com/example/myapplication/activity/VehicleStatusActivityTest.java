package com.example.myapplication.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserBackupUtil;
import com.example.myapplication.util.TestFirebaseAuthUtil;

import org.junit.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.*;

/**
 * Test class for VehicleStatusActivity.
 * This class contains UI tests for the vehicle status checking feature.
 * Tests include search functionality, vehicle details display,
 * and various button interactions.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class VehicleStatusActivityTest {

    Instrumentation instrumentation;

    /**
     * Rule for intercepting and handling intents during testing.
     */
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    /**
     * Performs one-time login setup before all tests.
     * This ensures the user is authenticated for all test cases.
     *
     * @throws Exception if login fails
     */
    @BeforeClass
    public static void loginOnce() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestFirebaseAuthUtil.loginIfNeeded(context);
    }

    /**
     * Sets up the test environment before each test.
     * Launches VehicleStatusActivity and clears logcat.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        FirebaseUserBackupUtil.backupFirebaseUser(instrumentation.getTargetContext());

        Intent intent = new Intent(instrumentation.getTargetContext(), VehicleStatusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instrumentation.getTargetContext().startActivity(intent);

        Thread.sleep(1500);
        Runtime.getRuntime().exec("logcat -c");
    }

    /**
     * Cleans up after each test.
     * Stops the application and restores Firebase user data.
     *
     * @throws Exception if cleanup fails
     */
    @After
    public void tearDown() throws Exception {
        Runtime.getRuntime().exec("am force-stop com.example.myapplication");
        Thread.sleep(1000);
        FirebaseUserBackupUtil.restoreFirebaseUser(instrumentation.getTargetContext());
    }

    /**
     * Checks logcat for specific keywords in a given tag.
     *
     * @param tag the log tag to search in
     * @param keyword the keyword to search for
     * @return true if the keyword is found, false otherwise
     * @throws Exception if log reading fails
     */
    private boolean checkLogForKeyword(String tag, String keyword) throws Exception {
        Process process = Runtime.getRuntime().exec("logcat -d " + tag + ":D *:S");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(keyword)) return true;
        }
        return false;
    }

    /**
     * Tests the share report functionality by intercepting the share intent.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testShareReportClicked_IntentIntercepted() throws Exception {
        intending(IntentMatchers.hasAction(Intent.ACTION_SEND))
                .respondWith(new Instrumentation.ActivityResult(0, null));

        onView(withId(R.id.share_report_button)).perform(click());
    }

    /**
     * Tests search behavior with empty input.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testSearchEmpty_ShowsToast() throws Exception {
        onView(withId(R.id.search_bar)).perform(typeText(" "), pressImeActionButton());
        Thread.sleep(1000);
    }

    /**
     * Tests search behavior with invalid plate number.
     * Verifies that UI is cleared when no vehicle is found.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testSearchNotFound_ClearsUI() throws Exception {
        onView(withId(R.id.search_bar)).perform(typeText("INVALID-PLATE"), pressImeActionButton());
        onView(withId(R.id.search_icon)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.vehicle_name)).check(matches(withText("-")));
        onView(withId(R.id.license_plate)).check(matches(withText("-")));
        onView(withId(R.id.vin)).check(matches(withText("-")));
        onView(withId(R.id.registration_status_text)).check(matches(withText("-")));
        onView(withId(R.id.last_maintenance_text)).check(matches(withText("-")));
        onView(withId(R.id.stolen_status_text)).check(matches(withText("-")));
    }

    /**
     * Tests search behavior with a valid plate number.
     * Verifies that UI is populated when vehicle is found.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testSearchFound_PopulatesUI() throws Exception {
        onView(withId(R.id.search_bar)).perform(typeText("CFF-799"), pressImeActionButton());
        onView(withId(R.id.search_icon)).perform(click());
        Thread.sleep(1500);

        onView(withId(R.id.vehicle_name)).check(matches(not(withText("-"))));
    }

    /**
     * Tests search functionality with a valid plate number.
     * Verifies that all vehicle details are displayed correctly.
     * Note: Requires the vehicle data to exist in Firebase.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testSearchValidPlate_ShowsVehicleDetails() throws Exception {
        // Using valid plate number CFF-799, requires data to exist in Firebase
        onView(withId(R.id.search_bar)).perform(typeText("CFF-799"));
        onView(withId(R.id.search_icon)).perform(click());

        Thread.sleep(1500);

        onView(withId(R.id.vehicle_name)).check(matches(not(withText("-"))));
        onView(withId(R.id.license_plate)).check(matches(withText(containsString("CFF-799"))));

        onView(withId(R.id.vin)).check(matches(not(withText("-"))));
        onView(withId(R.id.registration_status_text)).check(matches(not(withText("-"))));
        onView(withId(R.id.last_maintenance_text)).check(matches(not(withText("-"))));
        onView(withId(R.id.stolen_status_text)).check(matches(not(withText("-"))));
    }

    /**
     * Tests profile icon click logging.
     * Currently ignored as it may be flaky.
     *
     * @throws Exception if test fails
     */
    @Ignore
    @Test
    public void testProfileClicked_LogTriggered() throws Exception {
        onView(withId(R.id.profile_icon)).perform(click());
        Thread.sleep(1500);
        assert checkLogForKeyword("VEHICLE_LOG", "Profile icon clicked");
    }

    /**
     * Tests check database button click logging.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testCheckDatabaseClicked_LogTriggered() throws Exception {
        onView(withId(R.id.check_database_button)).perform(click());
        Thread.sleep(1500);
        assert checkLogForKeyword("VEHICLE_LOG", "Check Database button clicked");
    }

    // Commented out test for upload certificate functionality
    // @Test
    // public void testUploadCertificateClicked_LogTriggered() throws Exception {
    //     onView(withId(R.id.upload_certificate_button)).perform(click());
    //     Thread.sleep(1500);
    //     assert checkLogForKeyword("VEHICLE_LOG", "Upload Certificate button clicked");
    // }
}
