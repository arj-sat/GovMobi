package com.example.myapplication.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserBackupUtil;
import com.example.myapplication.util.TestFirebaseAuthUtil;

import org.hamcrest.Matchers;
import org.junit.*;

/**
 * Test class for ProfileActivity.
 * This class contains UI tests for profile editing, including field validation, edit mode, and photo change.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
public class ProfileActivityTest {

    private ActivityScenario<ProfileActivity> scenario;

    /**
     * Ensures Firebase login is performed once before all tests.
     *
     * @throws Exception if login fails
     */
    @BeforeClass
    public static void ensureFirebaseLogin() throws Exception {
        // Log in to test account (only once)
        TestFirebaseAuthUtil.loginIfNeeded(ApplicationProvider.getApplicationContext());
    }

    /**
     * Sets up the test environment before each test.
     * Backs up Firebase user data and launches a new ProfileActivity instance.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();

        // Backup Firebase user data before each test
        FirebaseUserBackupUtil.backupFirebaseUser(context);

        // Launch a new activity instance
        scenario = ActivityScenario.launch(ProfileActivity.class);
    }

    /**
     * Closes the activity and restores Firebase user data after each test.
     *
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        if (scenario != null) scenario.close(); // Close UI first to avoid affecting restore
        FirebaseUserBackupUtil.restoreFirebaseUser(ApplicationProvider.getApplicationContext());
    }

    /**
     * Tests that the email field is not editable.
     */
    @Test
    public void testEmailFieldIsNotEditable() {
        onView(withId(R.id.editEmail)).check(matches(Matchers.not(isEnabled())));
    }

    /**
     * Tests that edit mode enables all fields except email.
     */
    @Test
    public void testEditModeEnablesFieldsExceptEmail() {
        onView(withId(R.id.buttonEdit)).perform(click());

        onView(withId(R.id.editName)).check(matches(isEnabled()));
        onView(withId(R.id.editPhone)).check(matches(isEnabled()));
        onView(withId(R.id.editLicenseNumber)).check(matches(isEnabled()));
        onView(withId(R.id.editAddress)).check(matches(isEnabled()));
        onView(withId(R.id.editDOB)).check(matches(isEnabled()));
        onView(withId(R.id.editEmail)).check(matches(Matchers.not(isEnabled())));
    }

    /**
     * Tests the full profile save flow.
     * Enters edit mode, fills out all fields, and saves.
     *
     * @throws InterruptedException if sleep is interrupted
     */
    @Test
    public void testSaveProfileFlow() throws InterruptedException {
        onView(withId(R.id.buttonEdit)).perform(click());

        onView(withId(R.id.editName)).perform(clearText(), typeText("Test User"), closeSoftKeyboard());
        onView(withId(R.id.editPhone)).perform(clearText(), typeText("0400000001"), closeSoftKeyboard());
        onView(withId(R.id.editLicenseNumber)).perform(clearText(), typeText("T1234567"), closeSoftKeyboard());
        onView(withId(R.id.editAddress)).perform(clearText(), typeText("Canberra"), closeSoftKeyboard());
        onView(withId(R.id.editDOB)).perform(clearText(), typeText("01/01/1990"), closeSoftKeyboard());

        onView(withId(R.id.buttonSave)).perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.buttonEdit)).check(matches(isDisplayed()));
    }

    /**
     * Tests that the change photo button is clickable.
     */
    @Test
    public void testChangePhotoButtonWorks() {
        onView(withId(R.id.buttonChangePhoto)).perform(click());
    }
}
