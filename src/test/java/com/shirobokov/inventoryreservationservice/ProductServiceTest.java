package com.shirobokov.inventoryreservationservice;

import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.model.Reservation;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import com.shirobokov.inventoryreservationservice.repository.ReservationRepository;
import com.shirobokov.inventoryreservationservice.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnTopReservedProducts() {

        Long product1Id = createProductWithConfirmedReservations("Banana", 10, 5);
        Long product2Id = createProductWithConfirmedReservations("Kiwi", 10, 3);
        Long product3Id = createProductWithConfirmedReservations("Lemon", 10, 1);


        var result = productService.getTopReservedProducts();


        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("Banana");
        assertThat(result.get(0).confirmedCount()).isEqualTo(5L);
        assertThat(result.get(1).name()).isEqualTo("Kiwi");
        assertThat(result.get(1).confirmedCount()).isEqualTo(3L);
        assertThat(result.get(2).name()).isEqualTo("Lemon");
        assertThat(result.get(2).confirmedCount()).isEqualTo(1L);
    }

    private Long createProductWithConfirmedReservations(String name, int stock, int count) {
        Product product = new Product();
        product.setName(name);
        product.setStock(stock);
        Long productId = productRepository.save(product).getId();

        for (int i = 0; i < count; i++) {
            Reservation reservation = new Reservation();
            reservation.setProduct(product);
            reservation.setQuantity(1);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setCreatedAt(Instant.now());
            reservation.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
            reservationRepository.save(reservation);
        }

        return productId;
    }
}
