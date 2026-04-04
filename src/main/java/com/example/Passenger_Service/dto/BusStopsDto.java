package com.example.Passenger_Service.dto;

import java.util.List;

public class BusStopsDto {

    private String busNumber;
    private List<String> stops;

    public BusStopsDto() {
    }

    public BusStopsDto(String busNumber, List<String> stops) {
        this.busNumber = busNumber;
        this.stops = stops;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }
}
