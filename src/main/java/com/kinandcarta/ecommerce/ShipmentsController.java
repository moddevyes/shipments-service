package com.kinandcarta.ecommerce;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
            log.error("::METHOD, create, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PutMapping(value = "/shipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shipments> update(@PathVariable("id") @NotNull final Long id, @RequestBody Shipments model) {
        try {
            return ResponseEntity.notFound().build();
        } catch (final Exception e) {
            log.error("::METHOD, update, exception occured.", e);
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
            log.error("::METHOD, create, exception occured.", e);
        }
    }

    @Override
    @GetMapping(value = "/shipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shipments> findById(@PathVariable("id") @NotNull final Long id) {
        try {
            return new ResponseEntity<>(shipmentsHandler.findById(id), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("::METHOD, findById, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping(value = "/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Shipments>> findAll() {
        try {
            return new ResponseEntity<>(shipmentsHandler.findAll(), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("::METHOD, findAll, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }
}
