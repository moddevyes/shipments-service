package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.infrastructure.ShipmentsRepository;
import com.kinandcarta.ecommerce.entities.Shipments;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

class ShipmentsHandlerTest {
    TestEntityManager entityManager = Mockito.mock(TestEntityManager.class);
    ShipmentsRepository shipmentsRepository = Mockito.mock(ShipmentsRepository.class);
    ShipmentsHandler shipmentsHandler;

    Shipments initialShipment = Shipments.builder()
            .id(1L)
            .shipmentAccount(1L)
            .shipmentAddress(2L)
            .shippedDate(Instant.now())
            .deliveryDate(Instant.now().plus(3, ChronoUnit.DAYS))
            .build();

    @BeforeEach void setUp() {
        shipmentsHandler = new ShipmentsHandler(shipmentsRepository);
        entityManager.persist(initialShipment);
    }

    @AfterEach void tearDown() { entityManager.flush(); }

    @Test
    void shouldCreate_find_andUpdate_newShipment() {
        // create shipment
        when(shipmentsRepository.save(initialShipment)).thenReturn(initialShipment);
        // is it there?
        when(shipmentsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        // find it
        when(shipmentsRepository.getReferenceById(1L)).thenReturn(initialShipment);

        Shipments updateShipment = shipmentsHandler.update(1L, initialShipment);
        updateShipment.setShipmentAccount(2L);
        updateShipment.setDeliveryDate(Instant.now().plus(5, ChronoUnit.DAYS));
        assertThat(updateShipment).isNotNull();
    }

    @Test void shouldDeleteShipment() {
        when(shipmentsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        willDoNothing().given(shipmentsRepository).deleteById(1L);
        shipmentsHandler.delete(1L);
        assertThat(shipmentsHandler.findById(1L)).isNull();
    }

    @Test void shouldFindShipment_byId() {
        when(shipmentsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        when(shipmentsRepository.getReferenceById(1L)).thenReturn(initialShipment);
        Shipments findByIdShipment = shipmentsHandler.findById(1L);
        assertThat(findByIdShipment).isNotNull();
        assertThat(findByIdShipment.getShipmentAccount()).isEqualTo(initialShipment.getShipmentAccount());
        assertThat(findByIdShipment.getShipmentAddress()).isEqualTo(initialShipment.getShipmentAddress());
        assertThat(findByIdShipment.getShippedDate()).isEqualTo(initialShipment.getShippedDate());
        assertThat(findByIdShipment.getShippedDate()).isEqualTo(initialShipment.getShippedDate());
    }

    @Test void shouldFindAll_Shipments() {
        when(shipmentsRepository.findAll()).thenReturn(List.of(initialShipment));
        Set<Shipments> findAllShipments = shipmentsHandler.findAll();
        assertThat(findAllShipments)
                .isNotEmpty()
                .hasSize(1);
    }

}
