package com.example.myapplication.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.util.DatabaseSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * LRequestReviewActivity - Activity for administrators to review and process license requests.
 * Allows approving, rejecting, or requesting more evidence for license applications.
 * Displays detailed request information and enables status updates with admin remarks.
 * @author  u8007015 Arjun Satish
 */

public class LRequestReviewActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private String requestId;
    private String nodeKey; // To store the actual node key (e.g., "0", "1")
    private static final String TAG = "LRequestReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_review);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Check authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "User not authenticated");
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize Firebase
        dbRef = DatabaseSingleton.getInstance(this)
                .getDBReference()
                .getDatabase()
                .getReference("LicenseRequests");
        Log.d(TAG, "Database reference initialized: " + dbRef);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(TAG, "No intent extras provided");
            Toast.makeText(this, "Error: No request data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        requestId = extras.getString("requestId");
        Log.d(TAG, "Received requestId: " + requestId);
        if (requestId == null || requestId.trim().isEmpty()) {
            Log.e(TAG, "Invalid requestId: null or empty");
            Toast.makeText(this, "Error: Invalid request ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Query to find the node with matching requestId
        Query query = dbRef.orderByChild("requestId").equalTo(requestId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // There should be only one matching node
                    for (DataSnapshot child : snapshot.getChildren()) {
                        nodeKey = child.getKey();
                        Log.d(TAG, "Found node key: " + nodeKey + " for requestId: " + requestId);
                        initializeViews(extras);
                        return;
                    }
                } else {
                    Log.e(TAG, "No request found with requestId: " + requestId);
                    Toast.makeText(LRequestReviewActivity.this, "Error: Request ID " + requestId + " not found", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to query request " + requestId + ": " + error.getMessage());
                Toast.makeText(LRequestReviewActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void initializeViews(Bundle extras) {
        String name = extras.getString("name");
        String licenseType = extras.getString("licenseType");
        String dateCreated = extras.getString("dateCreated");
        String email = extras.getString("contact_email");
        String phone = extras.getString("contact_phone");
        String certNo = extras.getString("certificateNo");

        // Initialize views
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvLicenseType = findViewById(R.id.tv_license_type);
        TextView tvDateSubmitted = findViewById(R.id.tv_date_submitted);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvPhone = findViewById(R.id.tv_phone);
        TextView tvCert = findViewById(R.id.tv_cert);
        EditText etRemarks = findViewById(R.id.et_remarks);
        Button btnApprove = findViewById(R.id.btn_approve);
        Button btnReject = findViewById(R.id.btn_reject);
        Button btnRequestMore = findViewById(R.id.btn_request_more);

        // Set data to views
        tvName.setText(createSpannableText("Name: ", name));
        tvLicenseType.setText(createSpannableText("License Type: ", licenseType));
        tvDateSubmitted.setText(createSpannableText("Date Submitted: ", dateCreated));
        tvEmail.setText(createSpannableText("Email: ", email));
        tvPhone.setText(createSpannableText("Phone: ", phone));
        tvCert.setText(createSpannableText("Certificate: ", certNo));

        // Button listeners
        btnApprove.setOnClickListener(v -> {
            Log.d(TAG, "Approve button clicked for requestId: " + requestId + ", nodeKey: " + nodeKey);
            updateRequestStatus("Approved", etRemarks.getText().toString());
        });
        btnReject.setOnClickListener(v -> {
            Log.d(TAG, "Reject button clicked for requestId: " + requestId + ", nodeKey: " + nodeKey);
            updateRequestStatus("Rejected", etRemarks.getText().toString());
        });
        btnRequestMore.setOnClickListener(v -> {
            Log.d(TAG, "Request More button clicked for requestId: " + requestId + ", nodeKey: " + nodeKey);
            updateRequestStatus("Awaiting evidence", etRemarks.getText().toString());
        });
    }

    private SpannableString createSpannableText(String label, String value) {
        String fullText = label + (value != null ? value : "");
        SpannableString spannable = new SpannableString(fullText);
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, label.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * Updates the request status in Firebase database.
     *
     * @param status The new status ("Approved", "Rejected", or "Awaiting evidence")
     * @param remarks Admin remarks to be stored with the request
     */
    private void updateRequestStatus(String status, String remarks) {
        if (nodeKey == null) {
            Log.e(TAG, "Cannot update: nodeKey is null for requestId: " + requestId);
            Toast.makeText(this, "Error: Invalid request node", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Updating path: LicenseRequests/" + nodeKey + " for requestId: " + requestId + " to status: " + status);

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("admin_remarks", remarks != null ? remarks : "");

        dbRef.child(nodeKey).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully updated request " + requestId + " (node: " + nodeKey + ") to status: " + status);
                    Toast.makeText(this, "Request " + status.toLowerCase() + " successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update request " + requestId + " (node: " + nodeKey + "): " + e.getMessage(), e);
                    Toast.makeText(this, "Failed to update request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}