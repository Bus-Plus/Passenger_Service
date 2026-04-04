package com.example.Passenger_Service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Passenger_Service.dto.BusStopsDto;
import com.example.Passenger_Service.service.IBusRouteService;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

@Service
public class BusRouteServiceImpl implements IBusRouteService {

    private final Firestore firestore;

    public BusRouteServiceImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public List<String> getBusNumbers() {
        if (firestore == null) {
            throw new IllegalStateException("Firestore config not initialized");
        }

        try {
            var documents = firestore.collection("bus_stops").listDocuments();
            var busNumbers = new java.util.ArrayList<String>();
            documents.forEach(docRef -> busNumbers.add(docRef.getId()));
            return busNumbers;
        } catch (ApiException e) {
            throw new RuntimeException("Firestore fetch failed: " + e.getMessage(), e);
        }
    }

    @Override
    public BusStopsDto getBusStops(String busNumber) {
        if (firestore == null) {
            throw new IllegalStateException("Firestore config not initialized");
        }

        try {
            DocumentReference docRef = firestore.collection("bus_stops").document(busNumber);
            DocumentSnapshot snapshot = docRef.get().get();

            if (!snapshot.exists()) {
                throw new IllegalArgumentException("Bus number not found: " + busNumber);
            }

            Object stopsObj = snapshot.get("stops");
            List<String> stops;
            if (stopsObj instanceof List<?> rawList) {
                stops = rawList.stream()
                        .filter(item -> item != null)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            } else {
                stops = java.util.Collections.emptyList();
            }

            return new BusStopsDto(busNumber, stops);
        } catch (ApiException e) {
            throw new RuntimeException("Firestore read failed for collection 'bus_stops', document '" + busNumber + "': " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Firestore request interrupted for busNumber: " + busNumber, e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Firestore request failed for busNumber: " + busNumber, e);
        }
    }
}
