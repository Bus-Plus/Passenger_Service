package com.example.Passenger_Service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Passenger_Service.dto.BusNumbersDto;
import com.example.Passenger_Service.dto.BusStopsDto;
import com.example.Passenger_Service.service.IBusRouteService;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;

@RestController
@RequestMapping("/connectivity")
public class BusRouteController {

    private final Firestore firestore;
    private final IBusRouteService busRouteService;

    public BusRouteController(Firestore firestore, IBusRouteService busRouteService) {
        this.firestore = firestore;
        this.busRouteService = busRouteService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/firebase")
    public ResponseEntity<String> checkFirebase() {
        if (firestore == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Firestore config not initialized");
        }

        try {
            CollectionReference passes = firestore.collection("passes");
            passes.limit(1).get().get();
            return ResponseEntity.ok("Firestore connection OK");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Firestore connection interrupted: " + e.getMessage());
        } catch (java.util.concurrent.ExecutionException | ApiException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Firestore connection failed: " + e.getMessage());
        }
    }

    @GetMapping("/bus-numbers")
    public ResponseEntity<?> getBusNumbers() {
        try {
            var busNumbers = busRouteService.getBusNumbers();
            var dto = new BusNumbersDto(busNumbers);
            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/bus-numbers/{busNumber}/stops")
    public ResponseEntity<?> getBusStopsByBusNumber(@PathVariable String busNumber) {
        try {
            BusStopsDto dto = busRouteService.getBusStops(busNumber);
            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
