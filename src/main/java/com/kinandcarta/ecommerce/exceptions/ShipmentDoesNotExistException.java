package com.kinandcarta.ecommerce.exceptions;

public class ShipmentDoesNotExistException extends RuntimeException {
    public ShipmentDoesNotExistException(String message) {
        super(message);
    }
}
