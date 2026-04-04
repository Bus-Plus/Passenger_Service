package com.example.Passenger_Service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Passenger_Service.model.PassType;

@Repository
public interface PassTypeRepository extends JpaRepository<PassType, String> {
    Optional<PassType> findByPassType(String passType);

    boolean existsByPassType(String passType);
}
