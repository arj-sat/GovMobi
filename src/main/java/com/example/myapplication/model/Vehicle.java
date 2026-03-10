package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Vehicle is a detailed model representing all key attributes of a user's vehicle.
 * Includes registration, maintenance, and security info along with display preferences.
 *
 * Implements Serializable for Intent passing and supports Firebase/Gson compatibility.
 *
 * Used in vehicle detail, management, and AVLTree-based search features.
 *
 * @author
 * u7884012 - Ruoheng Feng
 * u7763790 - Vrushabh Vijay Dhoke
 */
public class Vehicle implements Serializable {
    private String vehicleName;
    private String licensePlate;
    private String vin;
    private String registrationStatus;
    private String registrationExpiry;
    private String lastMaintenance;
    private String lastMaintenanceStatus;
    private String stolenStatus;
    private String lastKnownLocation;
    private String visibility;

    /**
     * Full parameter constructor for manual instantiation.
     */
    public Vehicle(String vehicleName, String licensePlate, String vin, String registrationStatus,
                   String registrationExpiry, String lastMaintenance, String lastMaintenanceStatus,
                   String stolenStatus, String lastKnownLocation, String visibility) {
        this.vehicleName = vehicleName;
        this.licensePlate = licensePlate;
        this.vin = vin;
        this.registrationStatus = registrationStatus;
        this.registrationExpiry = registrationExpiry;
        this.lastMaintenance = lastMaintenance;
        this.lastMaintenanceStatus = lastMaintenanceStatus;
        this.stolenStatus = stolenStatus;
        this.lastKnownLocation = lastKnownLocation;
        this.visibility = (visibility != null) ? visibility : "Private";
    }

    /**
     * Default constructor for Firebase/Gson.
     */
    public Vehicle() {
        this.visibility = "Private";
    }

    /**
     * Constructor for deserializing from a Map (Firebase use case).
     *
     * @param map Map containing vehicle field keys and values.
     */
    public Vehicle(Map<String, Object> map) {
        this.vehicleName = (String) map.get("vehicleName");
        this.licensePlate = (String) map.get("licensePlate");
        this.vin = (String) map.get("vin");
        this.registrationStatus = (String) map.get("registrationStatus");
        this.registrationExpiry = (String) map.get("registrationExpiry");
        this.lastMaintenance = (String) map.get("lastMaintenance");
        this.lastMaintenanceStatus = (String) map.get("lastMaintenanceStatus");
        this.stolenStatus = (String) map.get("stolenStatus");
        this.lastKnownLocation = (String) map.get("lastKnownLocation");
        this.visibility = (String) map.getOrDefault("visibility", "Private");
    }

    // Getters
    public String getVehicleName() { return vehicleName; }
    public String getLicensePlate() { return licensePlate; }
    public String getVin() { return vin; }
    public String getRegistrationStatus() { return registrationStatus; }
    public String getRegistrationExpiry() { return registrationExpiry; }
    public String getLastMaintenance() { return lastMaintenance; }
    public String getLastMaintenanceStatus() { return lastMaintenanceStatus; }
    public String getStolenStatus() { return stolenStatus; }
    public String getLastKnownLocation() { return lastKnownLocation; }
    public String getVisibility() { return visibility; }

    // Setters
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setVin(String vin) { this.vin = vin; }
    public void setRegistrationStatus(String registrationStatus) { this.registrationStatus = registrationStatus; }
    public void setRegistrationExpiry(String registrationExpiry) { this.registrationExpiry = registrationExpiry; }
    public void setLastMaintenance(String lastMaintenance) { this.lastMaintenance = lastMaintenance; }
    public void setLastMaintenanceStatus(String lastMaintenanceStatus) { this.lastMaintenanceStatus = lastMaintenanceStatus; }
    public void setStolenStatus(String stolenStatus) { this.stolenStatus = stolenStatus; }
    public void setLastKnownLocation(String lastKnownLocation) { this.lastKnownLocation = lastKnownLocation; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    /**
     * Converts the vehicle to a Map for Firebase storage.
     *
     * @return Map with key-value pairs for all vehicle fields.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("vehicleName", vehicleName);
        map.put("licensePlate", licensePlate);
        map.put("vin", vin);
        map.put("registrationStatus", registrationStatus);
        map.put("registrationExpiry", registrationExpiry);
        map.put("lastMaintenance", lastMaintenance);
        map.put("lastMaintenanceStatus", lastMaintenanceStatus);
        map.put("stolenStatus", stolenStatus);
        map.put("lastKnownLocation", lastKnownLocation);
        map.put("visibility", visibility);
        return map;
    }
}
