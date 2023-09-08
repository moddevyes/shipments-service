package com.kinandcarta.ecommerce.exceptions;

public class MinimumShipmentFieldsNotProvidedException extends RuntimeException {
    public MinimumShipmentFieldsNotProvidedException(String message) {
        super(message);
    }
}
