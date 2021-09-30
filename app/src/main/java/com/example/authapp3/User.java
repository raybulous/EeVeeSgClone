package com.example.authapp3;

public class User {
    public String Email, Name;

    public User(){

    }

    public User(String fullName, String email){
        this.Name = fullName;
        this.Email = email;
    }
}

