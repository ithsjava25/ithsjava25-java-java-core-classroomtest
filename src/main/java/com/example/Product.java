package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {
    UUID uuid;
    String name;
    Category category;
    BigDecimal price;

    public Product(UUID uuid, String name, Category category, BigDecimal price) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        if(price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public BigDecimal price() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public abstract String productDetails();
}
