package com.nghiasoftware.bookshop_product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "product")
@Getter
@Setter
public class Product {

    @Id
    private String id;

    private String title;
    private String author;
    private int review;
    private double price;
    private String images;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = LocalDateTime.now();
    }

}
