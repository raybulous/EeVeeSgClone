package com.example.authapp3.entity;

public class evChargingLocation {
    private String address;
    private String stationName;
    private String company;

    public evChargingLocation(String stationName, String address, String company)
    {
        this.stationName = stationName;
        this.address = address;
        this.company = company;
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
}
