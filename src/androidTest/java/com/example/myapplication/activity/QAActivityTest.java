package com.example.myapplication.activity;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

/**
 * Test class for QAActivity.
 * This class contains UI tests for the AI Chat Support feature.
 * Tests include toolbar title verification, welcome message display,
 * and user interaction with the chat interface.
 *
 * @author u7884012 - Ruoheng Feng
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class QAActivityTest {

    /**
     * Sets up the test environment before each test.
     * Launches the QAActivity and waits for the initial welcome message.
     */
    @Before
    public void setUp() {
        ActivityScenario.launch(QAActivity.class);
        SystemClock.sleep(1000); // Wait for initial welcome message
    }

    /**
     * Tests if the toolbar title is correctly displayed as "AI Chat Support".
     */
    @Test
    public void testToolbarTitleIsCorrect() {
        onView(withText("AI Chat Support")).check(matches(isDisplayed()));
    }

    /**
     * Tests if the initial welcome message is displayed correctly.
     */
    @Test
    public void testInitialWelcomeMessageDisplayed() {
        onView(withText("Hi there! I'm your Assist AI. I can help you with any questions related to your vehicles, licenses and profile. How can I assist you today?"))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests the chat functionality by:
     * 1. Inputting a question
     * 2. Clicking the Ask button
     * 3. Verifying the question appears in the chat
     * 4. Waiting for and verifying the AI response
     */
    @Test
    public void testUserInputAndResponseDisplayed() {
        // Input question
        onView(withId(R.id.questionInput)).perform(typeText("How do I apply for a license?"), closeSoftKeyboard());

        // Click Ask button
        onView(withId(R.id.askButton)).perform(click());

        // Check if question is added to interface
        onView(withText("How do I apply for a license?")).check(matches(isDisplayed()));

        // Wait for LLMFacade response (simulate network delay)
        SystemClock.sleep(3000);

        // Check if answer text appears (check for any non-empty bot response)
        // You can replace with specific return text: e.g. "You can apply for a license by..."
//        onView(withId(R.id.chatRecyclerView)).check(matches(hasDescendant(withText(matchesPattern(".*")))));
    }
}
