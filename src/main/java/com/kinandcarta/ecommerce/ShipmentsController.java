package com.kinandcarta.ecommerce;

import com.kinandcarta.ecommerce.contracts.CrudUseCase;
import com.kinandcarta.ecommerce.entities.Shipments;
import com.kinandcarta.ecommerce.exceptions.MinimumShipmentFieldsNotProvidedException;
import com.kinandcarta.ecommerce.exceptions.ShipmentDoesNotExistException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
public class ShipmentsController implements CrudUseCase<Shipments> {
    private final ShipmentsHandler shipmentsHandler;

    public ShipmentsController(ShipmentsHandler shipmentsHandler) {
        this.shipmentsHandler = shipmentsHandler;
    }


    @Override
    @PostMapping(value = "/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shipments> create(@RequestBody Shipments model) {
        try {
            return new ResponseEntity<>(shipmentsHandler.create(model), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof MinimumShipmentFieldsNotProvidedException ||
                e instanceof DuplicateKeyException) {
                log.error("Failed to create new Shipment {}", e.getMessage());
                return ResponseEntity.badRequest().build();
            }
            log.error("::METHOD, create, exception occurred.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PutMapping(value = "/shipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shipments> update(@PathVariable("id") @NotNull final Long id, @RequestBody Shipments model) {
        try {
            return new ResponseEntity<>(shipmentsHandler.update(id, model), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ShipmentDoesNotExistException) {
                log.error("Shipment does not exists Exception, will not update.");
                return ResponseEntity.badRequest().build();
            }
            log.error("::METHOD, update, exception occurred.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/shipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") @NotNull final Long id) {
        try {
            shipmentsHandler.delete(id);
        } catch (final Exception e) {
            if (e instanceof ShipmentDoesNotExistException) {
                log.error("Delete failure, shipment does not exist for id - "+id);
            }
            log.error("::METHOD, delete, exception occurred.", e);
        }
    }

    @Override
    @GetMapping(value = "/shipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shipments> findById(@PathVariable("id") @NotNull final Long id) {
        try {
            return new ResponseEntity<>(shipmentsHandler.findById(id), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("::METHOD, findById, exception occurred.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping(value = "/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Shipments>> findAll() {
        try {
            return new ResponseEntity<>(shipmentsHandler.findAll(), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("::METHOD, findAll, exception occurred.", e);
            return ResponseEntity.notFound().build();
        }
    }
}
