package com.example.authapp3.control;

import android.view.View;

import com.example.authapp3.entity.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route, int option, View view);
}