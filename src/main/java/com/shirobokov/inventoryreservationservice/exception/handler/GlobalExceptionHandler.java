package com.shirobokov.inventoryreservationservice.exception.handler;

import com.shirobokov.inventoryreservationservice.enumerate.ErrorCode;
import com.shirobokov.inventoryreservationservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.PRODUCT_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.RESERVATION_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantity(InvalidQuantityException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_QUANTITY, e.getMessage()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.INSUFFICIENT_STOCK, e.getMessage()));
    }

    @ExceptionHandler(ReservationAlreadyConfirmedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyConfirmed(ReservationAlreadyConfirmedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.RESERVATION_ALREADY_CONFIRMED, e.getMessage()));
    }

    @ExceptionHandler(ReservationCancelledException.class)
    public ResponseEntity<ErrorResponse> handleCancelled(ReservationCancelledException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.RESERVATION_CANCELLED, e.getMessage()));
    }

    @ExceptionHandler(ReservationExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpired(ReservationExpiredException e) {
        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(new ErrorResponse(ErrorCode.RESERVATION_EXPIRED, e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse(ErrorCode.TOO_MANY_REQUESTS, e.getMessage()));
    }
}