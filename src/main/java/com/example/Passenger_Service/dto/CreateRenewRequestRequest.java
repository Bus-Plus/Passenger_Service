package com.example.Passenger_Service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CreateRenewRequestRequest {

    @JsonAlias({"id"})
    private String passId;
    private String passTypeName;

    public CreateRenewRequestRequest() {
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
}
