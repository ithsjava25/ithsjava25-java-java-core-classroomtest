package com.example;

import jdk.jfr.Category;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public abstract class Product {
    private final UUID uuid;
    private final String name;
    private final Category category;
    private BigDecimal price;

    protected Product(UUID uuid, String name, Category category, BigDecimal price) {
        this.uuid = Objects.requireNonNull(uuid, "UUID cannot be null.");
        this.name = validateName(name);
        this.category = Objects.requireNonNull(category, "Category cannot be null.");
        this.price = validatePrice(price);
    }


    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal price() {
        return price;
    }


    public void setPrice(BigDecimal newPrice) {
        this.price = validatePrice(newPrice);
    }


    public abstract String productDetails();


    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank.");
        }
        return name.trim();
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        return price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return name + " (" + category.getClass() + ")";
    }

    public Category category() {
        return null;
    }
}