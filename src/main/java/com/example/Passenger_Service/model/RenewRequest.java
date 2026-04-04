package com.example.Passenger_Service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "renew_request")
public class RenewRequest {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "passid", columnDefinition = "char(36)", nullable = false)
    private String passId;

    @Column(name = "pass_type_name", length = 100, nullable = false)
    private String passTypeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private RenewRequestStatus status;

    public RenewRequest() {
    }

    public RenewRequest(String id, String passId, String passTypeName, RenewRequestStatus status) {
        this.id = id;
        this.passId = passId;
        this.passTypeName = passTypeName;
        this.status = status;
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

    public String getPassTypeName() {
        return passTypeName;
    }

    public void setPassTypeName(String passTypeName) {
        this.passTypeName = passTypeName;
    }

    public RenewRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RenewRequestStatus status) {
        this.status = status;
    }
}
