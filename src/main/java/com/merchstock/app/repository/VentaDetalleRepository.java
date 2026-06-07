package com.merchstock.app.repository;

import com.merchstock.app.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio DAO para la entidad VentaDetalle
 */
@Repository
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {

    /**
     * Lista los detalles de una venta especifica
     */
    List<VentaDetalle> findByVentaId(Long ventaId);

    /**
     * Productos mas vendidos en un rango de fechas
     * (RF19 - Reporte de productos mas vendidos)
     */
    @Query("SELECT vd.producto.id, vd.producto.nombre, SUM(vd.cantidad) AS totalVendido " +
           "FROM VentaDetalle vd " +
           "WHERE vd.venta.fechaVenta BETWEEN :inicio AND :fin " +
           "AND vd.venta.estado = 'COMPLETADA' " +
           "GROUP BY vd.producto.id, vd.producto.nombre " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}