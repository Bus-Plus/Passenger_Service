package com.example.Passenger_Service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "passestype")
public class PassType {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "passtype", length = 30, nullable = false)
    private String passType;

    public PassType() {
    }

    public PassType(String id, String passType) {
        this.id = id;
        this.passType = passType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }
}
