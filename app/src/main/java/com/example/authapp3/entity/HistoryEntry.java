package com.example.authapp3.entity;

import com.google.api.client.util.DateTime;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.Map;

public class HistoryEntry {
    private String name;
    private int points;
    private Map<String, String> dateTime;

    public HistoryEntry() {
    }

    public HistoryEntry(String name, int points) {
        this.name = name;
        this.points = points;
        this.dateTime = ServerValue.TIMESTAMP;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Map<String, String> getDateTime() {
        return dateTime;
    }
}
