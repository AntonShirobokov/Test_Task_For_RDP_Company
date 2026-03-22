package com.shirobokov.inventoryreservationservice.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
    super(message);
  }
}
