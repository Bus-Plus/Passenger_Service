package com.example.Passenger_Service.service;

import java.util.Optional;

import com.example.Passenger_Service.dto.PassResponseDto;
import com.example.Passenger_Service.dto.UserPassesDto;

public interface PassService {
    Optional<PassResponseDto> getPassDetailsByPassId(String passId);
    Optional<PassResponseDto> getPassDetailsByPassIdForUser(String passId, String userId);
    Optional<UserPassesDto> getPassesByUserId(String userId);
}
