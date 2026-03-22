package com.shirobokov.inventoryreservationservice.service;


import com.shirobokov.inventoryreservationservice.dto.ProductResponseDTO;
import com.shirobokov.inventoryreservationservice.exception.ProductNotFoundException;
import com.shirobokov.inventoryreservationservice.model.Product;
import com.shirobokov.inventoryreservationservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
