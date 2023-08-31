package com.kinandcarta.ecommerce;

import java.util.Set;

public interface ServiceHandler {
    Shipments create(final Shipments model);
    Shipments update(final Long id, final Shipments model);
    void delete(final Long id);
    Shipments findById(final Long id);

    Set<Shipments> findAll();
}
