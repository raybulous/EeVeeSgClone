package com.example.authapp3.entity;

public class EVChargingPrice {
    private String companyName;
    private String price;

    public EVChargingPrice() {
    }

    public EVChargingPrice(String companyName, String price) {
        this.companyName = companyName;
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPrice() {
        return price;
    }
}
