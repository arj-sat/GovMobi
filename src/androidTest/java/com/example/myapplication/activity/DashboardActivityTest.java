package com.example.myapplication.activity;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.util.TestFirebaseAuthUtil;
import com.example.myapplication.R;

import org.junit.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

/**
 * Test class for DashboardActivity.
 * This class contains UI tests for dashboard interactions, including button clicks and log validation.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
public class DashboardActivityTest {

    private static final String TAG = "DASHBOARD_LOG";
    private ActivityScenario<DashboardActivity> scenario;

    /**
     * Logs in to Firebase test account once before all tests.
     *
     * @throws Exception if login fails
     */
    @BeforeClass
    public static void loginOnce() throws Exception {
        TestFirebaseAuthUtil.loginIfNeeded(ApplicationProvider.getApplicationContext());
    }

    /**
     * Sets up the test environment before each test.
     * Launches DashboardActivity and clears logcat.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        // 启动 DashboardActivity
        scenario = ActivityScenario.launch(DashboardActivity.class);
        // 清空 logcat 缓存
        Runtime.getRuntime().exec("logcat -c");
        Thread.sleep(500);
    }

    /**
     * Closes the activity after each test.
     *
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        if (scenario != null) scenario.close();
    }

    /**
     * Checks if the logcat contains the expected log entry.
     *
     * @param expected the expected log message
     * @return true if found, false otherwise
     * @throws Exception if logcat reading fails
     */
    private boolean checkLogcatFor(String expected) throws Exception {
        Process process = Runtime.getRuntime().exec("logcat -d " + TAG + ":D *:S");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(expected)) return true;
        }
        return false;
    }

    /**
     * Tests Help & Support button click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testHelpSupport_Log() throws Exception {
        onView(withId(R.id.help_support_button)).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Help & Support clicked");
    }

    /**
     * Tests Logout button click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testLogout_Log() throws Exception {
        onView(withId(R.id.button_logout)).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Logout button clicked");
    }

    /**
     * Tests Search icon click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testSearch_Log() throws Exception {
        onView(withId(R.id.search_icon)).perform(click());
        Thread.sleep(800);
        assert checkLogcatFor("Search icon clicked");
    }

    /**
     * Tests My Vehicles card click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testCard_MyVehicles_Log() throws Exception {
        onView(withText("My Vehicles")).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Card clicked: My Vehicles");
    }

    /**
     * Tests My Licenses card click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testCard_MyLicenses_Log() throws Exception {
        onView(withText("My Licenses")).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Card clicked: My Licenses");
    }

    /**
     * Tests Apply License card click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testCard_ApplyLicense_Log() throws Exception {
        onView(withText("Apply License")).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Card clicked: Apply License");
    }

    /**
     * Tests Vehicle History card click and log output.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testCard_VehicleHistory_Log() throws Exception {
        onView(withText("Vehicle History")).perform(scrollTo(), click());
        Thread.sleep(800);
        assert checkLogcatFor("Card clicked: Vehicle History");
    }
}
