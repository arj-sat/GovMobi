package com.example.myapplication.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Simulates periodic license request submissions to Firebase.
 * Generates artificial license requests every 30 seconds and adds them to the database.
 *  Implementation of Datastream
 * @author  u8007015 Arjun Satish
 */
public class LicenseRequestSimulator {
    private static final String TAG = "LicenseRequestSimulator";
    private final DatabaseReference dbRef;
    private final Handler handler;
    private final Random random;
    private final Context context;
    private boolean isRunning;
    private int delay;

    private final String[] names = {"John Doe", "Rahul Sanker", " Anjali T", "Jane Smith", "Alex Johnson", "Emily Brown", "Michael Chen", "Clint Wright", " Maddie Brown", "Victoria Wilson", "Deeksha Singh","Tom Sawyer"};
    private final String[] licenseTypes = {"Learner", "Provisional 1", "Provisional 2", "Full"};
    private final String[] remarks = {"First time applicant", "Renewal request", "Additional certification", "Resubmission"};

    public LicenseRequestSimulator(Context context, int delay) {
        this.context = context;
        this.dbRef = DatabaseSingleton.getInstance(context)
                .getDBReference()
                .getDatabase()
                .getReference("LicenseRequests");
        this.handler = new Handler(Looper.getMainLooper());
        this.random = new Random();
        this.isRunning = false;
        this.delay = delay*1000  ;
        Log.d(TAG, "Initialized LicenseRequestSimulator");
    }

    /**
     * Starts the simulation, adding a new license request every 30 seconds.
     */
    public void start() {
        if (isRunning) {
            Log.d(TAG, "Simulator already running");
            return;
        }
        isRunning = true;
        scheduleNextRequest();
        Log.d(TAG, "Simulator started");
    }

    public boolean getRunning(){
        return isRunning;
    }

    /**
     * Stops the simulation.
     */
    public void stop() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        Log.d(TAG, "Simulator stopped");
    }

    /**
     * Schedules the next license request submission.
     */
    private void scheduleNextRequest() {
        if (!isRunning) return;

        handler.postDelayed(() -> {
            addLicenseRequest();
            scheduleNextRequest();
        }, delay); // 30 seconds
    }

    /**
     * Generates and adds a simulated license request to Firebase.
     */
    private void addLicenseRequest() {
        String requestId = new IdGenerator().getid();
        String name = names[random.nextInt(names.length)];
        String age = String.valueOf(18 + random.nextInt(42)); // Age 18-60
        String contactNumber = "04" + String.format("%08d", random.nextInt(100000000));
        String certificateNo = "CERT" + String.format("%06d", random.nextInt(1000000));
        String licenseType = licenseTypes[random.nextInt(licenseTypes.length)];
        //String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String currentDate= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());

        String remarks = this.remarks[random.nextInt(this.remarks.length)];
        String email = name.toLowerCase().replace(" ", ".") + "@example.com";

        // Prepare application data
        Map<String, Object> application = new HashMap<>();
        application.put("requestId", requestId);
        application.put("name", name);
        application.put("age", age);
        application.put("email", email);
        application.put("contactNumber", contactNumber);
        application.put("certificateNo", certificateNo);
        application.put("licenseType", licenseType);
        application.put("dateCreated", currentDate);
        application.put("remarks", remarks);
        application.put("status", "Pending");
        application.put("admin_remarks", "");

        // Submit to Firebase
        DatabaseReference applicationRef = dbRef.child(requestId);
        applicationRef.setValue(application)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Simulated license request added, ID: " + requestId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add simulated request: " + e.getMessage());
                });
    }

    /**
     * Immediately inserts 3 mock license requests for testing purposes.
     */
    public void insertMockRequestsForTest() {
        Log.d(TAG, "insertMockRequestsForTest: inserting 3 test license requests immediately");
        for (int i = 0; i < 3; i++) {
            addLicenseRequest();  // uses randomized mock data
        }
    }
}


