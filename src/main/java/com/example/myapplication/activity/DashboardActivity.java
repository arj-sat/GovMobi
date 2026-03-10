package com.example.myapplication.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.util.LocalDataUtil;
import com.example.myapplication.util.FirebaseUserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Map;
/**
 * This activity displays the user dashboard with multiple features
 * represented as clickable cards. Based on the selected card,
 * users are taken to different parts of the app.
 * It also shows the current user's name and provides logout and navigation features.
 *
 * Features include:
 * - Automatic data refresh every 10 seconds using Handler
 * - Manual refresh through refresh icon
 * - Grid layout of feature cards (adaptive to screen orientation)
 * - Real-time vehicle and license count display
 * - Navigation to all major app features (Vehicles, Licenses, Apply License, Vehicle History)
 * - Firebase data synchronization with local storage
 * - User profile management
 * - Help & Support access
 *
 * The dashboard serves as the main navigation hub of the application,
 * providing quick access to all major features while maintaining
 * real-time data synchronization with the server.
 *
 * @author u7991805 - Janvi Rajendra Nandre
 * @author u7884012 - Ruoheng Feng
 */


public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DASHBOARD_LOG";
    private final Handler handler = new Handler();
    private final Runnable autoRefresh = new Runnable() {
        @Override
        public void run() {
            buildCards();
            handler.postDelayed(this, 10000); // Refresh every 10 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set user name
        TextView userName = findViewById(R.id.user_name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
            userName.setText(name);
            Log.d(TAG, "User loaded: " + name);
        }

        buildCards();
        handler.postDelayed(autoRefresh, 10000);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseUserUtil.fetchAndSaveUserData(this, currentUser, () -> {
                Toast.makeText(this, "Data refreshed.", Toast.LENGTH_SHORT).show();
                buildCards(); // Refresh view immediately
            });
        }

        /**
         * Displays a simple toast when Help & Support is clicked.
         */
        findViewById(R.id.help_support_button).setOnClickListener(v -> {
            Log.d(TAG, "Help & Support clicked");
            //Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this, QAActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.button_logout).setOnClickListener(v -> {
            Log.d(TAG, "Logout button clicked");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });



        ImageView refreshIcon = findViewById(R.id.refresh_icon);
        refreshIcon.setOnClickListener(v -> {
            if (currentUser != null) {
                FirebaseUserUtil.fetchAndSaveUserData(this, currentUser, () -> {
                    Toast.makeText(this, "Data renewed.", Toast.LENGTH_SHORT).show();
                    buildCards();
                });
            } else {
                Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void buildCards() {
        ViewGroup gridLayout = findViewById(R.id.grid_layout);

        // Set GridLayout columns based on orientation
        if (gridLayout instanceof GridLayout) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((GridLayout) gridLayout).setColumnCount(2); // Two columns in landscape
            } else {
                ((GridLayout) gridLayout).setColumnCount(1); // One column in portrait
            }
        }

        gridLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        int vehicleCount = 0;
        int licenseCount = 0;
        Map<String, Object> fullData = LocalDataUtil.loadUserMapFromTempJson(this);
        if (fullData != null) {
            if (fullData.containsKey("vehicles")) {
                vehicleCount = ((Map<?, ?>) fullData.get("vehicles")).size();
            }
            if (fullData.containsKey("licenses")) {
                licenseCount = ((Map<?, ?>) fullData.get("licenses")).size();
            }
        }

        String[][] cardData = {
                {"My Vehicles", vehicleCount + " Registered", "ic_vehicle"},
                {"My Licenses", licenseCount + " Registered", "ic_license"},
                {"Apply License", "", "ic_calendar"},
                {"Vehicle History", "Check vehicle details", "ic_form"}
        };

        ImageView searchIcon = findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(v -> {
            Log.d(TAG, "Search icon clicked");
            startActivity(new Intent(DashboardActivity.this, SmartSearchActivity.class));
        });

        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
        });

        ImageView refreshIcon = findViewById(R.id.refresh_icon);
        refreshIcon.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                FirebaseUserUtil.fetchAndSaveUserData(this, currentUser, () -> {
                    Toast.makeText(this, "Data renewed.", Toast.LENGTH_SHORT).show();
                    buildCards();
                });
            } else {
                Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
            }
        });

        for (String[] item : cardData) {
            View card = inflater.inflate(R.layout.item_card, gridLayout, false);

            TextView title = card.findViewById(R.id.textTitle);
            TextView subtitle = card.findViewById(R.id.textSubtitle);
            ImageView icon = card.findViewById(R.id.imageIcon);

            title.setText(item[0]);
            subtitle.setText(item[1]);
            icon.setImageResource(getResources().getIdentifier(item[2], "drawable", getPackageName()));

            card.setOnClickListener(v -> handleCardClick(item[0]));
            gridLayout.addView(card);
        }
    }




    private void handleCardClick(String cardTitle) {
        Log.d(TAG, "Card clicked: " + cardTitle);
        switch (cardTitle) {
            case "My Vehicles":
                startActivity(new Intent(this, MyVehiclesActivity.class));
                break;
            case "Apply License":
                startActivity(new Intent(this, ApplyLicenseActivity.class));
                break;
            case "My Licenses":
                startActivity(new Intent(this, MyLicensesActivity.class));
                break;
            case "Vehicle History":
                startActivity(new Intent(this, VehicleStatusActivity.class));
                break;
            case "Renew Data": // Keep functionality, hide card
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    FirebaseUserUtil.fetchAndSaveUserData(this, currentUser, () -> {
                        Toast.makeText(this, "Data renewed.", Toast.LENGTH_SHORT).show();
                        buildCards();
                    });
                } else {
                    Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, cardTitle + " clicked", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoRefresh); // Avoid memory leak
    }
}
