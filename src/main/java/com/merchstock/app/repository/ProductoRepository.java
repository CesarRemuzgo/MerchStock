package com.merchstock.app.repository;

import com.merchstock.app.entity.Categoria;
import com.merchstock.app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio DAO para la entidad Producto
 * Incluye queries personalizadas para alertas de stock y busquedas
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca un producto por su SKU
     */
    Optional<Producto> findBySku(String sku);

    /**
     * Verifica si ya existe un producto con ese SKU
     */
    boolean existsBySku(String sku);

    /**
     * Lista solo productos activos
     */
    List<Producto> findByActivoTrueOrderByNombreAsc();

    /**
     * Lista productos por categoria
     */
    List<Producto> findByCategoriaAndActivoTrueOrderByNombreAsc(Categoria categoria);

    /**
     * Busca productos por nombre (LIKE case-insensitive)
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrueOrderByNombreAsc(String nombre);

    /**
     * Alertas de stock bajo - productos donde stock_actual <= stock_minimo
     * (RF15 - Generar alerta visual cuando stock < stock_minimo)
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo ORDER BY p.stockActual ASC")
    List<Producto> findProductosConStockBajo();

    /**
     * Cuenta productos con stock bajo (para el dashboard)
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    long countProductosConStockBajo();

    /**
     * Productos sin stock (stock_actual = 0)
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual = 0 ORDER BY p.nombre ASC")
    List<Producto> findProductosSinStock();

    /**
     * Actualiza solo el stock de un producto (operacion atomica)
     */
    @Query("UPDATE Producto p SET p.stockActual = :nuevoStock WHERE p.id = :idProducto")
    void actualizarStock(@Param("idProducto") Long idProducto, @Param("nuevoStock") Integer nuevoStock);
}