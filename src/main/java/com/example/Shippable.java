package com.example;

import java.math.BigDecimal;

public interface Shippable {

    BigDecimal weight();


    BigDecimal calculateShippingCost();

    BigDecimal price();
}
