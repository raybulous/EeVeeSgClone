package com.example.authapp3.entity;

import com.example.authapp3.R;

public class EV {
    private String chargeStatus, colour, model;
    private int batteryStatus;
    private boolean manualInput;

    public EV() {

    }

    public EV(String chargeStatus, String colour, String model, int batteryStatus, boolean manualInput) {
        this.chargeStatus = chargeStatus;
        this.colour = colour;
        this.model = model;
        this.batteryStatus = batteryStatus;
        this.manualInput = true;
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

    public boolean isManualInput() {
        return manualInput;
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
