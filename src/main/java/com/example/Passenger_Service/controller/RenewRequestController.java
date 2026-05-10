package com.example.Passenger_Service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.Passenger_Service.dto.CreateRenewRequestRequest;
import com.example.Passenger_Service.model.RenewRequest;
import com.example.Passenger_Service.service.RenewRequestService;

@RestController
@RequestMapping("/renew-requests")
public class RenewRequestController {

    private static final Logger logger = LoggerFactory.getLogger(RenewRequestController.class);
    private final RenewRequestService renewRequestService;

    public RenewRequestController(RenewRequestService renewRequestService) {
        this.renewRequestService = renewRequestService;
    }

    @PostMapping
    public ResponseEntity<RenewRequest> createRenewRequest(@RequestBody CreateRenewRequestRequest request) {
        logger.debug("POST /renew-requests received with passId='{}', passTypeName='{}'", request.getPassId(), request.getPassTypeName());

        try {
            RenewRequest savedRequest = renewRequestService.createRenewRequest(
                    request.getPassId(),
                    request.getPassTypeName());

            logger.debug("POST /renew-requests created RenewRequest id='{}' for passId='{}'", savedRequest.getId(), request.getPassId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                logger.debug("Caught 409 CONFLICT in controller for passId='{}'", request.getPassId());
            }
            throw ex;
        }
    }
}
