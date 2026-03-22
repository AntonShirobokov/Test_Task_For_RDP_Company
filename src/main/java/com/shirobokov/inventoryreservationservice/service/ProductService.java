package com.shirobokov.inventoryreservationservice.service;


import com.shirobokov.inventoryreservationservice.dto.ProductResponseDTO;
import com.shirobokov.inventoryreservationservice.dto.ProductTopDTO;
import com.shirobokov.inventoryreservationservice.exception.ProductNotFoundException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ProductResponseDTO getProductInfo(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }

    public List<ProductTopDTO> getTopReservedProducts() {
        Instant since = Instant.now().minus(24, ChronoUnit.HOURS);
        return productRepository.findTopReservedProducts(since)
                .stream()
                .map(p -> new ProductTopDTO(p.getId(), p.getName(), p.getConfirmedCount()))
                .toList();
    }
}
