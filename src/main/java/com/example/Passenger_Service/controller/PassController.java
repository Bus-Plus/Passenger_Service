package com.example.Passenger_Service.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Passenger_Service.dto.UserPassesDto;
import com.example.Passenger_Service.service.PassService;

@RestController
@RequestMapping("/passes")
public class PassController {

    private static final Logger logger = LoggerFactory.getLogger(PassController.class);
    private final PassService passService;

    public PassController(PassService passService) {
        this.passService = passService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserPassesDto> getPassesForAuthenticatedUser(Authentication authentication) {
        String userId = authentication.getName();
        logger.debug("GET /passes/user called", userId);
        System.out.println("🔍 Searching for passes for user ID: " + userId);
        Optional<UserPassesDto> passes = passService.getPassesByUserId(userId);
        return passes.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPassesDto> getPassesForUser(@PathVariable String userId) {
        System.err.println("🔍 Searching for passes for user ID: " + userId);
        Optional<UserPassesDto> passes = passService.getPassesByUserId(userId);
        return passes.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
