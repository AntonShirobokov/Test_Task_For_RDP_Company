package com.shirobokov.inventoryreservationservice.exception;

public class ReservationAlreadyConfirmedException extends RuntimeException {
    public ReservationAlreadyConfirmedException(Long id) {
        super("Резерв " + id + " уже подтверждён");
    }
}
