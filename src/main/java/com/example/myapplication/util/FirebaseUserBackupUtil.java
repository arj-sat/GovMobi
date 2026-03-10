package com.example.myapplication.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for managing Firebase user data backup and restoration.
 * This class provides methods for:
 * - Creating backups of user data from Firebase
 * - Restoring user data from backup files
 * - Handling backup and restore operations with timeout protection
 *
 * @author u7884012 - Ruoheng Feng
 */
public class FirebaseUserBackupUtil {

    /** Tag used for logging backup and restore operations */
    private static final String TAG = "FirebaseBackup";

    /** Name of the backup file used for storing user data */
    private static final String BACKUP_FILE = "firebase_user_backup.json";

    /**
     * Creates a backup of the current user's data from Firebase.
     * Retrieves all user data from the Firebase database and saves it to a local JSON file.
     * The operation includes timeout protection and error handling.
     *
     * @param context The application context used for file operations
     * @throws IllegalStateException if no user is currently logged in
     * @throws RuntimeException if backup operation fails or times out
     * @throws InterruptedException if the backup operation is interrupted
     */
    public static void backupFirebaseUser(Context context) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) throw new IllegalStateException("User not logged in");

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Userdata").child(user.getUid());

        CountDownLatch latch = new CountDownLatch(1);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data == null) data = new HashMap<>();

                    JSONObject json = new JSONObject(data);
                    File file = new File(context.getFilesDir(), BACKUP_FILE);
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(json.toString(2));
                    }

                    Log.d(TAG, "Backup saved to " + file.getAbsolutePath());
                } catch (Exception e) {
                    throw new RuntimeException("Backup failed: " + e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                throw new RuntimeException("Database error: " + error.getMessage());
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Firebase backup timeout");
        }
    }

    /**
     * Restores user data from a backup file to Firebase.
     * Reads the backup file and uploads the data to the user's Firebase node.
     * The operation includes timeout protection and error handling.
     *
     * @param context The application context used for file operations
     * @throws IllegalStateException if no user is currently logged in
     * @throws RuntimeException if restore operation fails or times out
     * @throws InterruptedException if the restore operation is interrupted
     */
    public static void restoreFirebaseUser(Context context) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) throw new IllegalStateException("User not logged in");

        File file = new File(context.getFilesDir(), BACKUP_FILE);
        if (!file.exists()) {
            Log.w(TAG, "No backup file found to restore.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }

        // Convert JSON to Map (avoid Firebase errors caused by JSONObject)
        String jsonString = builder.toString();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Userdata")
                .child(user.getUid());

        CountDownLatch latch = new CountDownLatch(1);
        ref.setValue(map)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Firebase user data restored");
                    latch.countDown();
                })
                .addOnFailureListener(e -> {
                    throw new RuntimeException("Restore failed: " + e.getMessage(), e);
                });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Firebase restore timeout");
        }
    }

}
