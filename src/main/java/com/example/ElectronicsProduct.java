package com.example;

import jdk.jfr.Category;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {

    private final int warrantyMonths;
    private final BigDecimal weight; // in kilograms

    public ElectronicsProduct(UUID uuid, String name, Category category, BigDecimal price, int warrantyMonths, BigDecimal weight) {
        super(uuid, name, category, price);

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        if (weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        this.warrantyMonths = warrantyMonths;
        this.weight = weight;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public BigDecimal weight() {
        return weight;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        // Shipping rule: base 79 + (if heavy > 5kg, add 49)
        BigDecimal cost = BigDecimal.valueOf(79);
        if (weight.compareTo(BigDecimal.valueOf(5)) > 0) {
            cost = cost.add(BigDecimal.valueOf(49));
        }
        return cost;
    }

    @Override
    public String productDetails() {
        return "Electronics: " + name() + ", Warranty: " + warrantyMonths + " months";
    }
}