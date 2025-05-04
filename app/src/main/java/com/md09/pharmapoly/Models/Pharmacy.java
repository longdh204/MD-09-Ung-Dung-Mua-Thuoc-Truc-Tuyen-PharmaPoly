package com.md09.pharmapoly.Models;

public class Pharmacy {
    private String name;
    private String location;
    private double distance;
    private double latitude;
    private double longitude;

    public Pharmacy(String name, String location, double distance, double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}