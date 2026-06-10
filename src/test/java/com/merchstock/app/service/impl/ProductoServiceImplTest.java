package com.merchstock.app.service.impl;

import com.merchstock.app.entity.Producto;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios de la logica de negocio de ProductoServiceImpl.
 *
 * Se usa Mockito para aislar el service de la base de datos: el repositorio
 * es un mock, por lo que se prueba SOLO la logica de negocio (validaciones,
 * defensa de stock, normalizacion de SKU) sin levantar Spring ni MySQL.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de ProductoServiceImpl")
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    @DisplayName("reducirStock con stock suficiente descuenta la cantidad correctamente")
    void reducirStock_conStockSuficiente_descuentaCorrectamente() {
        Producto producto = Producto.builder()
                .id(1L).nombre("Taza Ceramica").sku("TAZ-001")
                .stockActual(10)
                .build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.reducirStock(1L, 3);

        assertThat(producto.getStockActual()).isEqualTo(7);
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("reducirStock con stock insuficiente lanza BusinessException y NO guarda")
    void reducirStock_conStockInsuficiente_lanzaBusinessException() {
        Producto producto = Producto.builder()
                .id(1L).nombre("Taza Ceramica").sku("TAZ-001")
                .stockActual(2)
                .build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> productoService.reducirStock(1L, 5))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Stock insuficiente");

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("reducirStock con cantidad cero lanza IllegalArgumentException (Guava Preconditions)")
    void reducirStock_conCantidadCero_lanzaIllegalArgument() {
        assertThatThrownBy(() -> productoService.reducirStock(1L, 0))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(productoRepository);
    }

    @Test
    @DisplayName("crear con SKU nuevo normaliza a mayusculas y guarda el producto")
    void crear_conSkuNuevo_guardaYNormalizaSku() {
        Producto producto = Producto.builder()
                .nombre("Producto Nuevo").sku("taz-099")
                .precioCompra(new BigDecimal("10.00"))
                .precioVenta(new BigDecimal("20.00"))
                .build();
        when(productoRepository.existsBySku("TAZ-099")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto resultado = productoService.crear(producto);

        assertThat(resultado.getSku()).isEqualTo("TAZ-099");
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("crear con SKU duplicado lanza BusinessException y NO guarda")
    void crear_conSkuDuplicado_lanzaBusinessException() {
        Producto producto = Producto.builder()
                .nombre("Producto Duplicado").sku("TAZ-001")
                .precioCompra(new BigDecimal("10.00"))
                .precioVenta(new BigDecimal("20.00"))
                .build();
        when(productoRepository.existsBySku("TAZ-001")).thenReturn(true);

        assertThatThrownBy(() -> productoService.crear(producto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ya existe un producto");

        verify(productoRepository, never()).save(any(Producto.class));
    }
}