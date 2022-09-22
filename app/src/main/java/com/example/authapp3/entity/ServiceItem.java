package com.example.authapp3.entity;

import java.util.Date;

public class ServiceItem {

    String serviceName;
    String serviceArea;
    Float servicePrice;
    Date serviceDate;
    Integer serviceRating;
    String serviceProvider;

    public ServiceItem ( String serviceName, String serviceArea, String serviceProvider){
        this.serviceName = serviceName;
        this.serviceArea = serviceArea;
        this.serviceDate = null;   // upon completed services
        this.serviceRating = null; // upon completed services
        this.serviceProvider = serviceProvider; // name of serviceProvider
    }


    public String getServiceName() {
        return serviceName;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }
}
