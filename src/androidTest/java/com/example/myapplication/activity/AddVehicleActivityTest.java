package com.example.myapplication.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Context;
import android.location.LocationManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserBackupUtil;
import com.example.myapplication.util.TestFirebaseAuthUtil;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.UiObject;

import org.junit.*;
import org.junit.runner.RunWith;

/**
 * Test class for AddVehicleActivity.
 * This class contains UI tests for adding vehicles, including permission handling and input validation.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddVehicleActivityTest {

    private ActivityScenario<AddVehicleActivity> scenario;

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
     * Backs up Firebase user data, launches the activity, and handles permission dialogs.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        FirebaseUserBackupUtil.backupFirebaseUser(ApplicationProvider.getApplicationContext());
        scenario = ActivityScenario.launch(AddVehicleActivity.class);
        allowLocationPermissionIfPrompted();
    }

    /**
     * Restores Firebase user data and closes the activity after each test.
     *
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        FirebaseUserBackupUtil.restoreFirebaseUser(ApplicationProvider.getApplicationContext());
        if (scenario != null) scenario.close();
    }

    /**
     * Tests clicking save with empty fields does not crash the app.
     */
    @Test
    public void testEmptyFields_clickSave_noCrash() {
        onView(withId(R.id.btnSaveVehicle)).perform(click());
    }

    /**
     * Tests saving a vehicle without location does not crash the app.
     *
     * @throws InterruptedException if sleep is interrupted
     */
    @Test
    public void testSaveVehicleWithoutLocation_doesNotCrash() throws InterruptedException {
        Thread.sleep(1000); // Simulate waiting for GPS
        onView(withId(R.id.etVehicleName)).perform(typeText("NoLocationCar"), closeSoftKeyboard());
        onView(withId(R.id.etLicensePlate)).perform(typeText("GPS-000"), closeSoftKeyboard());
        onView(withId(R.id.etVin)).perform(typeText("VIN00000"), closeSoftKeyboard());
        onView(withId(R.id.radioPrivate)).perform(click());
        onView(withId(R.id.btnSaveVehicle)).perform(click());
    }

    /**
     * Tests saving a vehicle with valid private input.
     *
     * @throws InterruptedException if sleep is interrupted
     */
    @Test
    public void testSaveVehicleWithValidInput_Private() throws InterruptedException {
        Thread.sleep(4000); // Wait for GPS
        onView(withId(R.id.etVehicleName)).perform(typeText("TestCar1"), closeSoftKeyboard());
        onView(withId(R.id.etLicensePlate)).perform(typeText("CAR-111"), closeSoftKeyboard());
        onView(withId(R.id.etVin)).perform(typeText("VINTEST001"), closeSoftKeyboard());
        onView(withId(R.id.radioPrivate)).perform(click());
        onView(withId(R.id.btnSaveVehicle)).perform(click());
        Thread.sleep(4000);
    }

    /**
     * Tests saving a vehicle with valid public input.
     *
     * @throws InterruptedException if sleep is interrupted
     */
    @Test
    public void testSaveVehicleWithValidInput_Public() throws InterruptedException {
        Thread.sleep(4000);
        onView(withId(R.id.etVehicleName)).perform(typeText("TestCar2"), closeSoftKeyboard());
        onView(withId(R.id.etLicensePlate)).perform(typeText("CAR-222"), closeSoftKeyboard());
        onView(withId(R.id.etVin)).perform(typeText("VINTEST002"), closeSoftKeyboard());
        onView(withId(R.id.radioPublic)).perform(click());
        onView(withId(R.id.btnSaveVehicle)).perform(click());
        Thread.sleep(4000);
    }

    /**
     * Tests GPS service disabled scenario. If GPS is enabled, this test is skipped.
     */
    @Test
    public void testGpsServiceDisabled_skippedIfEnabled() {
        Context context = ApplicationProvider.getApplicationContext();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            System.out.println("GPS is disabled — check manually if Toast appears.");
        }
    }

    /**
     * Handles location permission dialog if prompted by UIAutomator.
     * This method is used to automatically click permission dialogs during tests.
     */
    private void allowLocationPermissionIfPrompted() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            device.waitForWindowUpdate(null, 2000);

            UiObject allowPrecise = device.findObject(new UiSelector().textContains("Precise"));
            if (allowPrecise.exists()) {
                allowPrecise.click();
                Thread.sleep(500);
            }

            UiObject allowButton = device.findObject(new UiSelector().textContains("While using the app"));
            if (allowButton.exists()) {
                allowButton.click();
                Thread.sleep(500);
            }

        } catch (Exception e) {
            System.out.println("No permission dialog appeared.");
        }
    }
}
