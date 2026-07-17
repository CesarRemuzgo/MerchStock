package com.merchstock.app.controller;

import com.merchstock.app.service.ReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controlador para la generacion y descarga de reportes Excel (Apache POI).
 *
 * Endpoints:
 *  - GET /reportes           - Pagina con botones de descarga
 *  - GET /reportes/productos - Descarga reporte de productos
 *  - GET /reportes/stock-bajo - Descarga reporte de stock bajo (RF15)
 *  - GET /reportes/ventas    - Descarga reporte de ventas (RF19-20)
 *
 * Cada endpoint de descarga retorna el archivo .xlsx como stream binario
 * con headers Content-Disposition: attachment para forzar la descarga
 * en el navegador.
 */
@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ReporteService reporteService;

    /**
     * Pagina principal de reportes con los botones de descarga
     */
    @GetMapping
    public String mostrarReportes() {
        return "reportes/index";
    }

    /**
     * Descarga el reporte de productos
     */
    @GetMapping("/productos")
    public ResponseEntity<byte[]> descargarReporteProductos() throws IOException {
        log.info("Descargando reporte de productos");

        Workbook workbook = reporteService.generarReporteProductos();
        byte[] bytes = workbookToBytes(workbook);

        String nombreArchivo = "MerchStock_Productos_"
                + LocalDate.now().format(DATE_FORMATTER) + ".xlsx";

        return buildExcelResponse(bytes, nombreArchivo);
    }

    /**
     * Descarga el reporte de stock bajo (RF15)
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<byte[]> descargarReporteStockBajo() throws IOException {
        log.info("Descargando reporte de stock bajo");

        Workbook workbook = reporteService.generarReporteStockBajo();
        byte[] bytes = workbookToBytes(workbook);

        String nombreArchivo = "MerchStock_StockBajo_"
                + LocalDate.now().format(DATE_FORMATTER) + ".xlsx";

        return buildExcelResponse(bytes, nombreArchivo);
    }

    /**
     * Descarga el reporte de ventas (RF19-20)
     */
    @GetMapping("/ventas")
    public ResponseEntity<byte[]> descargarReporteVentas() throws IOException {
        log.info("Descargando reporte de ventas");

        Workbook workbook = reporteService.generarReporteVentas();
        byte[] bytes = workbookToBytes(workbook);

        String nombreArchivo = "MerchStock_Ventas_"
                + LocalDate.now().format(DATE_FORMATTER) + ".xlsx";

        return buildExcelResponse(bytes, nombreArchivo);
    }

    // ============================================================
    // METODOS AUXILIARES
    // ============================================================

    /**
     * Convierte el Workbook a un array de bytes para enviarlo por HTTP
     */
    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }

    /**
     * Construye la respuesta HTTP con los headers correctos para
     * forzar la descarga del archivo Excel en el navegador
     */
    private ResponseEntity<byte[]> buildExcelResponse(byte[] bytes, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", nombreArchivo);
        headers.setContentLength(bytes.length);

        log.info("Enviando archivo: {} ({} bytes)", nombreArchivo, bytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}