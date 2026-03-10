package com.example.myapplication.auth;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.myapplication.activity.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles user registration using Firebase Authentication and Database.
 * Stores default user profile and empty collections for vehicles and licenses.
 * Redirects to DashboardActivity on success.
 *
 * @see AuthHandler
 * @see DashboardActivity
 * @see FirebaseDatabase
 * @see FirebaseAuth
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class SignUpHandler implements AuthHandler {

    private final String name, phone;

    /**
     * Constructor that accepts name and phone number for the new user.
     *
     * @param name  Full name of the user.
     * @param phone Phone number of the user.
     */
    public SignUpHandler(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Handles Firebase user registration and profile creation.
     *
     * @param activity Current activity context.
     * @param email    New user's email.
     * @param password New user's password.
     */
    @Override
    public void handle(Activity activity, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            Map<String, Object> profile = new HashMap<>();
                            profile.put("name", name);
                            profile.put("email", email);
                            profile.put("phone", phone);
                            profile.put("licenseNumber", "");
                            profile.put("vehicleRego", "");
                            profile.put("dob", "");

                            Map<String, Object> fullData = new HashMap<>();
                            fullData.put("profile", profile);
                            fullData.put("vehicles", new HashMap<>());
                            fullData.put("licenses", new HashMap<>());

                            FirebaseDatabase.getInstance().getReference("Userdata").child(uid).setValue(fullData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(activity, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        activity.startActivity(new Intent(activity, DashboardActivity.class));
                                        activity.finish();
                                    });
                        }
                    } else {
                        Toast.makeText(activity, "Sign Up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
