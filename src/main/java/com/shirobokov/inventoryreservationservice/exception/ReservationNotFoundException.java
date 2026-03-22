package com.shirobokov.inventoryreservationservice.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Резерв с id: " + id + " не найден");
    }
}
