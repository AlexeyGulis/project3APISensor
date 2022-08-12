package org.example;

public class Measurement {

    private Double value;
    private Boolean raining;

    private Sensor sensor;

    public Measurement() {
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "value = " + value +
                ", raining = " + raining +
                ", sensor = " + sensor;
    }
}
