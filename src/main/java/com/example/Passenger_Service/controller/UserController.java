package com.example.Passenger_Service.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Passenger_Service.config.JwtTokenProvider;
import com.example.Passenger_Service.dto.ValidateTokenResponse;
import com.example.Passenger_Service.model.User;
import com.example.Passenger_Service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public UserController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/by-userid")
    public ResponseEntity<User> getByUserId(Authentication authentication) {

        String userId = authentication.getName();
        System.out.println("🔍 Searching for user with ID: " + userId);
        return userService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateAccessToken(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7).trim();
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String tokenUserId = tokenProvider.getUserId(token);
        if (tokenUserId == null || tokenUserId.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Date expiration = tokenProvider.getExpirationDate(token);
        if (expiration == null || expiration.before(new Date())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = tokenProvider.getRole(token);
        if (role == null || role.isBlank()) {
            role = "UNKNOWN";
        }

        ValidateTokenResponse response = new ValidateTokenResponse(role, "token valid");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
