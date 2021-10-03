package com.example.authapp3;

public class EV {
    public String ChargeStatus, Colour, Model;
    public int BatteryStatus;

    public EV() {

    }

    public EV(String chargeStatus, String colour, String model, int batteryStatus) {
        ChargeStatus = chargeStatus;
        Colour = colour;
        Model = model;
        BatteryStatus = batteryStatus;
    }
}
