package com.example.myapplication.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for AdminActivity.
 * This class contains UI tests for the admin login and logout functionality.
 * Tests include admin login process and logout verification.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminActivityTest {

    /**
     * Activity scenario rule for LoginActivity.
     * This rule launches the LoginActivity before each test.
     */
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Tests the complete admin login and logout flow:
     * 1. Enters admin credentials (email and password)
     * 2. Clicks login button
     * 3. Navigates to profile
     * 4. Performs logout
     *
     * @throws InterruptedException if the test is interrupted during sleep
     */
    @Test
    public void adminActivityTest() throws InterruptedException {
        // Enter admin email
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutRoot),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                4)));
        appCompatEditText.perform(scrollTo(), replaceText("admin@govmobi.com"), closeSoftKeyboard());

        // Enter admin password
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutRoot),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                5)));
        appCompatEditText2.perform(scrollTo(), replaceText("admin1"), closeSoftKeyboard());

        // Click login button
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.buttonLogin), withText("Login"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutRoot),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                6)));
        materialButton.perform(scrollTo(), click());
        Thread.sleep(3000);

        // Click profile icon
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.iv_profile_icon), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        // Click logout button
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_admin_logout), withText("Logout"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        materialButton2.perform(click());
    }

    /**
     * Creates a matcher that matches a child view at a specific position in a parent view.
     *
     * @param parentMatcher the matcher for the parent view
     * @param position the position of the child view
     * @return a matcher that matches the child view at the specified position
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
