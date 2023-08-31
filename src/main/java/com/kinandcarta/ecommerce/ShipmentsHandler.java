package com.kinandcarta.ecommerce;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShipmentsHandler implements ServiceHandler {
    private final ShipmentsRepository shipmentsRepository;

    public ShipmentsHandler(ShipmentsRepository shipmentsRepository) {
        this.shipmentsRepository = shipmentsRepository;
    }

    @Override
    public Shipments create(Shipments model) {
        return shipmentsRepository.save(model);
    }

    @Override
    public Shipments update(Long id, Shipments model) {
        Shipments shipments = findById(id);
        shipments.setAccountId(model.getAccountId());
        shipments.setShippingAddressId(model.getShippingAddressId());
        shipments.setOrderLineItems(model.getOrderLineItems());
        shipments.setShippedDate(model.getShippedDate());
        shipments.setDeliveryDate(model.getDeliveryDate());
        return shipmentsRepository.save(shipments);
    }

    @Override
    public void delete(Long id) {
        shipmentsRepository.deleteById(id);
    }

    @Override
    public Shipments findById(Long id) {
        if (!shipmentsRepository.existsById(id)) {
            throw new EntityNotFoundException("findById - not found by id - " + id);
        }
        return shipmentsRepository.getReferenceById(id);
    }

    @Override
    public Set<Shipments> findAll() {
        return new HashSet<>(shipmentsRepository.findAll());
    }
}
