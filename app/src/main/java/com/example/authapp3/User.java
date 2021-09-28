package com.example.authapp3;

public class User {
    public String email, age, fullName;

    public User(){

    }

    public User(String fullName, String age, String email){
        this.fullName = fullName;
        this.email = email;
        this.age = age;
    }
}

