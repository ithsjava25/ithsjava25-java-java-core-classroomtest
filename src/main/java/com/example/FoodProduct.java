package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    public LocalDate expirationDate;
    public BigDecimal weight;

    public FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        super(id, name, category, price);
        this.expirationDate = expirationDate;
        if (weight.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Weight cannot be negative.");
        this.weight = weight;
    }

    @Override
    public LocalDate expirationDate() {
        return expirationDate;
    }

    @Override
    public String productDetails() {
        return "Food: " + name + ", Expires: " + expirationDate;
    }

    @Override
    public double weight() {
        return weight.doubleValue();
    }

    @Override
    public BigDecimal calculateShippingCost() {
        return weight.multiply(BigDecimal.valueOf(50));
    }
}