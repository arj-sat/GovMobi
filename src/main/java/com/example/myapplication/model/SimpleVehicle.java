package com.example.myapplication.model;

/**
 * SimpleVehicle is a lightweight model used for quick reference in search or summary views.
 * Contains minimal fields such as registration number, status, and expiry.
 *
 * Commonly used in SmartSearchActivity or public listings.
 *
 * @author
 * u7884012 - Ruoheng Feng
 */
public class SimpleVehicle {
    private String rego;
    private String status;
    private String expiry;

    /**
     * Constructor.
     *
     * @param rego   Registration number (license plate).
     * @param status Registration status (e.g., VALID, EXPIRED).
     * @param expiry Expiry date.
     */
    public SimpleVehicle(String rego, String status, String expiry) {
        this.rego = rego;
        this.status = status;
        this.expiry = expiry;
    }

    public String getRego() { return rego; }
    public String getStatus() { return status; }
    public String getExpiry() { return expiry; }
}
