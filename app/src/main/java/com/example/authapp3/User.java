package com.example.authapp3;

public class User {
    private String Name, Address, Contact;

    public User() {
    }

    public User(String name){
        this.Name = name;
    }

    public User(String name, String address, String contact) {
        Name = name;
        Address = address;
        Contact = contact;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getContact() {
        return Contact;
    }
}

