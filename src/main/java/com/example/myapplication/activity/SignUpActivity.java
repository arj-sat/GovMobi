package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;
import com.example.myapplication.auth.AuthHandler;
import com.example.myapplication.auth.AuthHandlerFactory;

/**
 * This activity allows a new user to sign up.
 * The user enters their name, email, phone, and password.
 * Firebase authentication is used to register the user.
 * If successful, the user is saved to the Firebase Realtime Database.
 * Users can also switch to the login screen.
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class SignUpActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPhone, editTextPassword;
    Button buttonSignUp;
    TextView textLogin;

    FirebaseAuth mAuth;
    DatabaseReference userRef;

    /**
     * Initializes the sign-up screen.
     * Sets up listeners for sign-up and navigation to login.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Bind UI elements
        editTextName     = findViewById(R.id.editTextName);
        editTextEmail    = findViewById(R.id.editTextEmail);
        editTextPhone    = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp     = findViewById(R.id.buttonSignUp);
        textLogin        = findViewById(R.id.textLogin);

        // Initialize Firebase Auth and Database
        mAuth   = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        /**
         * Handles the sign-up process when the sign-up button is clicked.
         * Validates inputs and delegates the handling to the appropriate AuthHandler.
         */
        buttonSignUp.setOnClickListener(v -> {
            String name     = editTextName.getText().toString().trim();
            String email    = editTextEmail.getText().toString().trim();
            String phone    = editTextPhone.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                Log.d("SIGNUP_LOG", "Empty fields submitted");
                return;
            }

            Log.d("SIGNUP_LOG", "Attempting signup with: " + email);
            AuthHandler handler = AuthHandlerFactory.getHandler("signup", name, phone);
            handler.handle(this, email, password);
        });

        /**
         * Switches to the login screen when the "Already have an account?" text is clicked.
         */
        textLogin.setOnClickListener(v -> {
            Log.d("SIGNUP_LOG", "Navigating to LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
