package com.kinandcarta.ecommerce.infrastructure;

import com.kinandcarta.ecommerce.entities.ShipmentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentAccountRepository extends JpaRepository<ShipmentAccount, Long> {
}
