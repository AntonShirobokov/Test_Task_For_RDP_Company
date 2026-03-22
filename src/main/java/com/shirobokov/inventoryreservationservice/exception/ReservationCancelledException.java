package com.shirobokov.inventoryreservationservice.exception;

public class ReservationCancelledException extends RuntimeException {
  public ReservationCancelledException(Long id) {
    super("Резерв " + id + " отменён");
  }
}
