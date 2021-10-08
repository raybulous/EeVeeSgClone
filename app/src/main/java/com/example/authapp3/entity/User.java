package com.example.authapp3.entity;

public class User {
    private String name, address, contact;

    public User() {
    }

    public User(String name){
        this.name = name;
    }

    public User(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }
}

