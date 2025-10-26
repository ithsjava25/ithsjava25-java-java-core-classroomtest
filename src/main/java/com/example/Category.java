package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Category {

    private static final Map<String, Category> CACHE = new ConcurrentHashMap<>();
    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }

        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Category name can't be blank");
        }

        String formattedName = capitalize(name);

        return CACHE.computeIfAbsent(formattedName, Category::new);
    }

    private static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public String getName() {
        return name;
    }

    public List<Product> findProductsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        return Warehouse.getProducts().stream()
                .filter(p -> p.getCategory().equals(category))
                .toList();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Category{name='" + name + "'}";
    }
}