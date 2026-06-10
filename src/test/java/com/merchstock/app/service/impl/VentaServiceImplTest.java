package com.merchstock.app.service.impl;

import com.merchstock.app.dto.ItemVentaForm;
import com.merchstock.app.dto.VentaForm;
import com.merchstock.app.entity.Producto;
import com.merchstock.app.entity.Usuario;
import com.merchstock.app.entity.Venta;
import com.merchstock.app.repository.AuditoriaStockRepository;
import com.merchstock.app.repository.ClienteRepository;
import com.merchstock.app.repository.ProductoRepository;
import com.merchstock.app.repository.UsuarioRepository;
import com.merchstock.app.repository.VentaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del nucleo transaccional: VentaServiceImpl.
 *
 * Verifica el calculo correcto del IGV peruano (18%), del total y la
 * generacion del codigo de venta con formato VTA-YYYY-NNNN, ademas del
 * descuento de stock. Todos los repositorios son mocks (sin BD real).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de VentaServiceImpl")
class VentaServiceImplTest {

    @Mock private VentaRepository ventaRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private AuditoriaStockRepository auditoriaStockRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    @DisplayName("registrarVenta calcula IGV 18%, total y genera codigo VTA-YYYY-0001")
    void registrarVenta_calculaIgvYTotalYGeneraCodigo() {
        // --- Arrange ---
        Usuario vendedor = Usuario.builder()
                .id(1L).username("pventa1").activo(true)
                .build();

        Producto producto = Producto.builder()
                .id(10L).nombre("Taza Ceramica").sku("TAZ-001")
                .activo(true).stockActual(50)
                .precioVenta(new BigDecimal("50.00"))
                .build();

        ItemVentaForm item = new ItemVentaForm();
        item.setProductoId(10L);
        item.setCantidad(2);

        VentaForm form = new VentaForm();
        form.setUsuarioId(1L);
        form.setItems(List.of(item));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(ventaRepository.count()).thenReturn(0L);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        // --- Act ---
        Venta venta = ventaService.registrarVenta(form);

        // --- Assert: calculo monetario (50.00 x 2 = 100.00 + 18% IGV) ---
        assertThat(venta.getSubtotal()).isEqualByComparingTo("100.00");
        assertThat(venta.getIgv()).isEqualByComparingTo("18.00");
        assertThat(venta.getTotal()).isEqualByComparingTo("118.00");

        // --- Assert: codigo y estado ---
        assertThat(venta.getCodigoVenta()).matches("VTA-\\d{4}-0001");
        assertThat(venta.getEstado()).isEqualTo(Venta.EstadoVenta.COMPLETADA);

        // --- Assert: stock descontado (50 - 2 = 48) ---
        assertThat(producto.getStockActual()).isEqualTo(48);
    }
}