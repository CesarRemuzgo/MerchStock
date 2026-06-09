package com.merchstock.app.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

/**
 * Servicio de Reportes - Interface (SOLID: Interface Segregation)
 *
 * Genera reportes en formato Excel (.xlsx) usando Apache POI.
 * Los reportes se devuelven como Workbook para que el Controller
 * los stream-ee como descarga al navegador.
 */
public interface ReporteService {

    /**
     * Genera reporte completo de productos activos.
     * Incluye: SKU, nombre, categoria, precios, stock, estado.
     */
    Workbook generarReporteProductos() throws IOException;

    /**
     * Genera reporte de productos con stock bajo (RF15).
     * Incluye: productos donde stock_actual <= stock_minimo.
     */
    Workbook generarReporteStockBajo() throws IOException;

    /**
     * Genera reporte de todas las ventas (RF19-20).
     * Incluye: codigo, fecha, cliente, vendedor, subtotal, IGV, total, estado.
     */
    Workbook generarReporteVentas() throws IOException;
}