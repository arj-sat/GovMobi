package com.example.myapplication.activity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.util.DatabaseSingleton;
import com.example.myapplication.util.IdGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplyLicense - Activity for users to apply for driving license.
 * Handles both new applications and displays status of existing applications.
 * Manages form submission to Firebase and provides admin feedback.
 * @author  u8007015 Arjun Satish
 */

public class ApplyLicenseActivity extends AppCompatActivity {
    private LinearLayout formContainer, paymentContainer;
    private EditText etName, etAge, etContactNumber, reMarks, etCert, etTransactionReference;
    private Spinner dropdown;
    private DatabaseReference dbRef;
    private Button btnSubmit, btnApply, btnReapply, btnFinalSubmit, btnCancelPayment;
    private TextView tvApplicationStatus, tvApplicationStatusHeader, tvBankDetails;


    private static final String TAG = "ApplyLicense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_license);

        Log.d(TAG, "onCreate: Initializing Firebase");
        dbRef = DatabaseSingleton.getInstance(this)
                .getDBReference()
                .getDatabase()
                .getReference("LicenseRequests");
        Log.d(TAG, "onCreate: Firebase initialized, dbRef=" + (dbRef != null ? "not null" : "null"));

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.tb_applylicense);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Initialize UI components
        btnApply = findViewById(R.id.bt_applylicense);
        btnReapply = findViewById(R.id.bt_reapply);
        formContainer = findViewById(R.id.form_container);
        paymentContainer = findViewById(R.id.payment_container);
        etName = findViewById(R.id.f_name);
        etAge = findViewById(R.id.f_dob); // Reusing DOB field for age
        etCert = findViewById(R.id.f_cert);
        etContactNumber = findViewById(R.id.f_phone);
        reMarks = findViewById(R.id.f_remarks);
        etTransactionReference = findViewById(R.id.et_reference_number);
        tvBankDetails = findViewById(R.id.tv_bank_details);
        dropdown = findViewById(R.id.spinner_license_type);
        btnSubmit = findViewById(R.id.btn_submit);
        btnFinalSubmit = findViewById(R.id.btn_final_submit);
        btnCancelPayment = findViewById(R.id.btn_cancel_payment);
        Button cancelButton = findViewById(R.id.bt_cancel);
        tvApplicationStatus = findViewById(R.id.tv_status_message);
        tvApplicationStatusHeader = findViewById(R.id.tv_status_heading);

        // Set up bank details
        String bankDetails = "Bank: Example Bank\n" +
                "Account Number: 1234567890\n" +
                "BSB: 123-456\n" +
                "Account Name: License Authority\n" +
                "Amount: $50.00";
        tvBankDetails.setText(bankDetails);

        // Set up license type dropdown
        String[] licenses = {"Learner", "Provisional 1", "Provisional 2", "Full"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, licenses);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        dropdown.setAdapter(adapter);

        // Check for existing application
        checkExistingApplication();

        // Button listeners
        btnApply.setOnClickListener(v -> {
            tvApplicationStatus.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
            btnApply.setVisibility(View.GONE);
            paymentContainer.setVisibility(View.GONE);
        });

        btnReapply.setOnClickListener(v -> {
            tvApplicationStatus.setVisibility(View.GONE);
            tvApplicationStatusHeader.setVisibility(View.GONE);
            btnReapply.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
            btnApply.setVisibility(View.GONE);
        });

        btnSubmit.setOnClickListener(v -> submitForm());

        btnFinalSubmit.setOnClickListener(v -> submitPayment());

        btnCancelPayment.setOnClickListener(v -> {
            etTransactionReference.setText("");
            paymentContainer.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
        });

        cancelButton.setOnClickListener(v -> {
            etName.setText("");
            etAge.setText("");
            etContactNumber.setText("");
            reMarks.setText("");
            formContainer.setVisibility(View.GONE);
            btnApply.setVisibility(View.VISIBLE);
            tvApplicationStatus.setVisibility(View.VISIBLE);
            paymentContainer.setVisibility(View.GONE);
        });
    }

    /**
     * Checks if the current user has an existing license application.
     * Updates UI to show application status if found, or shows form if no application exists.
     */
    private void checkExistingApplication() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser != null && currentUser.getEmail() != null ? currentUser.getEmail() : "";
        if (email.isEmpty()) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "checkExistingApplication: Snapshot exists=" + dataSnapshot.exists() + ", child count=" + dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    // User has an existing application
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "checkExistingApplication: Processing snapshot key=" + snapshot.getKey());
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String key = child.getKey();
                            Object value = child.getValue();
                            Log.d(TAG, "checkExistingApplication: Child key=" + key + ", value=" + (value != null ? value.toString() : "null"));
                        }
                        String name = snapshot.child("name").getValue(String.class);
                        String status = snapshot.child("status").getValue(String.class);
                        String remarks = snapshot.child("admin_remarks").getValue(String.class);
                        String displayText;

                        if (status == null) {
                            status = "Unknown";
                        }

                        switch (status) {

                            case "Pending":
                                displayText = "Hello, " + (name != null ? name : "User") + ", you have an active license request which is under review. Please expect to hear back shortly.";
                                btnReapply.setVisibility(View.GONE);
                                btnApply.setVisibility(View.GONE);
                                break;
                            case "Awaiting evidence":
                                displayText = "Hello, " + (name != null ? name : "User") + ", you have an active license request which requires further evidence.\nThe reviewer has requested further evidence. Please submit them at the earliest.";
                                if (remarks != null && !remarks.isEmpty()) {
                                    displayText += "\nRemarks: " + remarks;
                                }
                                btnReapply.setVisibility(View.VISIBLE);
                                btnApply.setVisibility(View.GONE);
                                break;
                            case "Rejected":
                                displayText = "Hello, " + (name != null ? name : "User") + ", your application has been rejected.";
                                if (remarks != null && !remarks.isEmpty()) {
                                    displayText += "\nRemarks: " + remarks;
                                }
                                btnReapply.setVisibility(View.VISIBLE);
                                btnApply.setVisibility(View.GONE);
                                break;
                            case "Approved":
                                displayText = "Hello, " + (name != null ? name : "User") + ", your application has been approved. Please expect the system to reflect the new license within 5 working days.";
                                btnReapply.setVisibility(View.GONE);
                                btnApply.setVisibility(View.GONE);
                                break;
                            default:
                                displayText = "Hello, " + (name != null ? name : "User") + ", you have an active license request with status: " + status + ".";
                                if (remarks != null && !remarks.isEmpty()) {
                                    displayText += "\nRemarks: " + remarks;
                                }
                                btnReapply.setVisibility(View.GONE);
                                btnApply.setVisibility(View.GONE);
                                break;
                        }


                        String headerText = "Application Status: " + status + "\n";
                        tvApplicationStatusHeader.setText(headerText);

                        tvApplicationStatus.setText(displayText);
                        tvApplicationStatus.setVisibility(View.VISIBLE);
                        tvApplicationStatusHeader.setVisibility(View.VISIBLE);
                        formContainer.setVisibility(View.GONE);
                        btnApply.setVisibility(View.GONE);
                        paymentContainer.setVisibility(View.GONE);
                    }
                } else {
                    // No existing application
                    tvApplicationStatusHeader.setText("No active application. Click Apply to start.");
                    tvApplicationStatus.setVisibility(View.GONE);
                    formContainer.setVisibility(View.GONE);
                    btnApply.setVisibility(View.VISIBLE);
                    btnReapply.setVisibility(View.GONE);
                    paymentContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "checkExistingApplication: Database error - " + databaseError.getMessage());
                Toast.makeText(ApplyLicenseActivity.this, "Failed to check application: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handles form submission for new license applications.
     * Validates inputs, creates application data, and submits to Firebase.
     */
    private void submitForm() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();
        String cert = etCert.getText().toString().trim();
        String licenseType = dropdown.getSelectedItem().toString();
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String requestId = new IdGenerator().getid();
        String remarks = reMarks.getText().toString().trim();

        // Get current user's email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser != null && currentUser.getEmail() != null ? currentUser.getEmail() : "";

        // Validate inputs
        if (name.isEmpty() || age.isEmpty() || contactNumber.isEmpty() || licenseType.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "User email not found. Please sign in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare application data
        Map<String, Object> application = new HashMap<>();
        application.put("requestId", requestId);
        application.put("name", name);
        application.put("age", age);
        application.put("email", email);
        application.put("contactNumber", contactNumber);
        application.put("certificateNo", cert);
        application.put("licenseType", licenseType);
        application.put("dateCreated", currentDate);
        application.put("remarks", remarks);
        application.put("status", "Pending");
        application.put("admin_remarks", "");

        DatabaseReference applicationRef = dbRef.child(requestId);
        applicationRef.setValue(application)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Application saved. Proceed to payment.", Toast.LENGTH_LONG).show();
                    formContainer.setVisibility(View.GONE);
                    paymentContainer.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Handles submission of transaction reference.
     * Validates the input and proceeds to DashboardActivity without modifying Firebase.
     */
    private void submitPayment() {
        String transactionReference = etTransactionReference.getText().toString().trim();

        // Validate input
        if (transactionReference.isEmpty()) {
            Toast.makeText(this, "Please enter the transaction reference", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed to DashboardActivity
        Toast.makeText(this, "Payment process completed.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}