package com.shirobokov.inventoryreservationservice.repository.projection;

public interface ProductTopProjection {
    Long getId();
    String getName();
    Long getConfirmedCount();
}