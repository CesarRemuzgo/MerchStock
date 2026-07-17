package com.merchstock.app.exception;

/**
 * Excepcion lanzada cuando se viola una regla de negocio
 * (ej. intentar registrar venta con stock insuficiente)
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }

    public BusinessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}