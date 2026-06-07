package com.merchstock.app.repository;

import com.merchstock.app.entity.AuditoriaStock;
import com.merchstock.app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio DAO para la entidad AuditoriaStock
 */
@Repository
public interface AuditoriaStockRepository extends JpaRepository<AuditoriaStock, Long> {

    /**
     * Historial de movimientos de un producto especifico
     */
    List<AuditoriaStock> findByProductoOrderByFechaMovimientoDesc(Producto producto);

    /**
     * Ultimos 50 movimientos del sistema
     */
    List<AuditoriaStock> findTop50ByOrderByFechaMovimientoDesc();

    /**
     * Movimientos por tipo (ENTRADA, SALIDA, AJUSTE)
     */
    List<AuditoriaStock> findByTipoMovimientoOrderByFechaMovimientoDesc(AuditoriaStock.TipoMovimiento tipo);
}