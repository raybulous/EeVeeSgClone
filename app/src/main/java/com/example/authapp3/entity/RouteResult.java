package com.example.authapp3.entity;

public class RouteResult {
    private String name;
    private String address;
    private String distance;
    private String estimatedCharge;
    private String estimatedTime;

    public RouteResult(String name, String address, String distance, String estimatedCharge, String estimatedTime) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.estimatedCharge = estimatedCharge;
        this.estimatedTime = estimatedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEstimatedCharge() {
        return estimatedCharge;
    }

    public void setEstimatedCharge(String estimatedCharge) {
        this.estimatedCharge = estimatedCharge;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
