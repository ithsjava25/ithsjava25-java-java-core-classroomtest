package com.example;

import jdk.jfr.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {

    private final LocalDate expirationDate;
    private final BigDecimal weight; // in kilograms

    public FoodProduct(UUID uuid, String name, Category category, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        super(uuid, name, category, price);

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        this.expirationDate = expirationDate;
        this.weight = weight;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public BigDecimal weight() {
        return weight;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        // Shipping rule: cost = weight * 50
        return weight.multiply(BigDecimal.valueOf(50));
    }

    @Override
    public String productDetails() {
        return "Food: " + name() + ", Expires: " + expirationDate;
    }

    @Override
    public LocalDate getExpiryDate() {
        return null;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public LocalDate expirationDate() {
        return null;
    }
}