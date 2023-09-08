package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.entities.OrderLineItems;
import com.kinandcarta.ecommerce.entities.Shipments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentsTest {
    Shipments model = new Shipments();
    Long defaultID = 1L;
    String defaultValue = "some value";
    BigDecimal defaultPrice = BigDecimal.ZERO;

    Instant defaultDateTime = Instant.now(Clock.systemUTC());

    Set<OrderLineItems> defaultOrderLineItems = Set.of(OrderLineItems.builder().build());

    @Test
    void modelMethods() {
        model.setId(defaultID);
        model.setShipmentAccount(defaultID);
        model.setShipmentAddress(defaultID);
        model.setDeliveryDate(defaultDateTime);
        model.setShippedDate(defaultDateTime);
        model.setOrderLineItems(defaultOrderLineItems);
        assertThat(model.getId()).isEqualTo(1L);
    }
}
