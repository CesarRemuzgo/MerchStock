package com.merchstock.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para cada item del carrito de venta.
 *
 * Representa una linea individual del formulario de nueva venta:
 *  - producto seleccionado (por ID)
 *  - cantidad solicitada
 *  - precio unitario (se recupera del producto, no del form, por seguridad)
 *
 * NO es una entidad JPA. Solo transporta datos del form al service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVentaForm {

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    // El precio se completa en el service desde la BD (no se confia en el cliente)
    private BigDecimal precioUnitario;

    // Campos auxiliares para mostrar en el HTML (no se persisten)
    private String productoNombre;
    private String productoSku;
    private BigDecimal subtotal;
}