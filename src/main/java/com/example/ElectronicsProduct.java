package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    int warrantyMonths;
    BigDecimal weight;
    BigDecimal basePrice = BigDecimal.valueOf(79);
    BigDecimal addPrice = BigDecimal.valueOf(49);

    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, int warranty, BigDecimal weight) {
        super(id, name, category, price);
        this.warrantyMonths = warranty;
        if (warrantyMonths < 0)
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        this.weight = weight;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String productDetails() {
        return "Electronics: " + name + ", Warranty: " + warrantyMonths + " months";
    }

    @Override
    public double weight() {
        return weight.doubleValue();
    }

    @Override
    public BigDecimal calculateShippingCost() {
        return (weight.compareTo(BigDecimal.valueOf(5.0)) >= 0)
            ? basePrice.add(addPrice)
            : basePrice;
    }
}
