package com.merchstock.app.service;

import com.merchstock.app.entity.Categoria;

import java.util.List;

/**
 * Servicio de Categorias - Interface
 */
public interface CategoriaService {

    /**
     * Lista todas las categorias activas
     */
    List<Categoria> listarActivas();

    /**
     * Lista todas las categorias (incluye inactivas)
     */
    List<Categoria> listarTodas();

    /**
     * Busca una categoria por ID
     */
    Categoria buscarPorId(Long id);

    /**
     * Crea una nueva categoria
     */
    Categoria crear(Categoria categoria);

    /**
     * Actualiza una categoria existente
     */
    Categoria actualizar(Long id, Categoria categoria);

    /**
     * Elimina logicamente una categoria
     */
    void eliminar(Long id);
}