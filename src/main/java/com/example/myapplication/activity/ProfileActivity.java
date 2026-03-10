package com.example.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.util.FirebaseUserUtil;
import com.example.myapplication.util.LocalDataUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ProfileActivity allows users to view and update their profile information.
 * Users can edit fields such as name, email, phone, license number, address, and date of birth.
 * Instead of uploading a custom photo, a random profile image from the drawable folder is assigned.
 *
 * The profile image index is saved in Firebase and used to restore the selected image on reload.
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class ProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPhone, editLicenseNumber, editAddress, editDOB;
    private Button buttonEdit, buttonSave, buttonChangePhoto;
    private ImageView profileImage;

    private boolean isEditMode = false;
    private ProgressDialog progressDialog;

    /** Stores the index of the selected profile image (1 to 5). */
    private int selectedProfileImageIndex = -1;

    /**
     * Initializes the activity and sets up all UI elements.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editLicenseNumber = findViewById(R.id.editLicenseNumber);
        editAddress = findViewById(R.id.editAddress);
        editDOB = findViewById(R.id.editDOB);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);
        profileImage = findViewById(R.id.profileImage);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");

        loadUserProfile();

        buttonEdit.setOnClickListener(v -> enableEditing(true));
        buttonSave.setOnClickListener(v -> saveProfileToFirebase());
        buttonChangePhoto.setOnClickListener(v -> selectRandomProfileImage());
    }

    /**
     * Loads the user's profile data from local storage and sets it to the UI fields.
     * Also sets the profile image based on saved profile image index.
     */
    private void loadUserProfile() {
        Map<String, Object> userMap = LocalDataUtil.loadUserMapFromTempJson(this);
        if (userMap == null || !userMap.containsKey("profile")) {
            Toast.makeText(this, "Profile not found in local data", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> profile = (Map<String, Object>) userMap.get("profile");

        editName.setText((String) profile.getOrDefault("name", ""));
        String cachedEmail = (String) profile.get("email");
        if (cachedEmail == null || cachedEmail.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            cachedEmail = (user != null) ? user.getEmail() : "";
        }
        editEmail.setText(cachedEmail);
        editPhone.setText((String) profile.getOrDefault("phone", ""));
        editLicenseNumber.setText((String) profile.getOrDefault("licenseNumber", ""));
        editDOB.setText((String) profile.getOrDefault("dob", ""));
        editAddress.setText((String) profile.getOrDefault("address", ""));

        Object indexObj = profile.get("profileImageIndex");
        if (indexObj instanceof Number) {
            selectedProfileImageIndex = ((Number) indexObj).intValue();
        }

        int resId = getResources().getIdentifier(
                selectedProfileImageIndex > 0 ? "profileimg" + selectedProfileImageIndex : "ic_profile",
                "drawable", getPackageName());

        Glide.with(this)
                .load(resId)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(profileImage);
    }

    /**
     * Enables or disables editing of profile fields and switches button visibility.
     *
     * @param enable true to allow editing, false to disable.
     */
    private void enableEditing(boolean enable) {
        isEditMode = enable;
        editName.setEnabled(enable);
        // Disable email editing
        editEmail.setEnabled(false);
        editEmail.setFocusable(false);
        editPhone.setEnabled(enable);
        editLicenseNumber.setEnabled(enable);
        editAddress.setEnabled(enable);
        editDOB.setEnabled(enable);

        buttonSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void selectRandomProfileImage() {
        // If it's the first time, start from image 1
        if (selectedProfileImageIndex < 1 || selectedProfileImageIndex >= 8) {
            selectedProfileImageIndex = 1;
        } else {
            selectedProfileImageIndex++;
        }

        // Build the drawable name dynamically (e.g., "profileimg2")
        String imageName = "profileimg" + selectedProfileImageIndex;

        // Get the resource ID from the drawable folder
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        // Fallback in case image not found
        if (resId == 0) {
            resId = R.drawable.ic_profile;
            Toast.makeText(this, "Image not found: " + imageName, Toast.LENGTH_SHORT).show();
        }

        // Load the image using Glide
        Glide.with(this)
                .load(resId)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(profileImage);


//        Log.d("PROFILE_IMAGE", "Selected: " + imageName + " (resId: " + resId + ")");
    }


    /**
     * Saves the current profile data to Firebase Realtime Database under the current user's UID.
     * Also saves the selected profile image index.
     */
    private void saveProfileToFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        Map<String, Object> profileMap = new HashMap<>();
        profileMap.put("name", editName.getText().toString());
        profileMap.put("email", editEmail.getText().toString());
        profileMap.put("phone", editPhone.getText().toString());
        profileMap.put("licenseNumber", editLicenseNumber.getText().toString());
        profileMap.put("dob", editDOB.getText().toString());
        profileMap.put("address", editAddress.getText().toString());
        profileMap.put("profileImageIndex", selectedProfileImageIndex);

        FirebaseDatabase.getInstance()
                .getReference("Userdata")
                .child(user.getUid())
                .child("profile")
                .setValue(profileMap)
                .addOnSuccessListener(aVoid -> {
                    FirebaseUserUtil.fetchAndSaveUserData(ProfileActivity.this, user, () -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                        enableEditing(false);
                        loadUserProfile();
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
