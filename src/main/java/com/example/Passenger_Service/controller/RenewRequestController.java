package com.example.Passenger_Service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Passenger_Service.dto.CreateRenewRequestRequest;
import com.example.Passenger_Service.model.RenewRequest;
import com.example.Passenger_Service.service.RenewRequestService;

@RestController
@RequestMapping("/renew-requests")
public class RenewRequestController {

    private final RenewRequestService renewRequestService;

    public RenewRequestController(RenewRequestService renewRequestService) {
        this.renewRequestService = renewRequestService;
    }

    @PostMapping
    public ResponseEntity<RenewRequest> createRenewRequest(@RequestBody CreateRenewRequestRequest request) {
        RenewRequest savedRequest = renewRequestService.createRenewRequest(
                request.getPassId(),
                request.getPassTypeName());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }
}
