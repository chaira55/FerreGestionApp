package com.ferregestion.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String mensaje) {
        super(mensaje);
    }
}