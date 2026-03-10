package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.example.myapplication.R;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.util.FirebaseUserBackupUtil;
import com.example.myapplication.util.TestFirebaseAuthUtil;

import org.junit.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;

/**
 * Test class for MyDetailActivity and related detail screens.
 * This class contains UI tests for deleting vehicles and licenses from the user's detail screens.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class MyDetailActivityTest {

    /**
     * Logs in and backs up Firebase user data once before all tests.
     *
     * @throws Exception if setup fails
     */
    @BeforeClass
    public static void loginAndBackup() throws Exception {
        TestFirebaseAuthUtil.loginIfNeeded(getContext());
        FirebaseUserBackupUtil.backupFirebaseUser(getContext());
    }

    /**
     * Restores Firebase user data after all tests.
     *
     * @throws Exception if teardown fails
     */
    @AfterClass
    public static void restoreData() throws Exception {
        FirebaseUserBackupUtil.restoreFirebaseUser(getContext());
    }

    /**
     * Tests deleting a vehicle from the My Vehicles screen.
     * Launches the activity, clicks the first vehicle card, and deletes it.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDeleteVehicleFromMyVehicles() throws Exception {
        Intent intent = new Intent(getContext(), MyVehiclesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);

        Thread.sleep(1500);

        // Click the first vehicle card
        onView(withId(R.id.vehicle_list_container)).check(matches(isDisplayed()));
        onView(withIndex(withClassName(containsString("CardView")), 0)).perform(click());
        Thread.sleep(1000);

        // Click the delete button
        onView(withId(R.id.btn_delete_vehicle)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(1000);
    }

    /**
     * Tests deleting a license from the My Licenses screen.
     * Launches the activity, clicks the first license card, and deletes it.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDeleteLicenseFromMyLicenses() throws Exception {
        Intent intent = new Intent(getContext(), MyLicensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);

        Thread.sleep(1500);

        // Click the first license card
        onView(withId(R.id.licenses_container)).check(matches(isDisplayed()));
        onView(withIndex(withClassName(containsString("CardView")), 0)).perform(click());
        Thread.sleep(1000);

        // Click the delete button
        onView(withId(R.id.btn_delete_license)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(1000);
    }

    /**
     * Gets the current resumed Activity instance (for Toast or UI checks).
     *
     * @return the current Activity
     */
    private static Activity getActivity() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            for (Activity a : androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(androidx.test.runner.lifecycle.Stage.RESUMED)) {
                activity[0] = a;
            }
        });
        return activity[0];
    }

    /**
     * Gets the target context for instrumentation.
     *
     * @return the target Context
     */
    private static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /**
     * Returns a matcher for the Nth view matching the given matcher (used for CardView selection).
     *
     * @param matcher the base matcher
     * @param index the index of the view to match
     * @return a matcher for the Nth view
     */
    public static org.hamcrest.Matcher<android.view.View> withIndex(
            final org.hamcrest.Matcher<android.view.View> matcher, final int index) {
        return new org.hamcrest.TypeSafeMatcher<android.view.View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with index: " + index + " ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(android.view.View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}
