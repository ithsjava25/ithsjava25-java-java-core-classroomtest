package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class Warehouse {

    public Product[] getProducts() {
        return new Product[0];
    }

    public boolean isEmpty() {
        return false;
    }

    public void addProduct(Product milk) {
    }

    public boolean getProductById(UUID nonExistentId) {
        return false;
    }

    public void updateProductPrice(UUID nonExistentId, BigDecimal newPrice) {
    }

    public org.assertj.core.api.Assertions getProductsGroupedByCategories() {
        return null;
    }
}
