package com.example;

import com.example.Shippable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

class ShippingGroup {
    private final List<Shippable> products;
    private final BigDecimal totalWeight;
    private final BigDecimal totalShippingCost;

    public ShippingGroup(List<Shippable> products) {
        this.products = new ArrayList<>(products);


        this.totalWeight = products.stream()
                .map(Shippable::weight)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        this.totalShippingCost = products.stream()
                .map(Shippable::calculateShippingCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Shippable> getProducts() {
        return new ArrayList<>(products);
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public BigDecimal getTotalShippingCost() {
        return totalShippingCost;
    }
}