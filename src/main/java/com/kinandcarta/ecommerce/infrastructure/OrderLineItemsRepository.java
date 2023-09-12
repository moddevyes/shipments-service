package com.kinandcarta.ecommerce.infrastructure;

import com.kinandcarta.ecommerce.entities.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Long> {
}
