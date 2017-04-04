package com.devicepilot.devicesexample;

/**
 * Example of a measurement record from a device.
 */
public class DeviceRecord {
    public String $id;
    public double latitude;
    public double longitude;
    public String label;
    public double temperature;

    public DeviceRecord(String id, double latitude, double longitude, String label, double temperature) {
        this.$id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.temperature = temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
