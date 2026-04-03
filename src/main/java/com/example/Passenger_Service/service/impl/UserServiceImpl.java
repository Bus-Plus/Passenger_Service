package com.example.Passenger_Service.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Passenger_Service.model.User;
import com.example.Passenger_Service.repository.UserRepository;
import com.example.Passenger_Service.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
