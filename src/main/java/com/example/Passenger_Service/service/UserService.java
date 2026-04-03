package com.example.Passenger_Service.service;

import java.util.Optional;

import com.example.Passenger_Service.model.User;

public interface UserService {
    Optional<User> findByUserId(String userId);
    Optional<User> findById(Long id);
}
