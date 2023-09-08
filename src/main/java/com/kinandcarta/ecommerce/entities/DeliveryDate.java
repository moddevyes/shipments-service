package com.kinandcarta.ecommerce.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Slf4j
public class DeliveryDate {

    private Instant valueForDeliveryDate;
}
