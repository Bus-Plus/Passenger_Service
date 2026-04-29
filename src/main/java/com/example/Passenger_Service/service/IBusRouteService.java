package com.example.Passenger_Service.service;

import java.util.List;

import com.example.Passenger_Service.dto.BusStopsDto;
import com.example.Passenger_Service.dto.RouteBusStatusDto;

public interface IBusRouteService {
    List<String> getBusNumbers();

    BusStopsDto getBusStops(String busNumber);

    List<RouteBusStatusDto> getRouteBusStatuses(String routeId, int selectedStopIndex);
}
