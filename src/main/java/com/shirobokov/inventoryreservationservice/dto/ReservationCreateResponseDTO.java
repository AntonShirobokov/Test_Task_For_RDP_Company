package com.shirobokov.inventoryreservationservice.dto;

import java.time.Instant;

public record ReservationCreateResponseDTO (Long reservationId, Instant createdAt, Instant expiresAt) {
}
