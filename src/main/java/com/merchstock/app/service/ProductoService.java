package com.merchstock.app.service;

import com.merchstock.app.entity.Categoria;
import com.merchstock.app.entity.Producto;

import java.util.List;

/**
 * Servicio de Productos - Interface (aplica SOLID: Interface Segregation Principle)
 *
 * Define el contrato que cualquier implementacion de ProductoService debe cumplir.
 * Esto permite cambiar la implementacion (ej. para tests) sin afectar a los controllers.
 */
public interface ProductoService {

    /**
     * Lista todos los productos activos
     */
    List<Producto> listarTodos();

    /**
     * Busca un producto por ID. Lanza ResourceNotFoundException si no existe.
     */
    Producto buscarPorId(Long id);

    /**
     * Busca un producto por SKU
     */
    Producto buscarPorSku(String sku);

    /**
     * Lista productos por categoria
     */
    List<Producto> listarPorCategoria(Categoria categoria);

    /**
     * Busca productos cuyo nombre contenga el texto dado
     */
    List<Producto> buscarPorNombre(String nombre);

    /**
     * Crea un nuevo producto. Lanza BusinessException si el SKU ya existe.
     */
    Producto crear(Producto producto);

    /**
     * Actualiza un producto existente
     */
    Producto actualizar(Long id, Producto producto);

    /**
     * Elimina logicamente un producto (activo = false)
     */
    void eliminar(Long id);

    /**
     * Lista productos con stock bajo (stock_actual <= stock_minimo)
     * RF15 - Alerta de stock bajo
     */
    List<Producto> obtenerProductosConStockBajo();

    /**
     * Cuenta productos con stock bajo (para dashboard)
     */
    long contarProductosConStockBajo();

    /**
     * Lista productos sin stock (stock_actual = 0)
     */
    List<Producto> obtenerProductosSinStock();

    /**
     * Reduce el stock de un producto (al vender)
     * Valida que el stock no quede negativo (defensa en capa Service)
     *
     * @throws BusinessException si la cantidad excede el stock actual
     */
    void reducirStock(Long idProducto, Integer cantidad);

    /**
     * Aumenta el stock de un producto (al recibir mercaderia)
     */
    void aumentarStock(Long idProducto, Integer cantidad);
}