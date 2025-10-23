package com.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Warehouse {
    static HashMap<String, Warehouse> warehouses = new HashMap<>();
    HashMap<UUID, Product> products;
    List<Shippable> shippables;
    List<Perishable> perishables;
    HashMap<Optional<Product>, HashMap<LocalDateTime, BigDecimal>> changedProducts;

    private Warehouse() {
        this.products = new HashMap<>();
        this.shippables = new ArrayList<>();
        this.perishables = new ArrayList<>();
        this.changedProducts = new HashMap<>();
    }

    public List<Product> getProducts(){
        return List.copyOf(products.values());
    }

    public List<Shippable> shippableProducts() {
        return shippables;
    }

    public void clearProducts(){
        products.clear();
        Category.categories.clear();
        shippables.clear();
        perishables.clear();
    }

    public boolean isEmpty(){
        return products.isEmpty();
    }

    public void addProduct(Product product){
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null.");
        if (getProductById(product.uuid).isPresent())
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        products.put(product.uuid, product);
        if(product instanceof Shippable)
            shippables.add((Shippable) product);
        if(product instanceof Perishable)
            perishables.add((Perishable) product);
    }

    public void remove(UUID id){
        products.remove(id);
    }

    public Optional<Product> getProductById(UUID id){
       if (products.isEmpty())
           return Optional.empty();
        return (Optional.ofNullable(products.get(id)));
    }

    public static synchronized Warehouse getInstance(String string) {
        if (!warehouses.containsKey(string)) {
            warehouses.put(string, new Warehouse());
        }
        return warehouses.get(string);
    }

    public static synchronized Warehouse getInstance() {
        if(!warehouses.containsKey("default"))
            warehouses.put("default", new  Warehouse());
        return warehouses.get("default");
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        if(products == null || products.isEmpty()){
            return Collections.emptyMap();
        }
        HashMap<Category, List<Product>> result = new HashMap<>();
        var tempMap = Category.categories.entrySet();
        var productsList = new ArrayList<>(products.values());
        for(var category : tempMap){
            List<Product> tempList = new ArrayList<>();
            productsList.stream()
                    .filter(element -> element.category.equals(category.getValue()))
                    .forEach(tempList::add);
            result.put(category.getValue(), tempList);
        }
        return result;
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice){
        var product = getProductById(id);
        if(product.isPresent()) {
            var tempMap = new HashMap<LocalDateTime, BigDecimal>();
            tempMap.put(LocalDateTime.now(), product.get().price);
            changedProducts.put(product, tempMap);
            product.get().setPrice(newPrice);
        }
        else
            throw new NoSuchElementException("Product not found with id:" + id);
    }

    public Map<Optional<Product>, HashMap<LocalDateTime, BigDecimal>> getChangedProducts(){
            return changedProducts;
    }

    public List<Perishable> expiredProducts() {
        return perishables.stream()
                .filter(Perishable::isExpired)
                .toList();
    }
}

