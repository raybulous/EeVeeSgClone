package com.example.authapp3.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesResults implements Serializable {

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions = new ArrayList<>();

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("results")
    private List<PlacesResult> placesResults = new ArrayList<>();

    @SerializedName("status")
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
    public List<PlacesResult> getResults() {
        return placesResults;
    }
    public void setResults(List<PlacesResult> placesResults) {
        this.placesResults = placesResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}