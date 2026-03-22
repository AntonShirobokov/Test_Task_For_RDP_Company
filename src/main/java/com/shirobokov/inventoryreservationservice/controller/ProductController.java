package com.shirobokov.inventoryreservationservice.controller;


import com.shirobokov.inventoryreservationservice.dto.ProductResponseDTO;
import com.shirobokov.inventoryreservationservice.dto.ProductTopDTO;
import com.shirobokov.inventoryreservationservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProductInfo(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductInfo(productId));
    }

    @GetMapping("/products/top-reserved")
    public ResponseEntity<List<ProductTopDTO>> getTopReserved() {
        return ResponseEntity.ok(productService.getTopReservedProducts());
    }
}
