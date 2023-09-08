package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.contracts.ServiceHandler;
import com.kinandcarta.ecommerce.entities.Shipments;
import com.kinandcarta.ecommerce.exceptions.MinimumShipmentFieldsNotProvidedException;
import com.kinandcarta.ecommerce.exceptions.ShipmentDoesNotExistException;
import com.kinandcarta.ecommerce.infrastructure.ShipmentsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class ShipmentsHandler implements ServiceHandler {

    private final ShipmentsRepository shipmentsRepository;

    public ShipmentsHandler(ShipmentsRepository shipmentsRepository) {
        this.shipmentsRepository = shipmentsRepository;
    }

    @Override
    @Transactional
    public Shipments create(final Shipments model) {
        try {
            if (!isShipmentValid(model)) {
                throw new MinimumShipmentFieldsNotProvidedException("Shipment elements required to create new shipment.");
            }
        } catch (final Exception e) {
            log.error("create exception occurred in handler", e);
        }

        return shipmentsRepository.save(model);
    }

    private static boolean isShipmentValid(Shipments model) {
        Set<ConstraintViolation<Shipments>> violations = new HashSet<>();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            violations = validator.validate(model);
        } catch (final Exception e) {
            log.error("Exception [validateShipmentsModel], ");
            violations.stream().filter(Objects::nonNull)
                    .forEach(issue -> log.error(issue.toString()));
            return Boolean.FALSE;
        }

        return violations.isEmpty();
    }

    @Override
    @Transactional
    public Shipments update(Long id, Shipments model) {
        if (model == null) throw new MinimumShipmentFieldsNotProvidedException("Shipments model not provided for update.");

        if (findById(id) == null) {
            throw new ShipmentDoesNotExistException("Update failure, shipment does not exist for id - " + id);
        }

        Shipments shipments = findById(id);
        shipments.setShipmentAccount(model.getShipmentAccount());
        shipments.setShipmentAddress(model.getShipmentAddress());
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
            throw new ShipmentDoesNotExistException("findById - not found by id - " + id);
        }
        return shipmentsRepository.getReferenceById(id);
    }

    @Override
    public Set<Shipments> findAll() {
        return new HashSet<>(shipmentsRepository.findAll());
    }

}
