package com.example.authapp3.nearby;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {

    @GET("place/nearbysearch/json")
    Call<PlacesResults> getNearBy(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

}