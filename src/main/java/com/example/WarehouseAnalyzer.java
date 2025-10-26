import com.example.Product;
import com.example.Warehouse;
import com.example.FoodProduct;
import com.example.Perishable;
import com.example.Shippable;
import jdk.jfr.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analyzer class that provides advanced warehouse operations.
 * Students must implement these methods for the advanced tests to pass.
 */
static class WarehouseAnalyzer {


    public WarehouseAnalyzer(Warehouse warehouse) {
        Warehouse warehouse1 = Objects.requireNonNull(warehouse, "Warehouse cannot be null.");
    }
    
    // Search and Filter Methods
    /**
     * Finds all products whose price is within the inclusive range [minPrice, maxPrice].
     * Based on tests: products priced exactly at the boundaries must be included; values outside are excluded.
     *

     * @return a list of products with minPrice <= price <= maxPrice, in the warehouse's iteration order
     */
    public List<Product> findProductsInCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        return Warehouse.getProducts().stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList()).reversed();
    }

    public List<Product> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product p : Warehouse.getProducts()) {
            BigDecimal price = p.price();
            if (price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0) {
                result.add(p);
            }
        }
        return result;
    }
    
    /**
     * Returns all perishable products that expire within the next {@code days} days counting from today,
     * including items that expire today, and excluding items already expired. Non-perishables are ignored.
     * Test expectation: when days = 3, items expiring Today/Tomorrow/In3Days are included; older or non-perishable are not.
     *
     * @param days number of days ahead to include (e.g., 3 includes today, 1, 2, and 3 days ahead)
     * @return list of Perishable items expiring within the window
     */
    public List<Perishable> findProductsExpiringWithinDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);
        List<Perishable> result = new ArrayList<>();
        for (Product p : Warehouse.getProducts()) {
            if (p instanceof Perishable per) {
                LocalDate exp = per.expirationDate();
                if (!exp.isBefore(today) && !exp.isAfter(end)) {
                    result.add(per);
                }
            }
        }
        return result;
    }
    
    /**
     * Performs a case-insensitive partial name search.
     * Test expectation: searching for "milk" returns all products whose name contains that substring,
     * regardless of letter casing or presence of symbols/spaces around it.
     *
     * @param searchTerm substring to search for (case-insensitive)
     * @return list of matching products
     */
    public List<Product> searchProductsByName(String searchTerm) {
        String term = searchTerm.toLowerCase(Locale.ROOT);
        List<Product> result = new ArrayList<>();
        for (Product p : Warehouse.getProducts()) {
            if (p.name().toLowerCase(Locale.ROOT).contains(term)) {
                result.add(p);
            }
        }
        return result;
    }
    
    /**
     * Returns all products whose price is strictly greater than the given price.
     * While not asserted directly by tests, this helper is consistent with price-based filtering.
     *
     * @param price threshold (exclusive)
     * @return list of products with price > threshold
     */
    public List<Product> findProductsAbovePrice(BigDecimal price) {
        List<Product> result = new ArrayList<>();
        for (Product p : Warehouse.getProducts()) {
            if (p.price().compareTo(price) > 0) {
                result.add(p);
            }
        }
        return result;
    }
    
    // Analytics Methods
    /**
     * Computes the average price per category using product weight as the weighting factor when available.
     * Test expectation: for FoodProduct with weights, use weighted average = sum(price*weight)/sum(weight).
     * For categories that contain only non-weighted products, a simple arithmetic mean may be used.
     * The result should round to two decimals in a way that matches the test values (e.g., 11.43 for Dairy example).
     *
     * @return a map from Category to weighted average price
     */
    public Map<Category, BigDecimal> calculateWeightedAveragePriceByCategory() {
        Map<Category, List<Product>> byCat = Warehouse.getProducts().stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        Map<Category, BigDecimal> result = new HashMap<>();

        for (Map.Entry<Category, List<Product>> e : byCat.entrySet()) {
            Category cat = e.getKey();
            List<Product> items = e.getValue();

            BigDecimal weightedSum = BigDecimal.ZERO;
            BigDecimal weightSum = BigDecimal.ZERO;

            for (Product p : items) {
                if (p instanceof Shippable s) {
                    BigDecimal w = Optional.ofNullable(s.weight()).orElse(BigDecimal.ZERO);
                    if (w.compareTo(BigDecimal.ZERO) > 0) {
                        weightedSum = weightedSum.add(p.price().multiply(w));
                        weightSum = weightSum.add(w);
                    }
                }
            }

            BigDecimal avg;
            if (weightSum.compareTo(BigDecimal.ZERO) > 0) {
                avg = weightedSum.divide(weightSum, 2, RoundingMode.HALF_UP);
            } else {
                BigDecimal sum = items.stream()
                        .map(Product::price)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                avg = sum.divide(BigDecimal.valueOf(items.size()), 2, RoundingMode.HALF_UP);
            }

            result.put(cat, avg);
        }

        return result;
    }
    
    /**
     * Identifies products whose price deviates from the mean by more than the specified
     * number of standard deviations. Uses population standard deviation over all products.
     * Test expectation: with a mostly tight cluster and two extremes, calling with 2.0 returns the two extremes.
     *
     * @param standardDeviations threshold in standard deviations (e.g., 2.0)
     * @return list of products considered outliers
     */
    public List<Product> findPriceOutliers(double standardDeviations) {
        List<Product> products = List.of();
        int n = 0;
        return List.of();
    }
    
    /**
     * Groups all shippable products into ShippingGroup buckets such that each group's total weight
     * does not exceed the provided maximum. The goal is to minimize the number of groups and/or total
     * shipping cost, but the exact algorithm is implementation-defined (e.g., first-fit decreasing).
     * Test expectation: for a max weight of 10.0, every group's totalWeight <= 10.0 and all items are included.
     *
     * @param maxWeightPerGroup maximum total weight per group (inclusive)
     * @return list of ShippingGroup objects covering all shippable products
     */
    public List<ShippingGroup> optimizeShippingGroups(BigDecimal maxWeightPerGroup) {
        if (maxWeightPerGroup == null || maxWeightPerGroup.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max weight per group must be positive.");
        }


        List<Shippable> items = new ArrayList<>(Warehouse.shippableProducts());


        items.sort((a, b) -> b.weight().compareTo(a.weight()));


        List<List<Shippable>> bins = new ArrayList<>();


        for (Shippable item : items) {
            BigDecimal itemWeight = item.weight() != null ? item.weight() : BigDecimal.ZERO;
            boolean placed = false;

            for (List<Shippable> bin : bins) {
                BigDecimal currentWeight = bin.stream()
                        .map(Shippable::weight)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (currentWeight.add(itemWeight).compareTo(maxWeightPerGroup) <= 0) {
                    bin.add(item);
                    placed = true;
                    break;
                }
            }


            if (!placed) {
                bins.add(new ArrayList<>(List.of(item)));
            }
        }


        return bins.stream()
                .map(ShippingGroup::new)
                .toList();
    }
}
    
    // Business Rules Methods
    /**
     * Calculates discounted prices for perishable products based on proximity to expiration.
     * Discount rules from tests:
     *  - Expires today: 50% discount (price * 0.50)
     *  - Expires tomorrow: 30% discount (price * 0.70)
     *  - Expires within 3 days: 15% discount (price * 0.85)
     *  - Otherwise (including >3 days ahead): no discount
     * Non-perishable products should retain their original price.
     *
     * @return a map from Product to its discounted price
     */
    public Map<Product, BigDecimal> calculateExpirationBasedDiscounts() {
        Map<Product, BigDecimal> result = new HashMap<>();
        LocalDate today = LocalDate.now();

        ShippingGroup warehouse = null;
        for (Shippable p : warehouse.getProducts()) {
            BigDecimal discounted = p.price();

            if (p instanceof FoodProduct f) {
                LocalDate exp = f.getExpirationDate();
                long daysBetween = ChronoUnit.DAYS.between(today, exp);

                if (daysBetween == 0) {
                    discounted = p.price().multiply(new BigDecimal("0.50"));
                } else if (daysBetween == 1) {
                    discounted = p.price().multiply(new BigDecimal("0.70"));
                } else if (daysBetween > 1 && daysBetween <= 3) {
                    discounted = p.price().multiply(new BigDecimal("0.85"));
                }

                discounted = discounted.setScale(2, RoundingMode.HALF_UP);
            }

            result.put((Product) p, discounted);
        }

        return result;
    }
    
    /**
     * Evaluates inventory business rules and returns a summary:
     *  - High-value percentage: proportion of products considered high-value (e.g., price >= some threshold).
     *    The tests imply a scenario where 15 of 20 items (priced 2000) yield ~75% and should trigger a warning
     *    when percentage exceeds 70%.
     *  - Category diversity: count of distinct categories in the inventory. The tests expect at least 2.
     *  - Convenience booleans: highValueWarning (percentage > 70%) and minimumDiversity (category count >= 2).
     *
     * Note: The exact high-value threshold is implementation-defined, but the provided tests create a clear
     * separation using very expensive electronics (e.g., 2000) vs. low-priced food items (e.g., 10),
     * allowing percentage computation regardless of the chosen cutoff as long as it matches the scenario.
     *
     * @return InventoryValidation summary with computed metrics
     */
    public InventoryValidation validateInventoryConstraints() {
        ShippingGroup warehouse;
        List<Product> items = Warehouse.getProducts();
        if (items.isEmpty()) return new InventoryValidation(0.0, 0);
        BigDecimal highValueThreshold = new BigDecimal("1000");
        long highValueCount = items.stream().filter(p -> p.price().compareTo(highValueThreshold) >= 0).count();
        double percentage = (highValueCount * 100.0) / items.size();
        int diversity = (int) items.stream().map(Product::category).distinct().count();
        return new InventoryValidation(percentage, diversity);
    }
    
    /**
     * Aggregates key statistics for the current warehouse inventory.
     * Test expectation for a 4-item setup:
     *  - totalProducts: number of products (4)
     *  - totalValue: sum of prices (1590.50)
     *  - averagePrice: totalValue / totalProducts rounded to two decimals (397.63)
     *  - expiredCount: number of perishable items whose expiration date is before today (1)
     *  - categoryCount: number of distinct categories across all products (2)
     *  - mostExpensiveProduct / cheapestProduct: extremes by price
     *
     * @return InventoryStatistics snapshot containing aggregated metrics
     */
    public InventoryStatistics getInventoryStatistics() {
        List<Product> items = Warehouse.getProducts();
        int totalProducts = items.size();

        BigDecimal totalValue = items.stream()
                .map(Product::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averagePrice = totalProducts == 0
                ? BigDecimal.ZERO
                : totalValue.divide(BigDecimal.valueOf(totalProducts), 2, RoundingMode.HALF_UP);

        int expiredCount = 0;
        for (Product p : items) {
            if (p instanceof FoodProduct f && f.getExpirationDate().isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }

        int categoryCount = (int) items.stream()
                .map(Product::getCategory)
                .distinct()
                .count();

        Product mostExpensive = items.stream()
                .max(Comparator.comparing(Product::price))
                .orElse(null);

        Product cheapest = items.stream()
                .min(Comparator.comparing(Product::price))
                .orElse(null);

        return new InventoryStatistics(
                totalProducts,
                totalValue,
                averagePrice,
                expiredCount,
                categoryCount,
                mostExpensive,
                cheapest
        );
    }

/**
 * Represents a group of products for shipping
 */
static class ShippingGroup {
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
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Shippable> getProducts() { return new ArrayList<>(products); }
    public BigDecimal getTotalWeight() { return totalWeight; }
    public BigDecimal getTotalShippingCost() { return totalShippingCost; }
}

/**
 * Validation result for inventory constraints
 */
static class InventoryValidation {
    private final double highValuePercentage;
    private final int categoryDiversity;
    private final boolean highValueWarning;
    private final boolean minimumDiversity;

    public InventoryValidation(double highValuePercentage, int categoryDiversity) {
        this.highValuePercentage = highValuePercentage;
        this.categoryDiversity = categoryDiversity;
        this.highValueWarning = highValuePercentage > 70.0;
        this.minimumDiversity = categoryDiversity >= 2;
    }

    public double getHighValuePercentage() { return highValuePercentage; }
    public int getCategoryDiversity() { return categoryDiversity; }
    public boolean isHighValueWarning() { return highValueWarning; }
    public boolean hasMinimumDiversity() { return minimumDiversity; }
}


public List<ShippingGroup> optimizeShippingGroups(BigDecimal maxWeightPerGroup) {
    if (maxWeightPerGroup == null || maxWeightPerGroup.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Max weight per group must be positive.");
    }

    // Get all shippable items
    List<Shippable> items = new ArrayList<>(Warehouse.shippableProducts());

    // Sort by descending weight (First-Fit Decreasing)
    items.sort((a, b) -> b.weight().compareTo(a.weight()));

    List<List<Shippable>> bins = new ArrayList<>();

    for (Shippable item : items) {
        BigDecimal itemWeight = item.weight() != null ? item.weight() : BigDecimal.ZERO;
        boolean placed = false;

        for (List<Shippable> bin : bins) {
            BigDecimal currentWeight = bin.stream()
                    .map(Shippable::weight)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (currentWeight.add(itemWeight).compareTo(maxWeightPerGroup) <= 0) {
                bin.add(item);
                placed = true;
                break;
            }
        }

        if (!placed) {
            bins.add(new ArrayList<>(List.of(item)));
        }
    }

    // Convert bins into ShippingGroup objects
    return bins.stream()
            .map(ShippingGroup::new)
            .toList();
}


/**
 * Comprehensive inventory statistics
 */
record InventoryStatistics(int totalProducts, BigDecimal totalValue, BigDecimal averagePrice, int expiredCount,
                           int categoryCount, Product mostExpensiveProduct, Product cheapestProduct) {
}

void main() {
}