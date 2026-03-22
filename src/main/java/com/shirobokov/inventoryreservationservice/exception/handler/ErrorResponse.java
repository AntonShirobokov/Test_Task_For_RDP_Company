package com.shirobokov.inventoryreservationservice.exception.handler;

import com.shirobokov.inventoryreservationservice.enumerate.ErrorCode;

public record ErrorResponse(
        ErrorCode code,
        String message
) {}
