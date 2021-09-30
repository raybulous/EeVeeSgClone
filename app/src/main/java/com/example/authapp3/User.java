package com.example.authapp3;

public class User {
    public String Name, Address, Contact;

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
}

