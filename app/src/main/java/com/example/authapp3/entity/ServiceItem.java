package com.example.authapp3.entity;

public class ServiceItem {

    String serviceName;
    String serviceArea;
    Float servicePrice;

    public ServiceItem ( String serviceName, String serviceArea){
        this.serviceName = serviceName;
        this.serviceArea = serviceArea;
    }


    public String getServiceName() {
        return serviceName;
    }

    public String getServiceArea() {
        return serviceArea;
    }

}
