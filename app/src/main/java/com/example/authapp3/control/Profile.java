package com.example.authapp3.control;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Profile {
    static Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
    static Pattern lowerCasePatten = Pattern.compile("[a-z ]");
    static Pattern digitCasePatten = Pattern.compile("[0-9 ]");

    public static String checkPassword(String password) {
        if (password.isEmpty()) {
            return "Password is required!";
        }
        if (password.length() < 8){
            return "Password must have minimum 8 characters!";
        }
        if (!UpperCasePatten.matcher(password).find()) {
            return "Password must have minimum 1 uppercase character!";
        }
        if (!lowerCasePatten.matcher(password).find()) {
            return "Password must have minimum 1 lowercase character!";
        }
        if (!digitCasePatten.matcher(password).find()) {
            return "Password must have minimum 1 digit character!";
        }
        return "No Error";
    }

    public static String checkEmail(String email) {
        if (email.isEmpty()) {
            return "Email is required!";
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Please provide valid email!";
        }
        return "No Error";
    }

    public static String checkContact(String contact, String initialValue) {
        if(contact.equals(initialValue)) {
            return "Cannot be same as previous";
        }
        if(contact.isEmpty()) {
            return "Number is required";
        }
        try {
            Integer.parseInt(contact);
        } catch (NumberFormatException e) {
            return "Must be contact number";
        }
        if(contact.length() != 8) {
            return "Must be length 8";
        }
        if(contact.charAt(0) != '6' & contact.charAt(0) != '8' & contact.charAt(0) != '9') {
            return "Must start with 6, 8 or 9";
        }
        return "No Error";
    }

    public static String checkName(String name, String initialValue) {
        if(name.isEmpty()){
            return "Full Name is required!";
        }
        if(name.equals(initialValue)) {
            return "Cannot be same as previous";
        }
        return "No Error";
    }

    public static String checkAddress(String address, String initialValue) {
        if(address.isEmpty()){
            return "Address is required!";
        }
        if(address.equals(initialValue)) {
            return "Cannot be same as previous";
        }
        return "No Error";
    }
}
