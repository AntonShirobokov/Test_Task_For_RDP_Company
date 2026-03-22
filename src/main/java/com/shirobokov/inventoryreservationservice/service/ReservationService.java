package com.shirobokov.inventoryreservationservice.service;

import com.shirobokov.inventoryreservationservice.dto.ReservationCreateResponseDTO;
import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;
import com.shirobokov.inventoryreservationservice.exception.ConflictException;
import com.shirobokov.inventoryreservationservice.exception.InsufficientStockException;
import com.shirobokov.inventoryreservationservice.exception.InvalidQuantityException;
import com.shirobokov.inventoryreservationservice.exception.ProductNotFoundException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.model.Reservation;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import com.shirobokov.inventoryreservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final ReservationOperationService operationService;

    public ReservationCreateResponseDTO createReservation(Long productId, int quantity) {
        int attempt = 0;
        while (true) {
            try {
                return operationService.doCreateReservation(productId, quantity);
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                if (attempt >= MAX_RETRY_ATTEMPTS) {
                    log.warn("Не удалось создать резерв для productId={} после {} попыток",
                            productId, MAX_RETRY_ATTEMPTS);
                    throw new ConflictException("Слишком много параллельных запросов, попробуйте позже");
                }
                log.debug("Optimistic lock конфликт для productId={}, попытка {}", productId, attempt);
            }
        }
    }
}
