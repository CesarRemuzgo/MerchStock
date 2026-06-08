package com.merchstock.app.dto;

import com.merchstock.app.entity.Venta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para el formulario completo de nueva venta.
 *
 * Contiene:
 *  - cliente (opcional - se permite venta sin cliente registrado)
 *  - vendedor (usuario que registra la venta)
 *  - lista de items (productos a vender con cantidades)
 *  - metodo de pago
 *  - observaciones opcionales
 *
 * El service lo transforma en una entidad Venta + sus VentaDetalle.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaForm {

    private Long clienteId; // Opcional - puede ser null para "Cliente generico"

    @NotNull(message = "El vendedor es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El metodo de pago es obligatorio")
    private Venta.MetodoPago metodoPago;

    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres")
    private String observaciones;

    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    @Builder.Default
    private List<ItemVentaForm> items = new ArrayList<>();
}