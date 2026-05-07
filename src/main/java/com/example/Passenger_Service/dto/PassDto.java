package com.example.Passenger_Service.dto;

import java.time.LocalDateTime;

public class PassDto {

    private String passId;
    private String passType;
    private String fromLocation;
    private String toLocation;
    private LocalDateTime createdAt;
    private LocalDateTime expiry;

    public PassDto() {
    }

    public PassDto(String passId, String passType, String fromLocation, String toLocation,
            LocalDateTime createdAt, LocalDateTime expiry) {
        this.passId = passId;
        this.passType = passType;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.createdAt = createdAt;
        this.expiry = expiry;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
