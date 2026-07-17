package com.merchstock.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merchstock.app.service.ProductoService;
import com.merchstock.app.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Controlador para la pagina principal del sistema (dashboard).
 *
 * Carga estadisticas en tiempo real para el panel de inicio:
 * - Total de productos activos
 * - Cantidad de alertas de stock bajo (RF15)
 * - Numero de ventas del dia de hoy
 * - Ingresos totales del dia de hoy
 * - Ultimas ventas registradas
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final ProductoService productoService;
    private final VentaService ventaService;
    private final ObjectMapper objectMapper;

    /**
     * Pagina de inicio con estadisticas del negocio.
     * URL: GET /
     */
    @GetMapping("/")
    public String home(Model model) {
        // Rango del dia de hoy: desde las 00:00 hasta las 23:59:59
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(LocalTime.MAX);

        // Estadisticas de productos
        long totalProductos = productoService.listarTodos().size();
        long totalAlertas = productoService.contarProductosConStockBajo();

        // Estadisticas de ventas del dia
        long ventasHoy = ventaService.contarVentas(inicioDia, finDia);
        BigDecimal ingresosHoy = ventaService.calcularTotalVentas(inicioDia, finDia);
        if (ingresosHoy == null) {
            ingresosHoy = BigDecimal.ZERO; // si no hay ventas hoy, el SUM devuelve null
        }

       model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalAlertas", totalAlertas);
        model.addAttribute("ventasHoy", ventasHoy);
        model.addAttribute("ingresosHoy", ingresosHoy);
        model.addAttribute("ventasRecientes", ventaService.listarRecientes());

        // Datos para los graficos del dashboard (Chart.js)
        model.addAttribute("ventasUltimos7DiasJson",
                convertirAJson(ventaService.obtenerVentasUltimos7Dias()));
        model.addAttribute("topProductosJson",
                convertirAJson(ventaService.obtenerTop5ProductosMasVendidos()));

        return "home";
    }

    /**
     * Convierte una lista de mapas a JSON para pasarla directamente
     * al script de Chart.js en la vista, evitando construir el JSON
     * manualmente con Thymeleaf.
     */
    private String convertirAJson(Object datos) {
        try {
            return objectMapper.writeValueAsString(datos);
        } catch (Exception ex) {
            log.error("Error al convertir datos del dashboard a JSON: {}", ex.getMessage());
            return "[]";
        }
    }
}