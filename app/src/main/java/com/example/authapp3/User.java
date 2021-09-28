package com.example.authapp3;

public class User {
    public String Email, Age, Name;

    public User(){

    }

    public User(String fullName, String age, String email){
        this.Name = fullName;
        this.Email = email;
        this.Age = age;
    }
}

