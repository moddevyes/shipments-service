package com.kinandcarta.ecommerce.infrastructure;

import com.kinandcarta.ecommerce.entities.Shipments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentsRepository extends JpaRepository<Shipments, Long> {
}
