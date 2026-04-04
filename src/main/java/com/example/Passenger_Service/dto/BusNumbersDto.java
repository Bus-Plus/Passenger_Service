package com.example.Passenger_Service.dto;

import java.util.List;

public class BusNumbersDto {

    private List<String> ids;

    public BusNumbersDto() {
    }

    public BusNumbersDto(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
