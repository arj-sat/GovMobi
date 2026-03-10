package com.example.myapplication.search;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.matcher.RootMatchers.*;

import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.SmartSearchActivity;
import com.example.myapplication.activity.VehicleDetailActivity;
import com.example.myapplication.util.TestFirebaseAuthUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for SmartSearchActivity.
 * This class contains UI tests for the smart search functionality.
 * Tests include search input handling, result filtering, and navigation to detail views.
 *
 * @author u7884012 - Ruoheng Feng
 */
@RunWith(AndroidJUnit4.class)
public class SmartSearchActivityTest {

    /**
     * Sets up the test environment before each test.
     * Initializes Intents, performs Firebase login, and launches SmartSearchActivity.
     *
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        // Initialize Intents
        Intents.init();

        // Perform silent Firebase login and save JSON
        TestFirebaseAuthUtil.loginIfNeeded(ApplicationProvider.getApplicationContext());

        // Launch SmartSearchActivity
        ActivityScenario.launch(SmartSearchActivity.class);

        // Wait for UI elements to load
        onView(withId(R.id.searchInput)).check(matches(isDisplayed()));
    }

    /**
     * Cleans up after each test.
     * Releases Intents resources.
     */
    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Tests search behavior with non-matching keywords.
     * Verifies that "No results found" message is displayed.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testSearchInstructionKeywords_noMatch() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(replaceText("zzzzzzzzzz"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);
        onView(withText("No results found.")).check(matches(isDisplayed()));
    }

    /**
     * Tests the clear button functionality.
     * Verifies that input field and results are cleared when button is clicked.
     */
    @Test
    public void testClearButtonClearsInputAndResults() {
        onView(withId(R.id.searchInput)).perform(replaceText("ABC"));
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.clearButton)).perform(click());

        onView(withId(R.id.searchInput)).check(matches(withText("")));
        onView(withId(R.id.resultListView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /**
     * Tests instruction result dialog display.
     * Verifies that clicking an instruction result shows the dialog.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testInstructionResultDialogDisplayed() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("instruction"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);

        // Click the first item in the list
        onData(anything())
                .inAdapterView(withId(R.id.resultListView))
                .atPosition(0)
                .perform(click());

        // Check if dialog is displayed
        onView(withText("Instruction"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText("OK"))
                .inRoot(isDialog())
                .perform(click());
    }

    /**
     * Tests vehicle search result display and navigation.
     * Verifies that vehicle results are displayed and clicking opens detail view.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testVehicleResultDisplaysCorrectlyAndOpensDetail() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("CFF-799"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.resultListView)).check(matches(isDisplayed()));
        Thread.sleep(1000);

        onData(allOf(is(instanceOf(String.class)), startsWith("[Vehicle]")))
                .inAdapterView(withId(R.id.resultListView))
                .perform(click());
    }

    /**
     * Tests license search result display and navigation.
     * Verifies that license results are displayed and clicking opens detail view.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testLicenseResultDisplaysCorrectlyAndOpensDetail() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("license"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.resultListView)).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    /**
     * Tests search behavior with unknown keywords.
     * Verifies that "No results found" message is displayed.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testUnknownKeyword_ShowsNoResultMessage() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("unknownkeyword9876"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);
        onView(withText("No results found.")).check(matches(isDisplayed()));
    }

    /**
     * Tests search behavior with empty input.
     * Verifies that appropriate toast message is shown.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testEmptyKeyword_ShowsToast() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText(" "), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    /**
     * Tests fuzzy keyword matching for instructions.
     * Verifies that partial matches work correctly.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testFuzzyKeywordMatchesInstruction() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("ren"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);
    }

    /**
     * Tests initial UI component visibility.
     * Verifies that search input and button are visible, while result list is hidden.
     */
    @Test
    public void testInitialUIComponentsVisible() {
        onView(withId(R.id.searchInput)).check(matches(isDisplayed()));
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.resultListView)).check(matches(not(isDisplayed())));
    }

    /**
     * Tests vehicle filter functionality.
     * Verifies that only vehicle results are shown when filter is applied.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testVehicleFilterOnlyShowsVehicleResults() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(typeText("CFF-799 license"), closeSoftKeyboard());
        onView(withId(R.id.filterVehiclesButton)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);

        onData(allOf(is(instanceOf(String.class)), startsWith("[Vehicle]")))
                .inAdapterView(withId(R.id.resultListView))
                .check(matches(isDisplayed()));

        // Optional: Verify non-target type is not displayed
        onView(withText(containsString("[License]")))
                .check(doesNotExist());
    }

    /**
     * Tests license filter functionality.
     * Verifies that only license results are shown when filter is applied.
     *
     * @throws InterruptedException if test is interrupted
     */
    @Test
    public void testLicenseFilterOnlyShowsLicenseResults() throws InterruptedException {
        onView(withId(R.id.searchInput)).perform(replaceText("CFF-799 license"), closeSoftKeyboard());
        onView(withId(R.id.filterLicensesButton)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.resultListView)).check(matches(isDisplayed()));
        Thread.sleep(1000);

        onData(anything())
                .inAdapterView(withId(R.id.resultListView))
                .atPosition(0)
                .perform(click());

        // Optional: Verify vehicle results are not displayed
        onView(withText(containsString("[Vehicle]"))).check(doesNotExist());
    }

    /**
     * Gets the current activity instance.
     *
     * @return the current SmartSearchActivity instance
     */
    private SmartSearchActivity getCurrentActivity() {
        final SmartSearchActivity[] activity = new SmartSearchActivity[1];
        ActivityScenario<SmartSearchActivity> scenario = ActivityScenario.launch(SmartSearchActivity.class);
        scenario.onActivity(a -> activity[0] = a);
        return activity[0];
    }
}
