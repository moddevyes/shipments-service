package com.kinandcarta.ecommerce;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {

    Products model = new Products();

    Long defaultID = 1L;
    String defaultValue = "some value";
    BigDecimal defaultPrice = BigDecimal.ZERO;

    Instant defaultDateTime = Instant.now(Clock.systemUTC());

    @Test
    void modelMethods() {
        model.setId(defaultID);
        model.setName(defaultValue);
        model.setProductDescription(defaultValue);
        model.setImage(defaultValue);
        model.setUnitPrice(defaultPrice);
        model.setCreateDateTime(defaultDateTime);
        model.setUpdateDateTime(defaultDateTime);
        assertThat(model.getId()).isEqualTo(1L);
    }
}