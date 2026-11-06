package com.apepdcl.gis_survey.METER_SURVEY;


/**
 * Created by SOUBHAGYA SWAIN on 09-06-2026.
 */
public class ServiceData {

    private String serviceNumber = "";
    private String serviceName = "";
    private String ukscNumber = "";
    private String imagePath = "";
    private double latitude = 0;
    private double longitude = 0;

    private boolean locationCaptured = false;

    private String imageUri = "";

    private double current_lat = 0;
    private double current_lon = 0;
    private double gps_accuracy = 0;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCurrent_lat() {
        return current_lat;
    }

    public void setCurrent_lat(double current_lat) {
        this.current_lat = current_lat;
    }

    public double getCurrent_lon() {
        return current_lon;
    }

    public void setCurrent_lon(double current_lon) {
        this.current_lon = current_lon;
    }

    public double getGps_accuracy() {
        return gps_accuracy;
    }

    public void setGps_accuracy(double gps_accuracy) {
        this.gps_accuracy = gps_accuracy;
    }

    public String getUkscNumber() {
        return ukscNumber;
    }

    public void setUkscNumber(String ukscNumber) {
        this.ukscNumber = ukscNumber;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isLocationCaptured() {
        return locationCaptured;
    }

    public void setLocationCaptured(boolean locationCaptured) {
        this.locationCaptured = locationCaptured;
    }
}
