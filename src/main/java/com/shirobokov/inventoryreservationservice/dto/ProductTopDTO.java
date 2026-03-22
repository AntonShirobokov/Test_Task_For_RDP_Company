package com.shirobokov.inventoryreservationservice.dto;

public record ProductTopDTO(
        Long id,
        String name,
        Long confirmedCount
) {}
