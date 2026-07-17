package com.merchstock.app.service;

import com.merchstock.app.entity.Venta;

import java.io.IOException;

/**
 * Servicio de generacion de boletas en PDF (SOLID: Interface Segregation).
 *
 * Genera un comprobante de venta en formato PDF usando OpenPDF,
 * incluyendo los datos de la empresa, el cliente, el vendedor,
 * el detalle de productos y el desglose de IGV.
 */
public interface VentaPdfService {

    /**
     * Genera el PDF de la boleta/comprobante para una venta ya registrada.
     *
     * @param venta la venta con sus detalles cargados
     * @return el PDF generado como arreglo de bytes
     */
    byte[] generarBoletaPdf(Venta venta) throws IOException;
}