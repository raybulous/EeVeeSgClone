package com.example.authapp3.entity;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceResults implements  Serializable{

    @SerializedName("candidates")
    private List<PlaceResult> candidates = new ArrayList<>();

    @SerializedName("status")
    private String status;

    public List<PlaceResult> getResults() {
        return candidates;
    }

    public void setResults(List<PlaceResult> results) {
        this.candidates = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}