package com.merchstock.app.exception;

/**
 * Excepcion lanzada cuando un recurso no es encontrado en la BD
 * (ej. buscar un producto por ID que no existe)
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ResourceNotFoundException(String entidad, String campo, Object valor) {
        super(String.format("%s no encontrado con %s: '%s'", entidad, campo, valor));
    }
}