package com.merchstock.app.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.merchstock.app.entity.Venta;
import com.merchstock.app.entity.VentaDetalle;
import com.merchstock.app.service.VentaPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

/**
 * Implementacion de generacion de boletas en PDF con OpenPDF.
 *
 * Estructura del documento:
 *  - Encabezado con datos de la empresa MerchStock Peru E.I.R.L.
 *  - Datos de la venta: codigo, fecha, cliente, vendedor, metodo de pago
 *  - Tabla de productos vendidos
 *  - Desglose de IGV (el precio ya incluye IGV, desglose inverso)
 *  - Pie de pagina con el total
 *
 * OpenPDF (fork libre de iText 4):
 * - Document / PdfWriter: documento base y su escritor de bytes
 * - PdfPTable / PdfPCell: tablas con columnas proporcionales
 * - Font / FontFactory: tipografias
 */
@Service
@Slf4j
public class VentaPdfServiceImpl implements VentaPdfService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Paleta institucional (misma familia navy/teal usada en el resto del sistema)
    private static final Color COLOR_NAVY = new Color(31, 56, 100);   // #1F3864
    private static final Color COLOR_TEAL = new Color(46, 139, 122);  // #2E8B7A
    private static final Color COLOR_GRIS_CLARO = new Color(233, 236, 239);

    private static final Font FONT_TITULO = new Font(Font.HELVETICA, 18, Font.BOLD, Color.WHITE);
    private static final Font FONT_SUBTITULO = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.WHITE);
    private static final Font FONT_SECCION = new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_NAVY);
    private static final Font FONT_LABEL = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.GRAY);
    private static final Font FONT_VALOR = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
    private static final Font FONT_TABLA_HEADER = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font FONT_TABLA_DATA = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static final Font FONT_TOTAL = new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE);

    @Override
    public byte[] generarBoletaPdf(Venta venta) throws IOException {
        log.info("Generando PDF de boleta para venta {}", venta.getCodigoVenta());

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarEncabezadoEmpresa(document);
            document.add(new Paragraph(" "));
            agregarDatosVenta(document, venta);
            document.add(new Paragraph(" "));
            agregarTablaProductos(document, venta);
            document.add(new Paragraph(" "));
            agregarResumenTotales(document, venta);

            document.close();
        } catch (DocumentException ex) {
            log.error("Error al generar el PDF de la venta {}: {}",
                    venta.getCodigoVenta(), ex.getMessage());
            throw new IOException("Error al generar el PDF de la boleta", ex);
        }

        log.info("PDF de boleta generado: {} ({} bytes)",
                venta.getCodigoVenta(), baos.size());
        return baos.toByteArray();
    }

    /**
     * Encabezado con los datos de la empresa, en una banda de color navy.
     */
    private void agregarEncabezadoEmpresa(Document document) throws DocumentException {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_NAVY);
        cell.setPadding(15);
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph titulo = new Paragraph("MERCHSTOCK PERU E.I.R.L.", FONT_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);

        Paragraph subtitulo1 = new Paragraph(
                "Av. Carlos Izaguirre 845, Los Olivos, Lima", FONT_SUBTITULO);
        subtitulo1.setAlignment(Element.ALIGN_CENTER);
        subtitulo1.setSpacingBefore(4);

        Paragraph subtitulo2 = new Paragraph(
                "Comprobante de Venta", FONT_SUBTITULO);
        subtitulo2.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(titulo);
        cell.addElement(subtitulo1);
        cell.addElement(subtitulo2);
        header.addCell(cell);

        document.add(header);
    }

    /**
     * Bloque con codigo de venta, fecha, cliente, vendedor y metodo de pago.
     */
    private void agregarDatosVenta(Document document, Venta venta) throws DocumentException {
        Paragraph codigo = new Paragraph(venta.getCodigoVenta(), FONT_SECCION);
        codigo.setAlignment(Element.ALIGN_CENTER);
        codigo.setSpacingAfter(10);
        document.add(codigo);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 1f});

        agregarCeldaDato(tabla, "Fecha de emision",
                venta.getFechaVenta() != null
                        ? venta.getFechaVenta().format(DATE_FORMATTER) : "-");

        agregarCeldaDato(tabla, "Metodo de pago",
                venta.getMetodoPago().toString());

        agregarCeldaDato(tabla, "Cliente",
                venta.getCliente() != null
                        ? venta.getCliente().getNombreCompleto()
                        : "Cliente generico (sin registro)");

        String documentoCliente = venta.getCliente() != null
                ? venta.getCliente().getTipoDocumento() + ": " + venta.getCliente().getNumeroDocumento()
                : "-";
        agregarCeldaDato(tabla, "Documento", documentoCliente);

        agregarCeldaDato(tabla, "Vendedor", venta.getUsuario().getNombreCompleto());
        agregarCeldaDato(tabla, "Usuario", venta.getUsuario().getUsername());

        document.add(tabla);
    }

    private void agregarCeldaDato(PdfPTable tabla, String label, String valor) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6);

        Paragraph pLabel = new Paragraph(label, FONT_LABEL);
        Paragraph pValor = new Paragraph(valor != null ? valor : "-", FONT_VALOR);
        pValor.setSpacingBefore(2);

        cell.addElement(pLabel);
        cell.addElement(pValor);
        tabla.addCell(cell);
    }

    /**
     * Tabla de productos vendidos: SKU, nombre, cantidad, precio unitario, subtotal.
     */
    private void agregarTablaProductos(Document document, Venta venta) throws DocumentException {
        Paragraph seccion = new Paragraph("Productos Vendidos", FONT_SECCION);
        seccion.setSpacingAfter(6);
        document.add(seccion);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.2f, 3f, 1f, 1.4f, 1.4f});

        String[] headers = {"SKU", "Producto", "Cant.", "P. Unit. (S/)", "Subtotal (S/)"};
        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(h, FONT_TABLA_HEADER));
            headerCell.setBackgroundColor(COLOR_NAVY);
            headerCell.setPadding(6);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(headerCell);
        }

        boolean filaClara = true;
        for (VentaDetalle detalle : venta.getDetalles()) {
            Color fondoFila = filaClara ? Color.WHITE : COLOR_GRIS_CLARO;

            agregarCeldaTabla(tabla, detalle.getProducto().getSku(), fondoFila, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, detalle.getProducto().getNombre(), fondoFila, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, String.valueOf(detalle.getCantidad()), fondoFila, Element.ALIGN_CENTER);
            agregarCeldaTabla(tabla, formatearMonto(detalle.getPrecioUnitario()), fondoFila, Element.ALIGN_RIGHT);
            agregarCeldaTabla(tabla, formatearMonto(detalle.getSubtotal()), fondoFila, Element.ALIGN_RIGHT);

            filaClara = !filaClara;
        }

        document.add(tabla);
    }

    private void agregarCeldaTabla(PdfPTable tabla, String texto, Color fondo, int alineacion) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FONT_TABLA_DATA));
        cell.setBackgroundColor(fondo);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alineacion);
        tabla.addCell(cell);
    }

    /**
     * Resumen final con Op. Gravada, IGV y Total.
     *
     * IMPORTANTE: el precio de los productos YA incluye IGV.
     * El desglose es inverso (por precision al centavo):
     *   Op. Gravada = Total / 1.18
     *   IGV         = Total - Op. Gravada
     *
     * En este metodo NO se recalcula nada: se muestran los valores
     * ya calculados y persistidos en la entidad Venta (venta.getSubtotal()
     * y venta.getIgv()), que fueron calculados con esa misma formula
     * al momento de registrar la venta.
     */
    private void agregarResumenTotales(Document document, Venta venta) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(55);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.setWidths(new float[]{1.3f, 1f});

        agregarFilaTotal(tabla, "Op. Gravada:", formatearMonto(venta.getSubtotal()), false);
        agregarFilaTotal(tabla, "IGV (18%):", formatearMonto(venta.getIgv()), false);

        PdfPCell labelTotal = new PdfPCell(new Phrase("TOTAL:", FONT_TOTAL));
        labelTotal.setBackgroundColor(COLOR_TEAL);
        labelTotal.setPadding(8);
        labelTotal.setBorder(Rectangle.NO_BORDER);
        labelTotal.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell valorTotal = new PdfPCell(
                new Phrase("S/ " + formatearMonto(venta.getTotal()), FONT_TOTAL));
        valorTotal.setBackgroundColor(COLOR_TEAL);
        valorTotal.setPadding(8);
        valorTotal.setBorder(Rectangle.NO_BORDER);
        valorTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tabla.addCell(labelTotal);
        tabla.addCell(valorTotal);

        document.add(tabla);
    }

    private void agregarFilaTotal(PdfPTable tabla, String label, String valor, boolean destacado) {
        Font fontLabel = destacado ? FONT_VALOR : FONT_LABEL;
        Font fontValor = FONT_VALOR;

        PdfPCell labelCell = new PdfPCell(new Phrase(label, fontLabel));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell valorCell = new PdfPCell(new Phrase("S/ " + valor, fontValor));
        valorCell.setBorder(Rectangle.NO_BORDER);
        valorCell.setPadding(4);
        valorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tabla.addCell(labelCell);
        tabla.addCell(valorCell);
    }

    /**
     * Formatea un BigDecimal a 2 decimales, redondeo HALF_UP
     * (consistente con el calculo de IGV usado en VentaServiceImpl).
     */
    private String formatearMonto(BigDecimal monto) {
        if (monto == null) {
            return "0.00";
        }
        return monto.setScale(2, RoundingMode.HALF_UP).toString();
    }
}