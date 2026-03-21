package com.shirobokov.inventoryreservationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private Integer stock;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
