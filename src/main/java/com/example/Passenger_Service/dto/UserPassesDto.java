package com.example.Passenger_Service.dto;

import java.util.List;

public class UserPassesDto {

    private Long userId;
    private List<PassDto> passes;

    public UserPassesDto() {
    }

    public UserPassesDto(Long userId, List<PassDto> passes) {
        this.userId = userId;
        this.passes = passes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<PassDto> getPasses() {
        return passes;
    }

    public void setPasses(List<PassDto> passes) {
        this.passes = passes;
    }
}
