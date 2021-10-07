package com.example.authapp3;

public class Duration {
    private String text;
    private int value;

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}