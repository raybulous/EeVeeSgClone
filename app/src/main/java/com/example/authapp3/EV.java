package com.example.authapp3;

import android.media.Image;

public class EV {
    private String ChargeStatus, Colour, Model;
    private int BatteryStatus;

    public EV() {

    }

    public EV(String chargeStatus, String colour, String model, int batteryStatus) {
        ChargeStatus = chargeStatus;
        Colour = colour;
        Model = model;
        BatteryStatus = batteryStatus;
    }

    public String getChargeStatus() {
        return ChargeStatus;
    }

    public String getColour() {
        return Colour;
    }

    public String getModel() {
        return Model;
    }

    public int getBatteryStatus() {
        return BatteryStatus;
    }

    public int getBatteryImage() {
        if(this.BatteryStatus > 75) {
            return R.drawable.battery_100;
        } else if (this.BatteryStatus > 50) {
            return R.drawable.battery_75;
        } else if (this.BatteryStatus > 25) {
            return R.drawable.battery_50;
        } else {
            return R.drawable.battery_25;
        }
    }
}
