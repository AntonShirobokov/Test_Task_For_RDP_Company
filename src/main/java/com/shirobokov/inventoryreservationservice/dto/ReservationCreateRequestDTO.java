package com.shirobokov.inventoryreservationservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservationCreateRequestDTO(@NotNull(message = "productId обязателен")
                                          Long productId,

                                          @NotNull(message = "quantity обязателен")
                                          @Min(value = 1, message = "quantity должен быть больше 0")
                                          Integer quantity) {
}
