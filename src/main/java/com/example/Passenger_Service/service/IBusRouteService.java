package com.example.Passenger_Service.service;

import java.util.List;

import com.example.Passenger_Service.dto.BusStopsDto;

public interface IBusRouteService {
    List<String> getBusNumbers();

    BusStopsDto getBusStops(String busNumber);
}
