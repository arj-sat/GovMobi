package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.example.myapplication.util.DatabaseSingleton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;
/**
 * AdminProfileActivity for profile of admin, allows admin to review personal information and logout
 * @author u8007015 Arjun Satish
 */
public class AdminProfileActivity extends AppCompatActivity {

    private static final String TAG = "AdminProfileActivity";
    private TextView nameTextView, emailTextView, roleTextView, phoneTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private Button logout;
    private ValueEventListener adminDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting AdminProfileActivity");
        setContentView(R.layout.activity_admin_profile);

        // Initialize Firebase
        Log.d(TAG, "onCreate: Initializing Firebase");
        mAuth = FirebaseAuth.getInstance();
        dbRef = DatabaseSingleton.getInstance(this)
                .getDBReference()
                .getDatabase()
                .getReference("AdminData");
        Log.d(TAG, "onCreate: Firebase initialized, dbRef=" + (dbRef != null ? "not null" : "null"));

        if (dbRef != null) {
            Log.d(TAG, "onCreate: Database URL=" + dbRef.getDatabase().getReference());
        }
        // Log Firebase app details
        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        Log.d(TAG, "onCreate: Firebase app name=" + (firebaseApp != null ? firebaseApp.getName() : "null"));
        Log.d(TAG, "onCreate: Firebase initialized=" + (firebaseApp != null));

        // Initialize Toolbar
        Log.d(TAG, "onCreate: Initializing Toolbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Log.d(TAG, "onCreate: Toolbar initialized");

        // Initialize views
        Log.d(TAG, "onCreate: Initializing views");
        nameTextView = findViewById(R.id.tv_admin_name);
        emailTextView = findViewById(R.id.tv_admin_email);
        roleTextView = findViewById(R.id.tv_admin_role);
        phoneTextView = findViewById(R.id.tv_admin_phone);
        logout = findViewById(R.id.btn_admin_logout);
        Log.d(TAG, "onCreate: Views initialized, nameTextView=" + (nameTextView != null ? "not null" : "null"));

        // Set logout button listener
        Log.d(TAG, "onCreate: Setting logout button listener");
        logout.setOnClickListener(v -> {
            Log.d(TAG, "logoutButton: Logout button clicked");
            mAuth.signOut();
            Log.d(TAG, "logoutButton: User signed out");
            Toast.makeText(AdminProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Log.d(TAG, "logoutButton: Navigated to LoginActivity");
        });

        // Load admin data
        Log.d(TAG, "onCreate: Calling loadAdminData");
        loadAdminData();
        Log.d(TAG, "onCreate: AdminProfileActivity setup complete");
    }

    @Override
    public void onBackPressed() {
        // Optional: Sign out (if needed)
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();

        // Navigate to LoginActivity and clear the back stack
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();  // Destroy AdminActivity
    }

    private void loadAdminData() {
        Log.d(TAG, "loadAdminData: Entered");
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "loadAdminData: Current user=" + (user != null ? user.getEmail() : "null"));
        if (user == null) {
            Log.e(TAG, "loadAdminData: User not logged in");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userEmail = user.getEmail();
        Log.d(TAG, "loadAdminData: User email=" + userEmail);
        if (userEmail == null) {
            Log.e(TAG, "loadAdminData: User email not available");
            Toast.makeText(this, "User email not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Query the AdminData node
        Log.d(TAG, "loadAdminData: Querying AdminData node");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "loadAdminData: onDataChange called");
                Log.d(TAG, "loadAdminData: AdminData snapshot value=" + (snapshot.getValue() != null ? snapshot.getValue().toString() : "null"));

                if (!snapshot.exists()) {
                    Log.e(TAG, "loadAdminData: No data found in AdminData node");
                    Toast.makeText(AdminProfileActivity.this, "No admin data found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                boolean adminFound = false;
                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    Log.d(TAG, "loadAdminData: Checking admin snapshot");
                    // Safely retrieve admin data as a Map
                    Map<String, Object> adminData = (Map<String, Object>) adminSnapshot.getValue();
                    if (adminData != null) {
                        String adminId = (String) adminData.get("adminId");
                        String email = (String) adminData.get("email");
                        String name = (String) adminData.get("name");
                        String role = (String) adminData.get("role");
                        String phone = (String) adminData.get("phone");
                        String dateAdded = (String) adminData.get("dateAdded");

                        // Log all fields of the admin data
                        Log.d(TAG, "loadAdminData: Admin data - adminId=" + adminId + ", email=" + email +
                                ", name=" + name + ", role=" + role + ", phone=" + phone +
                                ", dateAdded=" + dateAdded);

                        if (userEmail.equals(email)) {
                            Log.d(TAG, "loadAdminData: Admin found - name=" + name + ", role=" + role + ", phone=" + phone);
                            // Update UI with admin data, using "N/A" for null values
                            nameTextView.setText(name != null ? name : "N/A");
                            emailTextView.setText(email != null ? email : "N/A");
                            roleTextView.setText(role != null ? role : "N/A");
                            phoneTextView.setText(phone != null ? phone : "N/A");
                            adminFound = true;
                            Log.d(TAG, "loadAdminData: UI updated with admin data");
                            break;
                        }
                    } else {
                        Log.w(TAG, "loadAdminData: Admin snapshot data is null");
                    }
                }

                if (!adminFound) {
                    Log.e(TAG, "loadAdminData: No admin found for email: " + userEmail);
                    Toast.makeText(AdminProfileActivity.this, "No admin found for email: " + userEmail, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "loadAdminData: Database error: " + error.getMessage());
                Toast.makeText(AdminProfileActivity.this, "Failed to load admin data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Log.d(TAG, "loadAdminData: Query initiated");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbRef != null && adminDataListener != null) {
            dbRef.removeEventListener(adminDataListener);  // Remove listener
        }
    }


}