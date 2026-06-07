package com.merchstock.app.service.impl;

import com.google.common.base.Preconditions;
import com.merchstock.app.entity.Categoria;
import com.merchstock.app.entity.Producto;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.repository.ProductoRepository;
import com.merchstock.app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del servicio de Productos
 *
 * Aplica:
 * - @Service: Spring lo registra como bean
 * - @Transactional: garantiza atomicidad en operaciones de BD
 * - @RequiredArgsConstructor: Lombok inyecta dependencias via constructor (mejor practica)
 * - @Slf4j: Lombok crea un logger automatico (usa Logback por debajo)
 *
 * Librerias APF3 utilizadas:
 * - Google Guava: Preconditions para validaciones defensivas
 * - Apache Commons Lang: StringUtils para manejo de strings
 * - SLF4J + Logback: logging estructurado
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)  // Por defecto solo lectura, sobreescribimos donde se necesita escribir
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        log.debug("Listando todos los productos activos");
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public Producto buscarPorId(Long id) {
        log.debug("Buscando producto por ID: {}", id);
        // Preconditions de Google Guava - validacion defensiva
        Preconditions.checkNotNull(id, "El ID del producto no puede ser nulo");

        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }

    @Override
    public Producto buscarPorSku(String sku) {
        log.debug("Buscando producto por SKU: {}", sku);
        Preconditions.checkArgument(StringUtils.isNotBlank(sku), "El SKU no puede estar vacio");

        return productoRepository.findBySku(sku.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "sku", sku));
    }

    @Override
    public List<Producto> listarPorCategoria(Categoria categoria) {
        Preconditions.checkNotNull(categoria, "La categoria no puede ser nula");
        return productoRepository.findByCategoriaAndActivoTrueOrderByNombreAsc(categoria);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        if (StringUtils.isBlank(nombre)) {
            return listarTodos();
        }
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByNombreAsc(nombre.trim());
    }

    @Override
    @Transactional
    public Producto crear(Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombre());
        Preconditions.checkNotNull(producto, "El producto no puede ser nulo");
        Preconditions.checkArgument(StringUtils.isNotBlank(producto.getSku()), "El SKU es obligatorio");

        // Normalizar SKU a mayusculas
        producto.setSku(producto.getSku().trim().toUpperCase());

        // Validar que el SKU no exista
        if (productoRepository.existsBySku(producto.getSku())) {
            throw new BusinessException("Ya existe un producto con SKU: " + producto.getSku());
        }

        // Validar que precio venta >= precio compra
        if (producto.getPrecioVenta().compareTo(producto.getPrecioCompra()) < 0) {
            log.warn("Precio venta menor a precio compra para SKU {}", producto.getSku());
        }

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Producto actualizar(Long id, Producto productoActualizado) {
        log.info("Actualizando producto ID: {}", id);
        Producto existente = buscarPorId(id);

        existente.setNombre(productoActualizado.getNombre());
        existente.setDescripcion(productoActualizado.getDescripcion());
        existente.setCategoria(productoActualizado.getCategoria());
        existente.setPrecioCompra(productoActualizado.getPrecioCompra());
        existente.setPrecioVenta(productoActualizado.getPrecioVenta());
        existente.setStockMinimo(productoActualizado.getStockMinimo());
        existente.setUnidadMedida(productoActualizado.getUnidadMedida());
        existente.setImagenUrl(productoActualizado.getImagenUrl());

        return productoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando logicamente producto ID: {}", id);
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> obtenerProductosConStockBajo() {
        log.debug("Consultando productos con stock bajo (RF15)");
        return productoRepository.findProductosConStockBajo();
    }

    @Override
    public long contarProductosConStockBajo() {
        return productoRepository.countProductosConStockBajo();
    }

    @Override
    public List<Producto> obtenerProductosSinStock() {
        return productoRepository.findProductosSinStock();
    }

    /**
     * Reduce el stock de un producto al realizar una venta.
     *
     * DEFENSA EN CAPA SERVICE: valida que el stock no quede negativo ANTES
     * de hacer el UPDATE. Esto complementa el CHECK constraint de SQL.
     */
    @Override
    @Transactional
    public void reducirStock(Long idProducto, Integer cantidad) {
        log.info("Reduciendo stock del producto {} en {} unidades", idProducto, cantidad);
        Preconditions.checkArgument(cantidad != null && cantidad > 0,
                "La cantidad a reducir debe ser mayor a cero");

        Producto producto = buscarPorId(idProducto);

        if (producto.getStockActual() < cantidad) {
            throw new BusinessException(String.format(
                    "Stock insuficiente para %s. Disponible: %d, solicitado: %d",
                    producto.getNombre(), producto.getStockActual(), cantidad));
        }

        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);
        log.info("Stock reducido. Nuevo stock del producto {}: {}",
                producto.getNombre(), producto.getStockActual());
    }

    @Override
    @Transactional
    public void aumentarStock(Long idProducto, Integer cantidad) {
        log.info("Aumentando stock del producto {} en {} unidades", idProducto, cantidad);
        Preconditions.checkArgument(cantidad != null && cantidad > 0,
                "La cantidad a aumentar debe ser mayor a cero");

        Producto producto = buscarPorId(idProducto);
        producto.setStockActual(producto.getStockActual() + cantidad);
        productoRepository.save(producto);
    }
}