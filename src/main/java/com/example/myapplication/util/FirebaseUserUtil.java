package com.example.myapplication.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.*;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility class for managing Firebase user data operations.
 * This class provides methods for synchronizing user data between Firebase and local storage,
 * including:
 * - Fetching user data from Firebase and saving to local storage
 * - Uploading user data from local storage to Firebase
 * - Handling data synchronization callbacks
 *
 * @author u7884012 - Ruoheng Feng
 */
public class FirebaseUserUtil {

    /** Tag used for logging user data operations */
    private static final String TAG = "USERDATA_UTIL";

    /**
     * Fetches user data from Firebase and saves it to local storage.
     * Retrieves data from /Userdata/{uid} and stores it as temp_userdata.json.
     * The operation is asynchronous and provides a callback mechanism.
     *
     * @param context The application context used for file operations
     * @param user The Firebase user whose data is to be fetched
     * @param onComplete Callback to be executed after the operation completes (success or failure)
     */
    public static void fetchAndSaveUserData(Context context, FirebaseUser user, Runnable onComplete) {
        if (user == null) {
            Log.e(TAG, "User is null");
            return;
        }

        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Userdata").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object data = snapshot.getValue();
                String json = new Gson().toJson(data);

                try {
                    FileOutputStream fos = context.openFileOutput("temp_userdata.json", Context.MODE_PRIVATE);
                    fos.write(json.getBytes(StandardCharsets.UTF_8));
                    fos.close();
                    Log.d(TAG, "Saved user data to temp_userdata.json");
                    Log.d(TAG, "JSON: " + json);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to write user data: " + e.getMessage());
                }

                if (onComplete != null) onComplete.run();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                if (onComplete != null) onComplete.run();
            }
        });
    }

    /**
     * Uploads user data from local storage to Firebase.
     * Updates the user's data in the Firebase database and provides feedback through callbacks.
     * Shows a toast message if the upload fails.
     *
     * @param context The application context used for displaying toast messages
     * @param data The user data map to be uploaded to Firebase
     * @param onComplete Callback to be executed after the upload completes (success or failure)
     */
    public static void uploadUserData(Context context, Map<String, Object> data, Runnable onComplete) {
        FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User is null, cannot upload data.");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Userdata")
                .child(user.getUid());

        ref.setValue(data)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "User data uploaded to Firebase.");
                    if (onComplete != null) onComplete.run();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to upload user data: " + e.getMessage());
                    Toast.makeText(context, "Failed to sync with server", Toast.LENGTH_SHORT).show();
                });
    }

}
