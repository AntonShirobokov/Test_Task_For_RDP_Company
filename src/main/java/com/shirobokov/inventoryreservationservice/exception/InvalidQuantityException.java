package com.shirobokov.inventoryreservationservice.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("Количество должно быть больше 0");
    }
}
