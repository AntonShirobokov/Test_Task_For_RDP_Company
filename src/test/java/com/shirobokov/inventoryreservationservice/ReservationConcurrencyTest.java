package com.shirobokov.inventoryreservationservice;

import com.shirobokov.inventoryreservationservice.enumerate.ReservationStatus;
import com.shirobokov.inventoryreservationservice.exception.ConflictException;
import com.shirobokov.inventoryreservationservice.exception.InsufficientStockException;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ReservationConcurrencyTest extends IntegrationTest {

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
        product.setName("Test Product");
        product.setStock(10);
        productId = productRepository.save(product).getId();
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldNotExceedStock_underConcurrentRequests() throws InterruptedException {
        int threads = 40;
        int quantityEach = 1;


        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    reservationService.createReservation(productId, quantityEach);
                    successCount.incrementAndGet();
                } catch (InsufficientStockException e) {
                    failCount.incrementAndGet();
                } catch (ConflictException e) {
                    conflictCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        System.out.println("Успешно:        " + successCount.get());
        System.out.println("Нет товара:     " + failCount.get());
        System.out.println("Конфликт lock:  " + conflictCount.get());
        System.out.println("Итого:          " + (successCount.get() + failCount.get() + conflictCount.get()));

        int totalReserved = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .mapToInt(Reservation::getQuantity)
                .sum();

        System.out.println("Зарезервировано в БД: " + totalReserved);


        assertThat(successCount.get() + failCount.get() + conflictCount.get())
                .isEqualTo(threads);


        assertThat(successCount.get())
                .isLessThanOrEqualTo(10);


        assertThat(totalReserved)
                .isLessThanOrEqualTo(10);
    }
}
