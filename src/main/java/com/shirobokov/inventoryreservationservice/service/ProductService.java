package com.shirobokov.inventoryreservationservice.service;


import com.shirobokov.inventoryreservationservice.dto.ProductResponseDto;
import com.shirobokov.inventoryreservationservice.exception.ProductNotFoundException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ProductResponseDto getProductInfo(Long productId) {
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        return new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getStock()
        );
    }
}
