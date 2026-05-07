package com.example.Passenger_Service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Passenger_Service.model.PassEst;

@Repository
public interface PassEstRepository extends JpaRepository<PassEst, String> {
    Optional<PassEst> findByPassId(String passId);
    List<PassEst> findAllByUserId(Long userId);
    boolean existsByPassId(String passId);
}
