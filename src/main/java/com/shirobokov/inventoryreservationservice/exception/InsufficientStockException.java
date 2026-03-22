package com.shirobokov.inventoryreservationservice.exception;

public class InsufficientStockException extends RuntimeException {
  public InsufficientStockException(Long productId) {
    super("Недостаточно товара на складе для продукта с id " + productId);
  }
}
