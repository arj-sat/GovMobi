package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserUtil;
import com.google.firebase.auth.*;
import com.example.myapplication.auth.AuthHandler;
import com.example.myapplication.auth.AuthHandlerFactory;

/**
 * This activity handles the login process of the application.
 * Users enter their email and password to log in.
 * Based on the email domain, users are routed to either an admin dashboard or a user dashboard.
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_LOG";
    private static final String ADMIN_EMAIL = "@govmobi.com";

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textSignUp;

    FirebaseAuth mAuth;

    /**
     * Called when the activity is first created.
     * It initializes the login screen and sets up event listeners for login and sign-up actions.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail    = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin      = findViewById(R.id.buttonLogin);
        textSignUp       = findViewById(R.id.textSignUp);

        mAuth = FirebaseAuth.getInstance();

        /**
         * Handles login button click.
         * Validates input and attempts Firebase authentication.
         * Redirects to different screens based on user type.
         */
        buttonLogin.setOnClickListener(v -> {
            String email    = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Empty fields submitted");
                return;
            }

            Log.d(TAG, "Attempting login with email: " + email);
            //AuthHandler handler = AuthHandlerFactory.getHandler("login");
            //handler.handle(this, email, password);


            // Change button text and disable button
            buttonLogin.setText("Logging in...");
            buttonLogin.setEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // Restore button state
                        buttonLogin.setText("Login");
                        buttonLogin.setEnabled(true);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user == null) {
                                Toast.makeText(this, "Unknown error: user is null", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Logged in user is null");
                                return;
                            }
                            if ("test@gmail.com".equalsIgnoreCase(user.getEmail())) {
                                com.example.myapplication.util.ResetTestAccount.runReset(); // Note: ensure package name matches
                            }
//                            if ("comp6442@anu.edu.au".equalsIgnoreCase(user.getEmail())) {
//                                com.example.myapplication.util.ResetTestAccount1.reset(); // Note: ensure package name matches
//                            }
//
//                            if ("comp2100@anu.edu.au".equalsIgnoreCase(user.getEmail())) {
//                                com.example.myapplication.util.ResetTestAccount2.reset(); // Note: ensure package name matches
//                            }
                            // Decide the next activity based on user email
                            Intent nextIntent =  user.getEmail().toLowerCase().contains("@govmobi.com") ?
                                    new Intent(this, AdminActivity.class) :
                                    new Intent(this, DashboardActivity.class);

                            // Fetch and save user data, then launch the appropriate screen
                            FirebaseUserUtil.fetchAndSaveUserData(this, user, () -> {
                                startActivity(nextIntent);
                                finish();
                            });
                        } else {
                            String error = (task.getException() != null)
                                    ? task.getException().getMessage()
                                    : "Unknown error";
                            Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Login failed: " + error);
                        }
                    });
        });

        /**
         * Redirects the user to the sign-up screen.
         */
        textSignUp.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to SignUpActivity");
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }
}
