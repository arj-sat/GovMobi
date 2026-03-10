package com.example.myapplication.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for Firebase authentication in test environments.
 * This class provides methods to handle Firebase authentication for testing purposes,
 * including automatic login and user data synchronization.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class TestFirebaseAuthUtil {

    /** Tag used for logging test-related authentication events */
    private static final String TAG = "TEST_LOGIN";

    /**
     * Attempts to log in to Firebase if no user is currently authenticated.
     * Uses a test account with predefined credentials and handles the authentication process
     * asynchronously with a timeout mechanism.
     *
     * @param context The application context used for user data operations
     * @throws Exception if login fails or times out
     * @throws RuntimeException if Firebase authentication fails or times out
     */
    public static void loginIfNeeded(Context context) throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            CountDownLatch latch = new CountDownLatch(1);
            auth.signInWithEmailAndPassword("testuser@example.com", "validPass123")
                    .addOnSuccessListener(result -> {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            FirebaseUserUtil.fetchAndSaveUserData(context, user, () -> {
                                Log.d(TAG, "Fetched and saved user data");
                                latch.countDown();
                            });
                        } else {
                            Log.e(TAG, "User is null after login");
                            latch.countDown();
                        }
                    })
                    .addOnFailureListener(e -> {
                        throw new RuntimeException("Firebase login failed: " + e.getMessage());
                    });

            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Firebase login timeout.");
            }
        }
    }

}
