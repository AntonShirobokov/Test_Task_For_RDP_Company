package com.shirobokov.inventoryreservationservice.exception;

public class ReservationExpiredException extends RuntimeException {
    public ReservationExpiredException(Long id) {
        super("Резерв " + id + " истёк");
    }
}
