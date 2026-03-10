package com.example.myapplication.activity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;
import com.example.myapplication.util.TestFirebaseAuthUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;

/**
 * Test class for ApplyLicenseActivity.
 * This class contains UI tests for the license application process, including form filling, submission, and Firebase cleanup.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ApplyLicenseActivityTest {

    @Rule
    public ActivityScenarioRule<ApplyLicenseActivity> activityRule =
            new ActivityScenarioRule<>(ApplyLicenseActivity.class);

    private static String testRequestId = null;
    private static String testUserEmail = null;

    /**
     * Sets up the test environment before each test.
     * Initializes Intents, logs in to Firebase, and captures the test user email.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        Intents.init();
        TestFirebaseAuthUtil.loginIfNeeded(ApplicationProvider.getApplicationContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            testUserEmail = user.getEmail();
        }
    }

    /**
     * Cleans up after each test.
     * Releases Intents and removes the test license request from Firebase if it was created.
     */
    @After
    public void tearDown() {
        Intents.release();
        if (testRequestId != null) {
            FirebaseDatabase.getInstance().getReference("LicenseRequests")
                    .child(testRequestId)
                    .removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("TEST", "Deleted application: " + testRequestId))
                    .addOnFailureListener(e -> Log.e("TEST", "Failed to delete: " + testRequestId, e));
        }
    }

    /**
     * Attempts to click the apply or reapply button if visible.
     * Used to handle both new and existing application scenarios.
     */
    private void tryClickApplyOrReapply() {
        try {
            onView(withId(R.id.bt_applylicense)).check(matches(isDisplayed()));
            onView(withId(R.id.bt_applylicense)).perform(click());
        } catch (Throwable e1) {
            try {
                onView(withId(R.id.bt_reapply)).check(matches(isDisplayed()));
                onView(withId(R.id.bt_reapply)).perform(click());
            } catch (Throwable e2) {
                System.out.println("Neither apply nor reapply is visible.");
            }
        }
    }

    /**
     * Tests the full license application process and cleans up the created request in Firebase.
     * Fills out the form, submits, and captures the requestId for later deletion.
     *
     * @throws InterruptedException if sleep is interrupted
     */
    @Test
    public void testApplyAndCleanup() throws InterruptedException {
        try {
            onView(withId(R.id.bt_cancel)).perform(click());
            Thread.sleep(500);
        } catch (Throwable ignored) {}

        tryClickApplyOrReapply();
        Thread.sleep(500);
        onView(withId(R.id.btn_submit)).perform(click());
        // Step 1: Fill out the form
        onView(withId(R.id.f_name)).perform(typeText("TestUser"), closeSoftKeyboard());
        onView(withId(R.id.f_dob)).perform(typeText("1995-05-01"), closeSoftKeyboard());
        onView(withId(R.id.f_adds)).perform(typeText("123 CleanTest Street"), closeSoftKeyboard());
        onView(withId(R.id.f_cert)).perform(typeText("CERT-CLEAN-TEST"), closeSoftKeyboard());
        onView(withId(R.id.f_phone)).perform(typeText("0412345678"), closeSoftKeyboard());

        onView(withId(R.id.spinner_license_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Learner"))).perform(click());

        onView(withId(R.id.btn_submit)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.et_reference_number)).perform(typeText("REF-CLEAN-TEST"), closeSoftKeyboard());
        onView(withId(R.id.btn_final_submit)).perform(click());
        Thread.sleep(2000);

        // Step 3: Capture the just-submitted requestId (by querying email)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LicenseRequests");
        ref.orderByChild("email").equalTo(testUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * Called when data is successfully read from Firebase.
                     * @param snapshot DataSnapshot containing the results
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot app : snapshot.getChildren()) {
                            testRequestId = app.getKey();
                            Log.d("TEST", "Captured requestId: " + testRequestId);
                            break;
                        }
                    }

                    /**
                     * Called when the Firebase query is cancelled or fails.
                     * @param error DatabaseError object
                     */
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TEST", "Failed to fetch requestId", error.toException());
                    }
                });

        Thread.sleep(3000); // Wait for Firebase query to complete so requestId can be used in tearDown
    }
}
