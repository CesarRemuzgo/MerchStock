package com.merchstock.app.service.impl;

import com.merchstock.app.entity.Producto;
import com.merchstock.app.entity.Venta;
import com.merchstock.app.repository.ProductoRepository;
import com.merchstock.app.repository.VentaRepository;
import com.merchstock.app.service.ReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementacion de generacion de reportes Excel con Apache POI.
 *
 * Cada reporte genera un Workbook (.xlsx) con:
 *  - Titulo grande (fila 0, merged cells)
 *  - Fila de metadata (fila 1: fecha generacion, total registros)
 *  - Headers con estilo navy + texto blanco (fila 3)
 *  - Filas de datos con estilos alternados
 *  - Columnas auto-ajustadas al contenido
 *
 * Apache POI:
 * - XSSFWorkbook: archivos .xlsx (Excel 2007+)
 * - CellStyle: formato de celdas (colores, bordes, fonts)
 * - CellRangeAddress: para combinar celdas (merge)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    // ============================================================
    // REPORTE 1: PRODUCTOS
    // ============================================================
    @Override
    public Workbook generarReporteProductos() throws IOException {
        log.info("Generando reporte Excel de productos");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        List<Producto> productos = productoRepository.findByActivoTrueOrderByNombreAsc();

        // Estilos
        CellStyle titleStyle = crearEstiloTitulo(workbook);
        CellStyle headerStyle = crearEstiloHeader(workbook);
        CellStyle dataStyle = crearEstiloData(workbook);
        CellStyle numberStyle = crearEstiloNumerico(workbook);

        // Fila 0: Titulo grande
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE PRODUCTOS - MERCHSTOCK");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        titleRow.setHeightInPoints(28);

        // Fila 1: Metadata
        Row metaRow = sheet.createRow(1);
        metaRow.createCell(0).setCellValue("Generado: "
                + java.time.LocalDateTime.now().format(DATE_FORMATTER));
        metaRow.createCell(4).setCellValue("Total productos: " + productos.size());

        // Fila 3: Headers
        Row headerRow = sheet.createRow(3);
        String[] headers = {"SKU", "Nombre", "Categoria", "P. Compra (S/)",
                "P. Venta (S/)", "Stock Actual", "Stock Minimo"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Filas de datos (desde fila 4)
        int rowIdx = 4;
        for (Producto p : productos) {
            Row row = sheet.createRow(rowIdx++);

            Cell c0 = row.createCell(0); c0.setCellValue(p.getSku()); c0.setCellStyle(dataStyle);
            Cell c1 = row.createCell(1); c1.setCellValue(p.getNombre()); c1.setCellStyle(dataStyle);
            Cell c2 = row.createCell(2);
            c2.setCellValue(p.getCategoria() != null ? p.getCategoria().getNombre() : "-");
            c2.setCellStyle(dataStyle);
            Cell c3 = row.createCell(3); c3.setCellValue(p.getPrecioCompra().doubleValue()); c3.setCellStyle(numberStyle);
            Cell c4 = row.createCell(4); c4.setCellValue(p.getPrecioVenta().doubleValue()); c4.setCellStyle(numberStyle);
            Cell c5 = row.createCell(5); c5.setCellValue(p.getStockActual()); c5.setCellStyle(dataStyle);
            Cell c6 = row.createCell(6); c6.setCellValue(p.getStockMinimo()); c6.setCellStyle(dataStyle);
        }

        // Auto-ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        log.info("Reporte de productos generado: {} registros", productos.size());
        return workbook;
    }

    // ============================================================
    // REPORTE 2: STOCK BAJO
    // ============================================================
    @Override
    public Workbook generarReporteStockBajo() throws IOException {
        log.info("Generando reporte Excel de stock bajo (RF15)");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Bajo");

        List<Producto> productos = productoRepository.findProductosConStockBajo();

        CellStyle titleStyle = crearEstiloTitulo(workbook);
        CellStyle headerStyle = crearEstiloHeader(workbook);
        CellStyle dataStyle = crearEstiloData(workbook);
        CellStyle alertStyle = crearEstiloAlerta(workbook);

        // Titulo
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("ALERTAS DE STOCK BAJO - MERCHSTOCK");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        titleRow.setHeightInPoints(28);

        // Metadata
        Row metaRow = sheet.createRow(1);
        metaRow.createCell(0).setCellValue("Generado: "
                + java.time.LocalDateTime.now().format(DATE_FORMATTER));
        metaRow.createCell(3).setCellValue("Productos en alerta: " + productos.size());

        // Headers
        Row headerRow = sheet.createRow(3);
        String[] headers = {"SKU", "Nombre", "Categoria", "Stock Actual",
                "Stock Minimo", "Faltante"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 4;
        for (Producto p : productos) {
            Row row = sheet.createRow(rowIdx++);
            int faltante = p.getStockMinimo() - p.getStockActual();

            Cell c0 = row.createCell(0); c0.setCellValue(p.getSku()); c0.setCellStyle(dataStyle);
            Cell c1 = row.createCell(1); c1.setCellValue(p.getNombre()); c1.setCellStyle(dataStyle);
            Cell c2 = row.createCell(2);
            c2.setCellValue(p.getCategoria() != null ? p.getCategoria().getNombre() : "-");
            c2.setCellStyle(dataStyle);
            Cell c3 = row.createCell(3); c3.setCellValue(p.getStockActual()); c3.setCellStyle(alertStyle);
            Cell c4 = row.createCell(4); c4.setCellValue(p.getStockMinimo()); c4.setCellStyle(dataStyle);
            Cell c5 = row.createCell(5); c5.setCellValue(faltante); c5.setCellStyle(alertStyle);
        }

        if (productos.isEmpty()) {
            Row emptyRow = sheet.createRow(4);
            Cell emptyCell = emptyRow.createCell(0);
            emptyCell.setCellValue("No hay productos con stock bajo. Inventario en orden.");
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 5));
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        log.info("Reporte de stock bajo generado: {} productos en alerta", productos.size());
        return workbook;
    }

    // ============================================================
    // REPORTE 3: VENTAS
    // ============================================================
    @Override
    public Workbook generarReporteVentas() throws IOException {
        log.info("Generando reporte Excel de ventas");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");

        List<Venta> ventas = ventaRepository.findAll();

        CellStyle titleStyle = crearEstiloTitulo(workbook);
        CellStyle headerStyle = crearEstiloHeader(workbook);
        CellStyle dataStyle = crearEstiloData(workbook);
        CellStyle numberStyle = crearEstiloNumerico(workbook);

        // Titulo
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE VENTAS - MERCHSTOCK");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        titleRow.setHeightInPoints(28);

        // Calcular total
        double totalGeneral = ventas.stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.COMPLETADA)
                .mapToDouble(v -> v.getTotal().doubleValue())
                .sum();

        // Metadata
        Row metaRow = sheet.createRow(1);
        metaRow.createCell(0).setCellValue("Generado: "
                + java.time.LocalDateTime.now().format(DATE_FORMATTER));
        metaRow.createCell(3).setCellValue("Total ventas: " + ventas.size());
        metaRow.createCell(6).setCellValue("Total vendido: S/ "
                + String.format("%.2f", totalGeneral));

        // Headers
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Codigo", "Fecha", "Cliente", "Vendedor", "Metodo Pago",
                "Subtotal (S/)", "IGV (S/)", "Total (S/)", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 4;
        for (Venta v : ventas) {
            Row row = sheet.createRow(rowIdx++);

            Cell c0 = row.createCell(0); c0.setCellValue(v.getCodigoVenta()); c0.setCellStyle(dataStyle);
            Cell c1 = row.createCell(1);
            c1.setCellValue(v.getFechaVenta() != null
                    ? v.getFechaVenta().format(DATE_FORMATTER) : "-");
            c1.setCellStyle(dataStyle);
            Cell c2 = row.createCell(2);
            c2.setCellValue(v.getCliente() != null
                    ? v.getCliente().getNombreCompleto() : "Cliente generico");
            c2.setCellStyle(dataStyle);
            Cell c3 = row.createCell(3); c3.setCellValue(v.getUsuario().getUsername()); c3.setCellStyle(dataStyle);
            Cell c4 = row.createCell(4); c4.setCellValue(v.getMetodoPago().toString()); c4.setCellStyle(dataStyle);
            Cell c5 = row.createCell(5); c5.setCellValue(v.getSubtotal().doubleValue()); c5.setCellStyle(numberStyle);
            Cell c6 = row.createCell(6); c6.setCellValue(v.getIgv().doubleValue()); c6.setCellStyle(numberStyle);
            Cell c7 = row.createCell(7); c7.setCellValue(v.getTotal().doubleValue()); c7.setCellStyle(numberStyle);
            Cell c8 = row.createCell(8); c8.setCellValue(v.getEstado().toString()); c8.setCellStyle(dataStyle);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        log.info("Reporte de ventas generado: {} registros", ventas.size());
        return workbook;
    }

    // ============================================================
    // ESTILOS REUTILIZABLES
    // ============================================================

    private CellStyle crearEstiloTitulo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle crearEstiloHeader(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloData(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloNumerico(Workbook workbook) {
        CellStyle style = crearEstiloData(workbook);
        style.setAlignment(HorizontalAlignment.RIGHT);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    private CellStyle crearEstiloAlerta(Workbook workbook) {
        CellStyle style = crearEstiloData(workbook);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}