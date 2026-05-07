package com.example.Passenger_Service.dto;

public class PassResponseDto {

    private Long userId;
    private PassDto pass;

    public PassResponseDto() {
    }

    public PassResponseDto(Long userId, PassDto pass) {
        this.userId = userId;
        this.pass = pass;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PassDto getPass() {
        return pass;
    }

    public void setPass(PassDto pass) {
        this.pass = pass;
    }
}
