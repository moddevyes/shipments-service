package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.entities.*;
import com.kinandcarta.ecommerce.exceptions.MinimumShipmentFieldsNotProvidedException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ShipmentsControllerTests {

    @Mock
    ShipmentsHandler shipmentsHandler;

    ShipmentsController controller;

    ShipmentAddress shipmentAddress = ShipmentAddress.builder().id(100L)
            .address1("1001 Shipment Effencient Drive")
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

    OrderLineItems firstProduct = OrderLineItems.builder()
            .id(3L)
            .orderId(1L)
            .quantity(2)
            .price(new BigDecimal("10"))
            .productId(1L)
            .build();
    OrderLineItems secondProduct = OrderLineItems.builder()
            .id(4L)
            .orderId(1L)
            .quantity(1)
            .price(new BigDecimal("13.99"))
            .productId(3L)
            .build();
    Shipments shipments = Shipments.
            builder()
            .id(1L)
            .shipmentAccount(shipmentAccount)
            .shipmentAddress(shipmentAddress)
            .deliveryDate(deliveryDate)
            .shippedDate(shippedDate)
            .orderLineItems(Set.of(firstProduct, secondProduct))
            .build();

    Shipments shipmentsMoonMousePads = Shipments.
            builder()
            .id(4L)
            .shipmentAccount(shipmentAccount)
            .shipmentAddress(shipmentAddress)
            .deliveryDate(deliveryDate)
            .shippedDate(shippedDate)
            .orderLineItems(Set.of(firstProduct, secondProduct))
            .build();

    Shipments shipmentsNoAccount = Shipments.
            builder()
            .id(1L)
            .shipmentAccount(null)
            .shipmentAddress(shipmentAddress)
            .deliveryDate(deliveryDate)
            .shippedDate(shippedDate)
            .orderLineItems(null)
            .build();

    Shipments shipmentsNoAccountNorAddress = Shipments.
            builder()
            .id(1L)
            .shipmentAccount(null)
            .shipmentAddress(null)
            .deliveryDate(deliveryDate)
            .shippedDate(shippedDate)
            .orderLineItems(Set.of(OrderLineItems.builder().build()))
            .build();

    @BeforeEach void setUp() {
        controller = new ShipmentsController(shipmentsHandler);
    }

    @Test void shouldCreateA_NewShipment() {
        assertThat(Shipments.
                builder()
                .id(1L)
                .shipmentAccount(shipmentAccount)
                .shipmentAddress(shipmentAddress)
                .deliveryDate(deliveryDate)
                .shippedDate(shippedDate)
                .orderLineItems(Set.of(OrderLineItems.builder().build()))
                .build()).isNotNull().hasFieldOrProperty("id")
                .hasFieldOrProperty("shipmentAccount")
                .hasFieldOrProperty("shipmentAddress")
                .hasFieldOrProperty("deliveryDate")
                .hasFieldOrProperty("shippedDate")
                .hasFieldOrProperty("createDateTime")
                .hasFieldOrProperty("updateDateTime");
    }

    @Test void shouldNOT_CreateShipment_withShippingAccount() {
        when(shipmentsHandler.create(shipmentsNoAccount)).thenThrow(new MinimumShipmentFieldsNotProvidedException("FAILED: shouldCreateShipment_withRequiredFields"));
        ResponseEntity<Shipments> createShipmentCmd = controller.create(shipmentsNoAccount);
        assertThat(createShipmentCmd.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test void shouldNOT_CreateShipment_without_ShippingAccount_Or_ShippingAddress() {
        when(shipmentsHandler.create(shipmentsNoAccountNorAddress)).thenThrow(new MinimumShipmentFieldsNotProvidedException("FAILED: shouldNOT_CreateShipment_without_ShippingAccount_Or_ShippingAddress"));
        ResponseEntity<Shipments> createShipmentCmd = controller.create(shipmentsNoAccountNorAddress);
        assertThat(createShipmentCmd.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test void should_createNewShipment() {
        ResponseEntity<Shipments> createShipmentCmd = createNewShipment_Given();
        assertionsForNewShipment(createShipmentCmd);
    }

    @Test void should_findAllShipments() {
        ResponseEntity<Shipments> createShipmentCmd = createNewShipment_Given(shipmentsMoonMousePads);
        assertionsForNewShipment(createShipmentCmd);
        when(shipmentsHandler.findAll()).thenReturn(Set.of(shipments, shipmentsMoonMousePads));

        ResponseEntity<Set<Shipments>> setOfShipments = controller.findAll();

        assertThat(setOfShipments).isNotNull();
        assertThat(setOfShipments.getBody()).isNotNull();
        Set<Shipments> shipmentsLocated = setOfShipments.getBody();
        assertThat(shipmentsLocated).hasSize(2);
    }

    @Test void should_findShipments_byID() {
        ResponseEntity<Shipments> createShipmentCmd = createNewShipment_Given(shipmentsMoonMousePads);
        assertionsForNewShipment(createShipmentCmd);
        when(shipmentsHandler.findById(1L)).thenReturn(shipmentsMoonMousePads);

        ResponseEntity<Shipments> shipments = controller.findById(1L);

        assertThat(shipments).isNotNull();
        assertThat(shipments.getBody()).isNotNull();
        Shipments shipmentsLocated = shipments.getBody();
        assertThat(shipmentsLocated.getShipmentAccount()).isNotNull();
        assertThat(shipmentsLocated.getShipmentAddress()).isNotNull();
        assertThat(shipmentsLocated.getOrderLineItems()).hasSize(2);
    }

    @Test void should_UpdateShipment_ThatDoesntExist() {
        // given, find the existing shipment
        when(shipmentsHandler.update(1L, shipments)).thenReturn(shipments);
        ResponseEntity<Shipments> shipmentsUpdateCommand = controller.update(1L, shipments);
        assertThat(shipmentsUpdateCommand).isNotNull();
        assertThat(shipmentsUpdateCommand.getBody()).isNotNull();
        log.debug("FoundShipment with ID - " + shipmentsUpdateCommand.getBody().getId() + " forUpdate: " + shipmentsUpdateCommand);

        // update, change_shipping_address_usecase
        Shipments updatingShipment = shipmentsUpdateCommand.getBody();
        updatingShipment.setShipmentAddress(ShipmentAddress.builder()
                .id(100L)
                .address1("100")
                .address2("")
                .city("Food Forest City")
                .state("FL")
                .province("")
                .postalCode("33000")
                .country("US").build());

        // assert, comparisons
        assertThat(shipments.getShipmentAddress().getId()).isEqualTo(updatingShipment.getShipmentAddress().getId());
        assertThat(shipments.getShipmentAddress()).isEqualTo(updatingShipment.getShipmentAddress());
    }

    @Test void should_DELETE_Shipment() {
        when(shipmentsHandler.create(shipments)).thenReturn(shipments);
        doNothing().when(shipmentsHandler).delete(shipments.getId());
        ResponseEntity<Shipments> createShipmentCmd = createNewShipment_Given();
        controller.delete(1L);

        verify(shipmentsHandler, times(1)).delete(1L);
    }

    private void assertionsForNewShipment(ResponseEntity<Shipments> shipmentCreated) {
        assertThat(shipmentCreated).isNotNull();
        assertThat(shipmentCreated.getBody()).isNotNull();
        assertThat(shipmentCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
        Shipments shipment = shipmentCreated.getBody();
        assertThat(shipment.getShipmentAccount()).isNotNull();
        assertThat(shipment.getShipmentAddress()).isNotNull();
        assertThat(shipment.getShipmentAccount().getId()).isEqualTo(100L);
    }
    private ResponseEntity<Shipments> createNewShipment_Given() {
        when(shipmentsHandler.create(shipments)).thenReturn(shipments);
        return controller.create(shipments);
    }

    private ResponseEntity<Shipments> createNewShipment_Given(final Shipments shipmentCreateCommand) {
        when(shipmentsHandler.create(shipmentCreateCommand)).thenReturn(shipmentCreateCommand);
        return controller.create(shipmentCreateCommand);
    }
}
