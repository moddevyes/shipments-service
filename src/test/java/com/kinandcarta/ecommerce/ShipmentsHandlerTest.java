package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.entities.*;
import com.kinandcarta.ecommerce.exceptions.ShipmentDoesNotExistException;
import com.kinandcarta.ecommerce.infrastructure.ShipmentsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

class ShipmentsHandlerTest {
    TestEntityManager entityManager = Mockito.mock(TestEntityManager.class);
    ShipmentsRepository shipmentsRepository = Mockito.mock(ShipmentsRepository.class);
    ShipmentsHandler shipmentsHandler;

    OrderLineItems firstProduct = OrderLineItems.builder()
            .id(3L)
            .orderId(1L)
            .quantity(2)
            .price(new BigDecimal("10"))
            .productId(1L)
            .build();
    ShipmentAddress shipmentAddress = ShipmentAddress.builder().id(100L)
            .address1("1001 Shipment Efficient Drive")
            .address2("Suite 1001")
            .city("Shipment Zone City")
            .state("FL")
            .province("Fast OnTime Province")
            .postalCode("33000")
            .country("US").build();
    ShipmentAccount shipmentAccount = ShipmentAccount.builder()
            .id(100L)
            .firstName("ShipFirstName")
            .lastName("ShipLastName")
            .emailAddress("shipfirst.last@arrivingfivedays.com")
            .addresses(
                    Set.of(shipmentAddress)).build();

    DeliveryDate deliveryDate = DeliveryDate.builder().valueForDeliveryDate(Instant.now(Clock.systemUTC())).build();

    ShippedDate shippedDate = ShippedDate.builder().valueForShippedDate(Instant.now(Clock.systemUTC())).build();
    Shipments initialShipment = Shipments.builder()
            .id(1L)
            .shipmentAccount(shipmentAccount)
            .shipmentAddress(shipmentAddress)
            .shippedDate(shippedDate)
            .deliveryDate(deliveryDate)
            .orderLineItems(Set.of(firstProduct))
            .build();

    @BeforeEach void setUp() {
        shipmentsHandler = new ShipmentsHandler(shipmentsRepository);
        entityManager.persist(initialShipment);
    }

    @AfterEach void tearDown() {
        entityManager.flush();
    }

    @Test
    void shouldUpdate_Shipment() {
        // create shipment
        when(shipmentsRepository.save(initialShipment)).thenReturn(initialShipment);
        // is it there?
        when(shipmentsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        // find it
        when(shipmentsRepository.getReferenceById(1L)).thenReturn(initialShipment);

        Shipments updateShipment = shipmentsHandler.update(1L, initialShipment);
        updateShipment.setShipmentAccount(shipmentAccount);
        updateShipment.setDeliveryDate(deliveryDate);
        assertThat(updateShipment).isNotNull();
        verify(shipmentsRepository).save(updateShipment);
    }

    @Test
    void shouldCreateShipment() {
        when(shipmentsRepository.save(initialShipment)).thenReturn(initialShipment);
        Shipments createShipment = shipmentsHandler.create(initialShipment);
        assertThat(createShipment).isNotNull();
    }
    @Test void shouldDeleteShipment() {
        when(shipmentsRepository.save(initialShipment)).thenReturn(initialShipment);
        when(shipmentsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        willDoNothing().given(shipmentsRepository).deleteById(1L);
        shipmentsHandler.delete(1L);
        assertThat(shipmentsHandler.findById(1L)).isNull();
    }

    @Test void shouldNOT_DeleteShipmentThatDoesNotExist() {
        when(shipmentsRepository.existsById(1L)).thenThrow(ShipmentDoesNotExistException.class);
        doNothing().when(shipmentsRepository).deleteById(1L);
        shipmentsHandler.delete(1L);
        verify(shipmentsRepository).deleteById(1L);
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
