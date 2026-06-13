package com.technical.test.prices.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;
}
