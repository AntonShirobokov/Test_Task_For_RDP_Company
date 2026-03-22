package com.shirobokov.inventoryreservationservice.repository;

import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.repository.projection.ProductTopProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    @Query("""
        SELECT p.id AS id, p.name AS name, COUNT(r) AS confirmedCount
        FROM Product p
        JOIN p.reservations r
        WHERE r.status = 'CONFIRMED'
        AND r.createdAt >= :since
        GROUP BY p.id, p.name
        ORDER BY COUNT(r) DESC
        LIMIT 5
        """)
    List<ProductTopProjection> findTopReservedProducts(@Param("since") Instant since);
}
