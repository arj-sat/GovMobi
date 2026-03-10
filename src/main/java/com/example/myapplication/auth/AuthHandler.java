package com.example.myapplication.auth;

import android.app.Activity;

/**
 * Interface for handling authentication logic.
 * Implementations will define specific behavior for login or signup actions.
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public interface AuthHandler {
    /**
     * Handles the authentication logic.
     *
     * @param activity Current activity context.
     * @param email    User's email.
     * @param password User's password.
     */
    void handle(Activity activity, String email, String password);
}
