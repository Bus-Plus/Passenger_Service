package com.example.Passenger_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Passenger_Service.model.RenewRequest;

@Repository
public interface RenewRequestRepository extends JpaRepository<RenewRequest, String> {
}
