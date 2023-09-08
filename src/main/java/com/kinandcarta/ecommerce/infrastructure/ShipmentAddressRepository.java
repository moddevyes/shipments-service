package com.kinandcarta.ecommerce.infrastructure;

import com.kinandcarta.ecommerce.entities.ShipmentAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentAddressRepository extends JpaRepository<ShipmentAddress, Long> {
}
