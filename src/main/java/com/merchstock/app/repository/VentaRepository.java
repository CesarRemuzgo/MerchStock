package com.merchstock.app.repository;

import com.merchstock.app.entity.Venta;
import com.merchstock.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio DAO para la entidad Venta
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Busca una venta por su codigo unico
     */
    Optional<Venta> findByCodigoVenta(String codigoVenta);

    /**
     * Lista las ventas en un rango de fechas
     */
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin ORDER BY v.fechaVenta DESC")
    List<Venta> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /**
     * Lista las ventas de un vendedor en un rango de fechas
     */
    @Query("SELECT v FROM Venta v WHERE v.usuario = :usuario AND v.fechaVenta BETWEEN :inicio AND :fin ORDER BY v.fechaVenta DESC")
    List<Venta> findByUsuarioAndRangoFechas(@Param("usuario") Usuario usuario, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /**
     * Suma total de ventas en un rango (para dashboard)
     */
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin AND v.estado = 'COMPLETADA'")
    BigDecimal sumTotalVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /**
     * Cuenta ventas en un rango de fechas
     */
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin AND v.estado = 'COMPLETADA'")
    long countVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /**
     * Top 10 ventas mas recientes
     */
    List<Venta> findTop10ByOrderByFechaVentaDesc();
}