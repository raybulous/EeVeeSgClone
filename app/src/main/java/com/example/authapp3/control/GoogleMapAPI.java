package com.example.authapp3.control;


import com.example.authapp3.entity.PlaceResults;
import com.example.authapp3.entity.PlacesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {

    @GET("place/nearbysearch/json")
    Call<PlacesResults> getNearBy(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            //@Query("keyword") String keyword,
            @Query("key") String key
    );

    @GET("place/findplacefromtext/json")
    Call<PlaceResults> findPlace(
            @Query("input") String input,
            @Query("inputtype") String inputtype,
            @Query("fields") String fields,
            @Query("key") String key
    );

}