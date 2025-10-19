package com.example;

import jdk.jfr.Category;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static Warehouse instance;

    private final String name;
    private static final Map<UUID, Product> products = new LinkedHashMap<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance(String name) {
        if (instance == null) {
            instance = new Warehouse(name);
        }
        return instance;
    }

    public void clearProducts() {
        products.clear();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.put(product.uuid(), product);
    }

    public void remove(UUID id) {
        products.remove(id);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public static List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        if (products.isEmpty()) {
            return Collections.emptyMap();
        }

        return products.values().stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
    }

    public void updateProductPrice(UUID productId, BigDecimal newPrice) {
        Product product = products.get(productId);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + productId);
        }
        product.setPrice(newPrice);
    }

    public String getName() {
        return name;
    }
    public List<Perishable> expiredProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Perishable)
                .map(p -> (FoodProduct) p)
                .filter(FoodProduct::isExpired)
                .map(p -> (Perishable) p)
                .toList();
    }
    public static List<Shippable> shippableProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .toList();
    }

}