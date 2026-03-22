package com.shirobokov.inventoryreservationservice.dto;

import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;

public record ReservationConfirmDTO(Long reservationId, ReservationStatus reservationStatus) {
}
