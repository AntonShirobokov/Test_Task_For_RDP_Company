package com.shirobokov.inventoryreservationservice;

import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;
import com.shirobokov.inventoryreservationservice.exception.InsufficientStockException;
import com.shirobokov.inventoryreservationservice.exception.ReservationAlreadyConfirmedException;
import com.shirobokov.inventoryreservationservice.exception.ReservationExpiredException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.model.Reservation;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import com.shirobokov.inventoryreservationservice.repository.ReservationRepository;
import com.shirobokov.inventoryreservationservice.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ReservationServiceTest extends IntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Long productId;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setName("Apple");
        product.setStock(1000);
        productId = productRepository.save(product).getId();
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void successfulReservationCreation() {

        var result = reservationService.createReservation(productId, 3);


        assertThat(result).isNotNull();
        assertThat(result.reservationId()).isNotNull();
        assertThat(result.expiresAt()).isAfter(result.createdAt());
    }

    @Test
    void shouldThrowWhenInsufficientStock() {

        assertThatThrownBy(() -> reservationService.createReservation(productId, 1001))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void shouldThrowWhenConfirmingExpiredReservation() {

        Reservation reservation = new Reservation();
        reservation.setProduct(productRepository.findById(productId).get());
        reservation.setQuantity(1);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCreatedAt(Instant.now().minus(20, ChronoUnit.MINUTES));
        reservation.setExpiresAt(Instant.now().minus(10, ChronoUnit.MINUTES)); // истёк 10 минут назад
        Long reservationId = reservationRepository.save(reservation).getId();


        assertThatThrownBy(() -> reservationService.confirmReservation(reservationId))
                .isInstanceOf(ReservationExpiredException.class);
    }

    @Test
    void shouldThrowWhenConfirmingAlreadyConfirmedReservation() {

        var created = reservationService.createReservation(productId, 1);
        reservationService.confirmReservation(created.reservationId());


        assertThatThrownBy(() -> reservationService.confirmReservation(created.reservationId()))
                .isInstanceOf(ReservationAlreadyConfirmedException.class);
    }

    @Test
    void shouldDecreaseStockWhenConfirmed() {
        int initialStock = 1000;
        int quantity = 3;
        var created = reservationService.createReservation(productId, quantity);

        reservationService.confirmReservation(created.reservationId());

        Product product = productRepository.findById(productId).get();
        assertThat(product.getStock()).isEqualTo(initialStock - quantity);
    }
}