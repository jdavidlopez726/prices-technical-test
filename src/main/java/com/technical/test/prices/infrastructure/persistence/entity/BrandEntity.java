package com.technical.test.prices.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class BrandEntity extends AuditableEntity {

    @Id
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "name", nullable = false)
    private String name;
}
