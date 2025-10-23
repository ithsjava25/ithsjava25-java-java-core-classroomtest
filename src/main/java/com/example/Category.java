package com.example;

import java.util.HashMap;

public class Category {
    public String name;
    static HashMap<String, Category> categories = new HashMap<>();

    private Category(String categoryName) {
        name = categoryName;
        name = name.substring(0, 1).toUpperCase()
                + name.substring(1).toLowerCase();
    }

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        } else if (name.isBlank())
            throw new IllegalArgumentException("Category name can't be blank");
        if (categories.containsKey(name))
                return categories.get(name);
        categories.put(name, new Category(name));
        return categories.get(name);
    }

    public String getName() {
        return name;
    }
}

