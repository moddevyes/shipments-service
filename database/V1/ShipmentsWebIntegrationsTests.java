package com.kinandcarta.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kinandcarta.ecommerce.infrastructure.ShipmentsRepository;
import com.kinandcarta.ecommerce.entities.OrderLineItems;
import com.kinandcarta.ecommerce.entities.Shipments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ShipmentsController.class)
@Import(ShipmentsHandler.class)
class ShipmentsWebIntegrationsTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShipmentsRepository shipmentsRepository;

    ObjectMapper mapper = new ObjectMapper();

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

    @BeforeEach void setUp() {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreate_Shipment() throws Exception {
        final String json = toJSON(shipmentNew());
        whenConditionsFor_CreateShipmentsWithId(shipmentNew());
        mockMvc.perform(MockMvcRequestBuilders.post("/shipments")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippedDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].orderId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].productId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].quantity").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].price").exists());
    }
    @Test @Disabled
    void shouldUpdate_Shipment() throws Exception {
        final String json = toJSON(shipmentNew());
        whenConditionsFor_FindShipmentsById(shipmentNew());
        mockMvc.perform(MockMvcRequestBuilders.put("/shipments/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
    @Test
    void shouldDelete_Shipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shipments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void shouldFindById_Shipment() throws Exception {
        whenConditionsFor_FindShipmentsById(shipmentNew());
        mockMvc.perform(MockMvcRequestBuilders.get("/shipments/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippedDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].orderId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].productId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].quantity").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems[0].price").exists());
    }
    @Test
    void shouldFindAll_Shipment() throws Exception {
        whenConditionsFor_FindAllShipments();
        mockMvc.perform(MockMvcRequestBuilders.get("/shipments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].shippedDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].deliveryDate").exists())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    private String toJSON(final Shipments shipmentToJson) throws Exception {
        return mapper.writeValueAsString(shipmentToJson);
    }
    private Shipments shipmentNew() {
        return  Shipments.builder()
                .id(1L)
                .shipmentAccount(1L)
                .shipmentAddress(2L)
                .shippedDate(Instant.now())
                .deliveryDate(Instant.now().plus(3, ChronoUnit.DAYS))
                .orderLineItems(Set.of(firstProduct, secondProduct))
                .build();
    }
    private void whenConditionsFor_CreateShipmentsWithId(final Shipments shipmentsCreate) {
        when(shipmentsRepository.save(shipmentsCreate)).thenReturn(shipmentsCreate);
        when(shipmentsRepository.getReferenceById(shipmentsCreate.getId())).thenReturn(shipmentsCreate);
    }

    private void whenConditionsFor_FindShipmentsById(final Shipments shipmentsFind) {
        when(shipmentsRepository.save(shipmentsFind)).thenReturn(shipmentsFind);
        when(shipmentsRepository.existsById(shipmentsFind.getId())).thenReturn(Boolean.TRUE);
        when(shipmentsRepository.getReferenceById(shipmentsFind.getId())).thenReturn(shipmentsFind);
    }

    private void whenConditionsFor_FindAllShipments() {
        when(shipmentsRepository.findAll()).thenReturn(List.of(shipmentNew()));
    }
}
