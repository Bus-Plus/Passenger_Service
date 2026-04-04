package com.example.Passenger_Service.service;

import com.example.Passenger_Service.model.RenewRequest;

public interface RenewRequestService {

    RenewRequest createRenewRequest(String passId, String passTypeName);
}
