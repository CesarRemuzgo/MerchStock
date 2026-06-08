package com.merchstock.app.service;

import com.merchstock.app.entity.Cliente;

import java.util.List;

/**
 * Servicio de Clientes - Interface (SOLID: Interface Segregation)
 */
public interface ClienteService {

    /**
     * Lista todos los clientes (activos e inactivos)
     */
    List<Cliente> listarTodos();

    /**
     * Lista solo clientes activos
     */
    List<Cliente> listarActivos();

    /**
     * Busca un cliente por ID
     */
    Cliente buscarPorId(Long id);

    /**
     * Busca un cliente por su numero de documento (DNI, RUC, etc.)
     */
    Cliente buscarPorDocumento(String numeroDocumento);

    /**
     * Busca clientes por nombre (LIKE)
     */
    List<Cliente> buscarPorNombre(String nombre);

    /**
     * Crea un nuevo cliente
     */
    Cliente crear(Cliente cliente);

    /**
     * Actualiza un cliente existente
     */
    Cliente actualizar(Long id, Cliente cliente);

    /**
     * Elimina logicamente un cliente
     */
    void eliminar(Long id);
}