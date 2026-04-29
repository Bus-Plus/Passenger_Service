package com.example.Passenger_Service.dto;

public class RouteBusStatusDto {

    private String busNumber;
    private String currentStop;
    private int currentPassengers;
    private Integer expectedCount;
    private int currentStopIndex;

    public RouteBusStatusDto() {
    }

    public RouteBusStatusDto(String busNumber, String currentStop, int currentPassengers, Integer expectedCount, int currentStopIndex) {
        this.busNumber = busNumber;
        this.currentStop = currentStop;
        this.currentPassengers = currentPassengers;
        this.expectedCount = expectedCount;
        this.currentStopIndex = currentStopIndex;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(String currentStop) {
        this.currentStop = currentStop;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public void setCurrentPassengers(int currentPassengers) {
        this.currentPassengers = currentPassengers;
    }

    public Integer getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(Integer expectedCount) {
        this.expectedCount = expectedCount;
    }

    public int getCurrentStopIndex() {
        return currentStopIndex;
    }

    public void setCurrentStopIndex(int currentStopIndex) {
        this.currentStopIndex = currentStopIndex;
    }
}
