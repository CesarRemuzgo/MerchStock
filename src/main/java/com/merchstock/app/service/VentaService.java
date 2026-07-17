package com.merchstock.app.service;

import com.merchstock.app.dto.VentaForm;
import com.merchstock.app.entity.Venta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio de Ventas - Interface (SOLID: Interface Segregation)
 *
 * Las ventas son la operacion mas critica del sistema:
 * - Descuentan stock de manera atomica
 * - Generan registros de auditoria
 * - Requieren validacion multinivel
 *
 * Por eso este servicio aplica @Transactional en operaciones de escritura.
 */
public interface VentaService {

    /**
     * Lista todas las ventas (orden descendente por fecha)
     */
    List<Venta> listarTodas();

    /**
     * Lista las top 10 ventas mas recientes (para dashboard)
     */
    List<Venta> listarRecientes();

    /**
     * Lista ventas en un rango de fechas
     */
    List<Venta> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Busca una venta por ID (con sus detalles cargados)
     */
    Venta buscarPorId(Long id);

    /**
     * Busca una venta por su codigo unico (ej: VTA-2026-0001)
     */
    Venta buscarPorCodigo(String codigoVenta);

    /**
     * Registra una nueva venta a partir del formulario.
     *
     * Esta operacion es transaccional y atomica:
     *  - Si falla cualquier paso, se hace rollback completo
     *  - Se reduce stock de cada producto del carrito
     *  - Se genera codigo unico de venta
     *  - Se calcula subtotal, IGV (18%) y total
     *  - Se persiste cabecera + detalles + auditoria de stock
     *
     * @throws BusinessException si algun producto no tiene stock suficiente
     */
    Venta registrarVenta(VentaForm form);

    /**
     * Anula una venta existente.
     *
     * Esta operacion:
     *  - Cambia el estado a ANULADA
     *  - Revierte el stock de cada producto al estado anterior
     *  - Genera auditoria de stock por la reversa
     *  - Todo dentro de una transaccion
     */
    void anularVenta(Long id, String motivo);

    /**
     * Calcula el total de ventas (en dinero) entre 2 fechas
     */
    java.math.BigDecimal calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Cuenta el numero de ventas entre 2 fechas
     */
    long contarVentas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Totales de ventas (en dinero) de los ultimos 7 dias (incluyendo hoy),
     * uno por dia, con relleno en cero para dias sin ventas.
     * Usado para el grafico de linea del dashboard (Chart.js).
     *
     * Cada elemento del mapa tiene las claves "fecha" (String, dd/MM) y "total" (BigDecimal).
     */
    List<Map<String, Object>> obtenerVentasUltimos7Dias();

    /**
     * Top 5 productos mas vendidos (por cantidad), historico completo.
     * Usado para el grafico de barras del dashboard (Chart.js).
     *
     * Cada elemento del mapa tiene las claves "nombre" (String) y "cantidad" (Long).
     */
    List<Map<String, Object>> obtenerTop5ProductosMasVendidos();
}