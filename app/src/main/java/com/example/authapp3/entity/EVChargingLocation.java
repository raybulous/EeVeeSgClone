package com.example.authapp3.entity;

public class EVChargingLocation {
    private String address;
    private String stationName;
    private String company;
    private double longitude;
    private double latitude;
    private boolean hasRental;

    public EVChargingLocation()
    {

    }

    public EVChargingLocation(String address, String stationName, String company, double longitude, double latitude, boolean hasRental) {
        this.address = address;
        this.stationName = stationName;
        this.company = company;
        this.longitude = longitude;
        this.latitude = latitude;
        this.hasRental = hasRental;
    }

    public String getAddress()
    {
        return address;
    }

    public String getStationName()
    {
        return stationName;
    }

    public String getCompany()
    {
        return company;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isHasRental() {
        return hasRental;
    }
}
