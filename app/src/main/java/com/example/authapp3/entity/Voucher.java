package com.example.authapp3.entity;

public class Voucher {
    private String details, name;
    private int cost;

    public Voucher() {
    }

    public Voucher(int cost, String details, String name) {
        this.details = details;
        this.name = name;
        this.cost = cost;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}