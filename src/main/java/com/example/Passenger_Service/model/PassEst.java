package com.example.Passenger_Service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "passes")
public class PassEst {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "passid", columnDefinition = "char(36)", nullable = false)
    private String passId;

    @Column(name = "userid", nullable = false)
    private Long userId;

    // Renamed columns in DB: from -> from_stop, to -> to_stop
    @Column(name = "from_stop", length = 50)
    private String fromLocation;

    @Column(name = "to_stop", length = 50)
    private String toLocation;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "expiry")
    private java.time.LocalDateTime expiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "passid",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    private PassType passType;

    public PassEst() {
    }

    public PassEst(String id, String passId, Long userId, String fromLocation, String toLocation,PassType passType, java.time.LocalDateTime createdAt, java.time.LocalDateTime expiry) {
        this.id = id;
        this.passId = passId;
        this.userId = userId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.createdAt = createdAt;
        this.expiry = expiry;
        this.passType = passType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // public Integer getPassType() {
    //     return passType;
    // }
    // public void setPassType(Integer passType) {
    //     this.passType = passType;
    // }
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

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(java.time.LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public PassType getPassType() {
        return passType;
    }

    public void setPassType(PassType passType) {
        this.passType = passType;
    }
}
