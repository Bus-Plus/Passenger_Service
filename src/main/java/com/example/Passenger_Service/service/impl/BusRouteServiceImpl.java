package com.example.Passenger_Service.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Passenger_Service.dto.BusStopsDto;
import com.example.Passenger_Service.dto.RouteBusStatusDto;
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
        System.out.println("DEBUG: getBusNumbers called");
        if (firestore == null) {
            throw new IllegalStateException("Firestore config not initialized");
        }

        try {
            var documents = firestore.collection("bus_stops").listDocuments();
            var busNumbers = new java.util.ArrayList<String>();
            documents.forEach(docRef -> busNumbers.add(docRef.getId()));
            return busNumbers;
        } catch (ApiException e) {
            System.out.println("DEBUG: getBusNumbers ApiException: " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Firestore fetch failed: " + e.getMessage(), e);
        }
    }

    @Override
    public BusStopsDto getBusStops(String busNumber) {
        System.out.println("DEBUG: getBusStops called for busNumber=" + busNumber);
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
            System.out.println("DEBUG: getBusStops ApiException for busNumber=" + busNumber + ": " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Firestore read failed for collection 'bus_stops', document '" + busNumber + "': " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("DEBUG: getBusStops InterruptedException for busNumber=" + busNumber + ": " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Firestore request interrupted for busNumber: " + busNumber, e);
        } catch (java.util.concurrent.ExecutionException e) {
            System.out.println("DEBUG: getBusStops ExecutionException for busNumber=" + busNumber + ": " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Firestore request failed for busNumber: " + busNumber, e);
        }
    }

    @Override
    public List<RouteBusStatusDto> getRouteBusStatuses(String routeId, int selectedStopIndex) {
        System.out.println("DEBUG: getRouteBusStatuses called for routeId=" + routeId + ", selectedStopIndex=" + selectedStopIndex);
        if (firestore == null) {
            throw new IllegalStateException("Firestore config not initialized");
        }

        if (selectedStopIndex < 0) {
            throw new IllegalArgumentException("selectedStopIndex must be >= 0");
        }

        System.out.println("DEBUG: fetching route stops for routeId=" + routeId + " from collection bus_stops");
        BusStopsDto stopsDto = getBusStops(routeId);
        List<String> stops = stopsDto.getStops();
        System.out.println("DEBUG: route stops loaded for routeId=" + routeId + ", stopCount=" + stops.size());

        System.out.println("DEBUG: fetching route document for routeId=" + routeId + " from collection bus_status");
        DocumentSnapshot routeSnapshot = findRoute(routeId);
        System.out.println("DEBUG: routeSnapshot exists=" + routeSnapshot.exists());

        List<String> activeBuses = getActiveBuses(routeId);
        System.out.println("DEBUG: active buses for routeId=" + routeId + " count=" + activeBuses.size() + ", ids=" + activeBuses);

        return activeBuses.stream()
                .map(busId -> createRouteBusStatus(routeId, busId, selectedStopIndex, stops))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> getActiveBuses(String routeId) {
        try {
            DocumentSnapshot routeSnapshot = findRoute(routeId);
            if (!routeSnapshot.exists()) {
                throw new IllegalArgumentException("Route not found: " + routeId);
            }

            var documents = routeSnapshot.getReference().collection("busses").listDocuments();
            var busIds = new java.util.ArrayList<String>();
            documents.forEach(docRef -> busIds.add(docRef.getId()));
            return busIds;
        } catch (ApiException e) {
            System.out.println("DEBUG: getActiveBuses ApiException for routeId=" + routeId + ": " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Firestore read failed for route_status route '" + routeId + "': " + e.getMessage(), e);
        }
    }

    private RouteBusStatusDto createRouteBusStatus(String routeId, String busId, int selectedStopIndex, List<String> stops) {
        System.out.println("DEBUG: fetching bus document for routeId=" + routeId + ", busId=" + busId);
        try {
            DocumentSnapshot snapshot = findBus(routeId, busId);

            if (!snapshot.exists()) {
                System.out.println("DEBUG: bus document not found for routeId=" + routeId + ", busId=" + busId);
                return null;
            }

            String storedRouteId = snapshot.getString("routeId");
            System.out.println("DEBUG: busId=" + busId + " storedRouteId=" + storedRouteId);
            if (storedRouteId != null && !storedRouteId.equals(routeId)) {
                System.out.println("DEBUG: busId=" + busId + " routeId mismatch (expected=" + routeId + ", actual=" + storedRouteId + ")");
                return null;
            }

            Long currentStopIndexValue = snapshot.getLong("currentStopIndex");
            int currentStopIndex = currentStopIndexValue != null ? currentStopIndexValue.intValue() : -1;
            System.out.println("DEBUG: busId=" + busId + " currentStopIndex=" + currentStopIndex);

            Object countsObj = snapshot.get("passengerCountsPerStop");
            List<Integer> passengerCounts;
            if (countsObj instanceof List<?> rawList) {
                passengerCounts = rawList.stream()
                        .filter(item -> item != null)
                        .map(BusRouteServiceImpl::parsePassengerCount)
                        .collect(Collectors.toList());
            } else {
                passengerCounts = java.util.Collections.emptyList();
            }
            System.out.println("DEBUG: busId=" + busId + " passengerCountsPerStop size=" + passengerCounts.size() + " values=" + passengerCounts);

            int currentPassengers = 0;
            if (currentStopIndex >= 0 && currentStopIndex < passengerCounts.size()) {
                Integer currentPassengersValue = passengerCounts.get(currentStopIndex);
                currentPassengers = currentPassengersValue != null ? currentPassengersValue : 0;
            }
            System.out.println("DEBUG: busId=" + busId + " currentPassengers=" + currentPassengers);

            Integer expectedCount = null;
            if (currentStopIndex >= 0 && currentStopIndex < passengerCounts.size()
                    && selectedStopIndex >= 0
                    && selectedStopIndex < passengerCounts.size()
                    && currentStopIndex < selectedStopIndex) {
                Integer expectedCountValue = passengerCounts.get(selectedStopIndex);
                expectedCount = expectedCountValue != null ? expectedCountValue : null;
            }
            System.out.println("DEBUG: busId=" + busId + " expectedCount=" + expectedCount + " selectedStopIndex=" + selectedStopIndex);

            String currentStop = (currentStopIndex >= 0 && currentStopIndex < stops.size())
                    ? stops.get(currentStopIndex)
                    : "Unknown";
            System.out.println("DEBUG: busId=" + busId + " currentStopName=" + currentStop);

            return new RouteBusStatusDto(busId, currentStop, currentPassengers, expectedCount, currentStopIndex);
        } catch (ApiException e) {
            System.out.println("DEBUG: createRouteBusStatus ApiException for busId=" + busId + ": " + e.getMessage());
            System.out.println("DEBUG: stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    private DocumentSnapshot findRoute(String routeId) {
        try {
            DocumentReference routeDoc = firestore.collection("bus_status").document(routeId);
            return routeDoc.get().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Firestore route lookup interrupted for routeId: " + routeId, e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Firestore route lookup failed for routeId: " + routeId + ": " + e.getMessage(), e);
        }
    }

    private DocumentSnapshot findBus(String routeId, String busNumber) {
        try {
            DocumentReference busDoc = firestore
                    .collection("bus_status")
                    .document(routeId)
                    .collection("busses")
                    .document(busNumber);

            return busDoc.get().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Firestore bus lookup interrupted for routeId=" + routeId + ", busNumber=" + busNumber, e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Firestore bus lookup failed for routeId=" + routeId + ", busNumber=" + busNumber + ": " + e.getMessage(), e);
        }
    }

    private static Integer parsePassengerCount(Object item) {
        if (item instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }
}
