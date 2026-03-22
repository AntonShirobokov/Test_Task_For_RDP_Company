package com.shirobokov.inventoryreservationservice.service;

import com.shirobokov.inventoryreservationservice.dto.ReservationCreateResponseDTO;
import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;
import com.shirobokov.inventoryreservationservice.exception.InsufficientStockException;
import com.shirobokov.inventoryreservationservice.exception.ProductNotFoundException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.model.Reservation;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import com.shirobokov.inventoryreservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationOperationService {

    private static final Duration RESERVATION_TTL = Duration.ofMinutes(10);

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReservationCreateResponseDTO doCreateReservation(Long productId, int quantity) {
        Product product = productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Instant now = Instant.now();

        expireStaleReservations(product, now);

        int reservedStock = calculateReservedStock(product, now);
        int availableStock = product.getStock() - reservedStock;

        log.info("productId={}: stock={}, reservedStock={}, availableStock={}",
                productId, product.getStock(), reservedStock, availableStock);

        if (availableStock < quantity) {
            throw new InsufficientStockException(productId);
        }

        Reservation reservation = buildReservation(product, quantity, now);
        reservation = reservationRepository.save(reservation);

        return new ReservationCreateResponseDTO(
                reservation.getId(),
                reservation.getCreatedAt(),
                reservation.getExpiresAt()
        );
    }

    private void expireStaleReservations(Product product, Instant now) {
        List<Reservation> expired = product.getReservations().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE
                        && r.getExpiresAt().isBefore(now))
                .toList();

        if (!expired.isEmpty()) {
            expired.forEach(r -> r.setStatus(ReservationStatus.EXPIRED));
            reservationRepository.saveAll(expired);
            log.debug("Переведено в EXPIRED: {} резервов для productId={}",
                    expired.size(), product.getId());
        }
    }

    private int calculateReservedStock(Product product, Instant now) {
        return product.getReservations().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE
                        && r.getExpiresAt().isAfter(now))
                .mapToInt(Reservation::getQuantity)
                .sum();
    }

    private Reservation buildReservation(Product product, int quantity, Instant now) {
        Reservation reservation = new Reservation();
        reservation.setProduct(product);
        reservation.setQuantity(quantity);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCreatedAt(now);
        reservation.setExpiresAt(now.plus(RESERVATION_TTL));
        return reservation;
    }
}