package com.example.authapp3.entity;

import com.example.authapp3.R;

public class EV {
    private String chargeStatus, colour, model;
    private int batteryStatus;

    public EV() {

    }

    public EV(String chargeStatus, String colour, String model, int batteryStatus) {
        this.chargeStatus = chargeStatus;
        this.colour = colour;
        this.model = model;
        this.batteryStatus = batteryStatus;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public String getColour() {
        return colour;
    }

    public String getModel() {
        return model;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public int findBatteryImage() {
        if(this.batteryStatus > 75) {
            return R.drawable.battery_100;
        } else if (this.batteryStatus > 50) {
            return R.drawable.battery_75;
        } else if (this.batteryStatus > 25) {
            return R.drawable.battery_50;
        } else {
            return R.drawable.battery_25;
        }
    }
}
