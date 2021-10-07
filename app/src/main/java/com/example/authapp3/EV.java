package com.example.authapp3;

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
}
