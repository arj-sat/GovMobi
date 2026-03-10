package com.example.myapplication.activity;

import android.content.Intent;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.LicenseAdapter;
import com.example.myapplication.util.DatabaseSingleton;
import com.example.myapplication.util.LicenseRequestSimulator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminActivity - Main activity for admin users that displays pending license requests.
 * This activity shows a list of pending license applications that need admin action.
 * Admins can view pending requests, approve/reject/request for more information.
 * License requests are added every 10 sec by a simulation agent (Datastream)
 * The admin can also view their profile page and logout as well.
 * @author  u8007015 Arjun Satish
 */

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "AdminActivity";
    private LicenseAdapter adapter;
    private List<Map<String, String>> applicationList;
    private DatabaseReference dbRef;
    private TextView pendingCountText;
    private ImageView profileIcon;
    private int pendingNotifications = 0;
    private LicenseRequestSimulator simulator;
    private int lastChildCount = 0;

    @Override
    protected void onStop() {
        super.onStop();
        if (simulator != null) {
            simulator.stop();
            Log.d(TAG, "Simulator stopped in onStop");
        }
    }

    protected void onStart() {
        super.onStart();
        if (simulator != null && !simulator.getRunning()) {
            simulator.start();
            Log.d(TAG, "Simulator restarted in onStart");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting AdminActivity");
        setContentView(R.layout.activity_admin);

        // Initialize Firebase using Singleton
        Log.d(TAG, "onCreate: Initializing Firebase");
        dbRef = DatabaseSingleton.getInstance(this)
                .getDBReference()
                .getDatabase()
                .getReference("LicenseRequests");
        //Log.d(TAG, "onCreate: Firebase initialized, dbRef=" + (dbRef != null ? "not null" : "null"));

        // Initialize views
        Log.d(TAG, "onCreate: Initializing views");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.rv_pending_requests);
        pendingCountText = findViewById(R.id.tv_pending_count);
        profileIcon = findViewById(R.id.iv_profile_icon);
        //notificationIcon = findViewById(R.id.iv_notification_icon);
        Log.d(TAG, "onCreate: Views initialized, profileIcon=" + (profileIcon != null ? "not null" : "null"));

        // Initialize RecyclerView
        applicationList = new ArrayList<>();
        adapter = new LicenseAdapter(applicationList);

//        LinearLayoutManager lm = new LinearLayoutManager((this));
//        lm.setReverseLayout(true);
//        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Starting simulator
        Log.d(TAG, "onCreate: Initializing LicenseRequestSimulator");
        int delay_time = 10; // sec//10
        simulator = new LicenseRequestSimulator(this, delay_time);
        simulator.start();


        // Fetch license requests
        fetchRequests();

        // Profile icon click listener
        Log.d(TAG, "onCreate: Setting profile icon click listener");
        profileIcon.setOnClickListener(v -> {
            Log.d(TAG, "Profile icon clicked");
            launchProfileActivity();
        });

        Log.d(TAG, "onCreate: AdminActivity setup complete");
    }


    /**
     * Stop adding license requets when admin page is destroyed
     */
    protected  void onDestroy(){
        super.onDestroy();
        if(simulator!=null){
            simulator.stop();
            Log.d(TAG,"Simulator stopped");
        }
    }

    @Override
    public void onBackPressed() {
        // Sign out and navigate to LoginActivity
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
       finish();
        }


    /**
     * Fetches license requests from Firebase real-time database.
     * Filters requests to only show those with 'Pending' status or no status.
     * Updates the RecyclerView adapter with the filtered list.
     */
    private void fetchRequests() {
        Log.d(TAG, "fetchRequests: Querying LicenseRequests for pending requests");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Received data snapshot, child count=" + dataSnapshot.getChildrenCount());
                applicationList.clear();
                pendingNotifications = 0;



                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> application = new HashMap<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String key = child.getKey();
                        Object value = child.getValue();
                        if (value != null) {
                            if ("contact".equals(key)) {
                                // Handle nested contact object
                                DataSnapshot contactSnapshot = child;
                                String email = contactSnapshot.child("email").getValue(String.class);
                                String phone = contactSnapshot.child("phone").getValue(String.class);
                                application.put("contact_email", email != null ? email : "");
                                application.put("contact_phone", phone != null ? phone : "");
                            } else {
                                // Convert value to String, handling different types
                                application.put(key, value.toString());
                                if("dateCreated".equals(key))  {
                                    Log.d(TAG,"Value: " + value);
                                }
                            }
                        } else {
                            application.put(key, "");
                        }
                        Log.d(TAG, "onDataChange: Key=" + key + ", Value=" + (value != null ? value.toString() : "null"));
                    }
                    // Filter for pending requests or missing status
                    String status = application.get("status");
                    if ( "Pending".equals(status)) {
                        applicationList.add(application);
                        Log.d(TAG, application.get("name") + " added");
                        pendingNotifications++;
                    }
                    Log.d(TAG,"Length of applicationnlist: " + applicationList.size());
                }

                // Sort applicationList by dateCreated in descending order

                Collections.sort(applicationList, (app1, app2) -> {
                    String date1 = app1.get("dateCreated");
                    String date2 = app2.get("dateCreated");

                    // Handle null cases first
                    if (date1 == null && date2 == null) return 0;
                    if (date1 == null) return 1;  // Nulls last
                    if (date2 == null) return -1; // Nulls last

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Date d1 = sdf.parse(date1);
                        Date d2 = sdf.parse(date2);
                        return d2.compareTo(d1); // Descending order
                    } catch (ParseException e) {
                        Log.e(TAG, "Failed to parse dates: " + e.getMessage());
                        return date1.compareTo(date2); // Fallback to string comparison
                    }
                });
                Log.d(TAG, "applicationList sorted");

                // Log the name field of each Map in sorted applicationList
                Log.d(TAG, "Printing sorted applicationList names:");
                for (Map<String, String> app : applicationList) {
                    String name = app.get("name") != null ? app.get("name") : "null";
                    Log.d(TAG, "Name: " + name);
                }

                // Check for new requests
                if (dataSnapshot.getChildrenCount() > lastChildCount && lastChildCount > 0) {
                    showCustomToast("New license request added");
                }
                lastChildCount = (int) dataSnapshot.getChildrenCount();

                adapter.notifyDataSetChanged();
                pendingCountText.setText("Pending Requests: " + pendingNotifications);
                Log.d(TAG, "onDataChange: Updated list with " + pendingNotifications + " pending requests");
                if (pendingNotifications == 0) {
                    Toast.makeText(AdminActivity.this, "No pending requests found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Database error - " + databaseError.getMessage());
                Toast.makeText(AdminActivity.this, "Failed to load requests: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Launches the AdminProfileActivity when profile icon is clicked.
     * This method handles the navigation to the admin profile screen.
     */
    private void launchProfileActivity() {
        Log.d(TAG, "launchProfileActivity: Creating Intent for AdminProfileActivity");
        Intent intent = new Intent(AdminActivity.this, AdminProfileActivity.class);
        Log.d(TAG, "launchProfileActivity: Starting AdminProfileActivity");
        startActivity(intent);
        Log.d(TAG, "launchProfileActivity: Intent started");
    }

    /**
     * Display pop up when a new license request is added.
     * @param message
     */
    private void showCustomToast(String message) {
        Toast toast = new Toast(this);
        TextView toastTextView = new TextView(this);
        toastTextView.setText(message);
        toastTextView.setTextSize(15); // Larger text size (in sp)
        toastTextView.setTextColor(getResources().getColor(android.R.color.white));
        toastTextView.setPadding(30, 20, 30, 20); // Padding for better appearance
        toastTextView.setTypeface(null, Typeface.BOLD);

        // Create rounded background with purple_500 from colors.xml
        GradientDrawable background = new GradientDrawable();
        background.setColor(ContextCompat.getColor(this, R.color.purple_500)); // Use purple_500
        background.setCornerRadius(40);
        toastTextView.setBackground(background);

        toast.setView(toastTextView);
        toast.setDuration(Toast.LENGTH_SHORT);
        // Position at top-middle (100 pixels from top)
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 350);
        toast.show();
        Log.d(TAG, "showCustomToast: Displayed custom Toast with message: " + message);
    }
}