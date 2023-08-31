package com.kinandcarta.ecommerce;

import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface CrudUseCase <T> {

    ResponseEntity<T> create(final T model);
    ResponseEntity<T> update(final Long id, final T model);
    void delete(final Long id);
    ResponseEntity<T> findById(final Long id);

    ResponseEntity<Set<T>> findAll();

}