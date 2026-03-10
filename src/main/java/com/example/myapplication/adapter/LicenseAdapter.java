package com.example.myapplication.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.activity.LRequestReviewActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An adapter class for displaying license application data in a RecyclerView.
 * This adapter binds license application data to view holders and handles user interactions.
 * Each item displays applicant name, license type, submission date, and provides
 * a button to review the application details.
 * @author u8007015 Arjun Satish
 */
public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.LicenseViewHolder> {

    private List<Map<String, String>> applicationList;

    /**
     * Constructor.
     *
     * @param applicationList List of license request maps to bind to the UI.
     */
    public LicenseAdapter(List<Map<String, String>> applicationList) {
        this.applicationList = applicationList;
    }

    public static void bindToFakeViews(FakeTextView tvName, FakeTextView tvType, FakeTextView tvId, Map<String, String> data) {
        tvName.setText("Name: " + data.get("name"));
        tvType.setText("License Type: " + data.get("licenseType"));
        tvId.setText("Request ID: " + data.get("randomId"));
    }

    @NonNull
    @Override
    public LicenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_license, parent, false);
        return new LicenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LicenseViewHolder holder, int position) {
        Map<String, String> application = applicationList.get(position);
        holder.tvName.setText("Name: " + application.get("name"));
        holder.tvLicenseType.setText("License Type: " + application.get("licenseType"));

        String dateCreated = application.get("dateCreated");
        String formattedDate = dateCreated;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a");
            Date date = inputFormat.parse(dateCreated);
            formattedDate = outputFormat.format(date); // e.g., "May 07, 2025 2:30 PM"
        } catch (ParseException e) {
            Log.e("LicenseAdapter", "Failed to parse date: " + e.getMessage());
        }

        //holder.tvDateSubmitted.setText("Date Submitted: " + application.get("dateCreated"));
        holder.tvDateSubmitted.setText("Received at " + formattedDate);

        holder.btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LRequestReviewActivity.class);
            intent.putExtra("requestId", application.get("requestId"));
            intent.putExtra("name", application.get("name"));
            intent.putExtra("licenseType", application.get("licenseType"));
            intent.putExtra("certificateNo", application.get("certificateNo"));
            intent.putExtra("dateCreated", application.get("dateCreated"));
            intent.putExtra("contact_email", application.get("contact_email"));
            intent.putExtra("contact_phone", application.get("contact_phone"));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    /**
     * ViewHolder class for license items.
     */
    public static class LicenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLicenseType, tvDateSubmitted;
        Button btnReview;

        public LicenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLicenseType = itemView.findViewById(R.id.tv_license_type);
            tvDateSubmitted = itemView.findViewById(R.id.tv_date_submitted);
            btnReview = itemView.findViewById(R.id.btn_review);
        }
    }
}
