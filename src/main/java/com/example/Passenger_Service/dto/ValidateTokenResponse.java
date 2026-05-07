package com.example.Passenger_Service.dto;

public class ValidateTokenResponse {
    private String role;
    private String status;

    public ValidateTokenResponse() {
    }

    public ValidateTokenResponse(String role, String status) {
        this.role = role;
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
