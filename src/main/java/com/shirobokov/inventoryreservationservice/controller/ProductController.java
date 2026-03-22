package com.shirobokov.inventoryreservationservice.controller;


import com.shirobokov.inventoryreservationservice.dto.ProductResponseDTO;
import com.shirobokov.inventoryreservationservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductInfo(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProductInfo(productId));
    }
}
