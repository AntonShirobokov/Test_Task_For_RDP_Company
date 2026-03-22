package com.shirobokov.inventoryreservationservice.controller;

import com.shirobokov.inventoryreservationservice.dto.ReservationConfirmDTO;
import com.shirobokov.inventoryreservationservice.dto.ReservationCreateRequestDTO;
import com.shirobokov.inventoryreservationservice.dto.ReservationCreateResponseDTO;
import com.shirobokov.inventoryreservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ReservationCreateResponseDTO> createReservation(@RequestBody ReservationCreateRequestDTO reservationCreateRequestDTO) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreateRequestDTO.productId(), reservationCreateRequestDTO.quantity()));
    }

    @PostMapping("/reservation/{id}/confirm")
    public ResponseEntity<ReservationConfirmDTO> confirmReservation(@PathVariable("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.confirmReservation(reservationId));
    }


}
