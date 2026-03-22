package com.shirobokov.inventoryreservationservice.service;

import com.shirobokov.inventoryreservationservice.dto.ReservationConfirmDTO;
import com.shirobokov.inventoryreservationservice.dto.ReservationCreateResponseDTO;
import com.shirobokov.inventoryreservationservice.exception.ConflictException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

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

    public ReservationConfirmDTO confirmReservation(Long reservationId) {
        int attempt = 0;
        while (true) {
            try {
                return operationService.doConfirmReservation(reservationId);
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                if (attempt >= MAX_RETRY_ATTEMPTS) {
                    log.warn("Optimistic lock конфликт при подтверждении reservationId={}", reservationId);
                    throw new ConflictException("Попробуйте подтвердить резерв ещё раз");
                }
            }
        }
    }
}
