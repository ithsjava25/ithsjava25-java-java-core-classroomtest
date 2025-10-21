package com.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Category {

    private static final Map<String, Category> cache = new ConcurrentHashMap<>();
    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        if (name == null) throw new IllegalArgumentException("Category name can't be null");
        String trimmed = name.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("Category name can't be blank");
        String normalized = trimmed.substring(0,1).toUpperCase() + trimmed.substring(1).toLowerCase();
        return cache.computeIfAbsent(normalized, Category::new);
    }

    public String getName() {
        return name;
    }
}
