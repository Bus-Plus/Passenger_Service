package com.example.Passenger_Service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Passenger_Service.model.RenewRequest;

@Repository
public interface RenewRequestRepository extends JpaRepository<RenewRequest, String> {
    boolean existsByPassId(String passId);
    Optional<RenewRequest> findByPassId(String passId);
}
