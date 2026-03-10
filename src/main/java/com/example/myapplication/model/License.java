package com.example.myapplication.model;

/**
 * License represents a user’s license record stored in Firebase.
 * Contains metadata like license type, number, status, and expiry.
 * This model supports Firebase and Gson serialization.
 *
 * Used in user dashboards and admin review screens.
 *
 * @author
 * u7991805 - Janvi Rajendra Nandre
 */
public class License {
    private String licenseId;
    private String licenseType;
    private String licenseNumber;
    private String expiryDate;
    private String status;
    private String userId;

    /**
     * Default constructor required for Firebase/Gson.
     */
    public License() {}

    /**
     * Parameterized constructor for custom creation.
     *
     * @param licenseId      Unique ID of the license.
     * @param licenseType    Type of the license (e.g., Driving, Commercial).
     * @param licenseNumber  Actual license number.
     * @param expiryDate     Expiry date in readable format.
     * @param status         Status string (e.g., Active, Expired).
     */
    public License(String licenseId, String licenseType, String licenseNumber, String expiryDate, String status) {
        this.licenseId = licenseId;
        this.licenseType = licenseType;
        this.licenseNumber = licenseNumber;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // Getters and setters
    public String getLicenseId() { return licenseId; }
    public void setLicenseId(String licenseId) { this.licenseId = licenseId; }

    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
