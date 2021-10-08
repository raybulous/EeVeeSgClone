package com.example.authapp3.entity;

import com.example.authapp3.entity.Distance;
import com.example.authapp3.entity.Duration;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}