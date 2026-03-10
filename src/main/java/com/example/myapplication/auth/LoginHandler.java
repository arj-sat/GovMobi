package com.example.myapplication.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.activity.AdminActivity;
import com.example.myapplication.activity.DashboardActivity;
import com.example.myapplication.util.FirebaseUserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Handles user login using Firebase Authentication.
 * Routes admin users to AdminActivity, others to DashboardActivity.
 * Uses FirebaseUserUtil to save user data after login.
 *
 * @see AuthHandler
 * @see FirebaseUserUtil
 * @see AdminActivity
 * @see DashboardActivity
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class LoginHandler implements AuthHandler {
    private static final String ADMIN_EMAIL = "u111333@anu.edu.au";

    /**
     * Handles login using Firebase Auth and navigates user upon success.
     *
     * @param activity Current activity context.
     * @param email    Email to authenticate.
     * @param password Password to authenticate.
     */
    @Override
    public void handle(Activity activity, String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Intent intent = ADMIN_EMAIL.equalsIgnoreCase(user.getEmail())
                                    ? new Intent(activity, AdminActivity.class)
                                    : new Intent(activity, DashboardActivity.class);

                            FirebaseUserUtil.fetchAndSaveUserData(activity, user, () -> {
                                activity.startActivity(intent);
                                activity.finish();
                            });
                        }
                    } else {
                        Toast.makeText(activity, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
