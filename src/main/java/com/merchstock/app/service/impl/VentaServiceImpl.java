package com.merchstock.app.service.impl;

import com.google.common.base.Preconditions;
import com.merchstock.app.dto.ItemVentaForm;
import com.merchstock.app.dto.VentaForm;
import com.merchstock.app.entity.*;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.repository.*;
import com.merchstock.app.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

/**
 * Implementacion del servicio de Ventas - El nucleo transaccional del sistema.
 *
 * APLICA:
 * - SOLID: Interface Segregation + Dependency Inversion
 * - Defensa en 3 capas para stock (SQL CHECK + Java @Min + Service validation)
 * - @Transactional para atomicidad
 * - Librerias rubrica APF3: Guava, Commons Lang, Logback
 *
 * IGV peruano: 18% (factor 0.18)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VentaServiceImpl implements VentaService {

    // IGV peruano - configurable si en el futuro cambia
    private static final BigDecimal IGV_FACTOR = new BigDecimal("0.18");
    private static final int ESCALA_DECIMAL = 2;

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaStockRepository auditoriaStockRepository;

    @Override
    public List<Venta> listarTodas() {
        log.debug("Listando todas las ventas");
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> listarRecientes() {
        log.debug("Listando top 10 ventas recientes");
        return ventaRepository.findTop10ByOrderByFechaVentaDesc();
    }

    @Override
    public List<Venta> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        Preconditions.checkNotNull(inicio, "La fecha inicio no puede ser nula");
        Preconditions.checkNotNull(fin, "La fecha fin no puede ser nula");
        return ventaRepository.findByRangoFechas(inicio, fin);
    }

    @Override
    public Venta buscarPorId(Long id) {
        Preconditions.checkNotNull(id, "El ID de venta no puede ser nulo");
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        // Forzar carga de los detalles (porque son lazy)
        venta.getDetalles().size();
        return venta;
    }

    @Override
    public Venta buscarPorCodigo(String codigoVenta) {
        Preconditions.checkArgument(StringUtils.isNotBlank(codigoVenta),
                "El codigo de venta no puede estar vacio");
        return ventaRepository.findByCodigoVenta(codigoVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "codigo", codigoVenta));
    }

    /**
     * ⭐ EL METODO MAS IMPORTANTE DEL SISTEMA
     *
     * Registra una venta de manera ATOMICA y TRANSACCIONAL.
     *
     * Pasos:
     *  1. Validar datos del formulario
     *  2. Cargar entidades (cliente opcional, usuario obligatorio)
     *  3. Para CADA item:
     *     - Validar que el producto existe y esta activo
     *     - Validar stock suficiente (defensa en capa Service)
     *     - Calcular subtotal de la linea
     *  4. Crear la cabecera de Venta
     *  5. Crear los VentaDetalle
     *  6. Calcular subtotal total, IGV y total general
     *  7. Reducir stock de cada producto
     *  8. Generar auditoria de stock por cada reduccion
     *  9. Persistir todo
     *
     * Si CUALQUIER paso falla, se hace rollback (gracias a @Transactional).
     */
    @Override
    @Transactional
    public Venta registrarVenta(VentaForm form) {
        log.info("Registrando nueva venta. Vendedor: {}, Items: {}",
                form.getUsuarioId(), form.getItems().size());

        // 1. VALIDACIONES INICIALES
        Preconditions.checkNotNull(form, "El formulario de venta no puede ser nulo");
        Preconditions.checkArgument(form.getItems() != null && !form.getItems().isEmpty(),
                "La venta debe tener al menos un item");

        // 2. CARGAR USUARIO (vendedor) - obligatorio
        Usuario usuario = usuarioRepository.findById(form.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", form.getUsuarioId()));

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new BusinessException("El usuario " + usuario.getUsername() + " esta inactivo");
        }

        // CARGAR CLIENTE - opcional (puede ser null para venta sin cliente registrado)
        Cliente cliente = null;
        if (form.getClienteId() != null) {
            cliente = clienteRepository.findById(form.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", form.getClienteId()));
        }

        // 3. CREAR LA CABECERA DE VENTA
        Venta venta = Venta.builder()
                .codigoVenta(generarCodigoVenta())
                .cliente(cliente)
                .usuario(usuario)
                .fechaVenta(LocalDateTime.now())
                .metodoPago(form.getMetodoPago())
                .estado(Venta.EstadoVenta.COMPLETADA)
                .observaciones(form.getObservaciones())
                .subtotal(BigDecimal.ZERO)
                .igv(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        // 4. PROCESAR CADA ITEM (validar stock + crear detalle + reducir stock)
        BigDecimal subtotalGeneral = BigDecimal.ZERO;

        for (ItemVentaForm itemForm : form.getItems()) {
            // Cargar el producto
            Producto producto = productoRepository.findById(itemForm.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto", "id", itemForm.getProductoId()));

            if (Boolean.FALSE.equals(producto.getActivo())) {
                throw new BusinessException(
                        "El producto " + producto.getNombre() + " no esta activo");
            }

            // DEFENSA EN CAPA SERVICE: validar stock suficiente
            if (producto.getStockActual() < itemForm.getCantidad()) {
                throw new BusinessException(String.format(
                        "Stock insuficiente para %s. Disponible: %d, solicitado: %d",
                        producto.getNombre(),
                        producto.getStockActual(),
                        itemForm.getCantidad()));
            }

            // Calcular subtotal de la linea (cantidad * precio venta del producto)
            BigDecimal precioUnitario = producto.getPrecioVenta();
            BigDecimal subtotalLinea = precioUnitario
                    .multiply(BigDecimal.valueOf(itemForm.getCantidad()))
                    .setScale(ESCALA_DECIMAL, RoundingMode.HALF_UP);

            // Crear el VentaDetalle
            VentaDetalle detalle = VentaDetalle.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(itemForm.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotalLinea)
                    .build();

            venta.getDetalles().add(detalle);

            // REDUCIR STOCK del producto
            Integer stockAnterior = producto.getStockActual();
            Integer stockNuevo = stockAnterior - itemForm.getCantidad();
            producto.setStockActual(stockNuevo);
            productoRepository.save(producto);

            // AUDITORIA DE STOCK
            AuditoriaStock auditoria = AuditoriaStock.builder()
                    .producto(producto)
                    .tipoMovimiento(AuditoriaStock.TipoMovimiento.SALIDA)
                    .cantidad(itemForm.getCantidad())
                    .stockAnterior(stockAnterior)
                    .stockNuevo(stockNuevo)
                    .motivo("Venta - Codigo pendiente de asignar")  // se actualiza despues
                    .usuario(usuario)
                    .venta(venta)  // se establece tras guardar venta
                    .build();
            auditoriaStockRepository.save(auditoria);

            // Acumular subtotal general
            subtotalGeneral = subtotalGeneral.add(subtotalLinea);

            log.debug("Item procesado: {} x{} = S/{}",
                    producto.getNombre(), itemForm.getCantidad(), subtotalLinea);
        }

        // 5. CALCULAR IGV (18%) Y TOTAL
        BigDecimal igv = subtotalGeneral
                .multiply(IGV_FACTOR)
                .setScale(ESCALA_DECIMAL, RoundingMode.HALF_UP);

        BigDecimal total = subtotalGeneral.add(igv)
                .setScale(ESCALA_DECIMAL, RoundingMode.HALF_UP);

        venta.setSubtotal(subtotalGeneral);
        venta.setIgv(igv);
        venta.setTotal(total);

        // 6. PERSISTIR (cascade guarda detalles automaticamente)
        Venta ventaGuardada = ventaRepository.save(venta);

        log.info("Venta registrada exitosamente. Codigo: {}, Total: S/{}",
                ventaGuardada.getCodigoVenta(), ventaGuardada.getTotal());

        return ventaGuardada;
    }

    /**
     * Anula una venta y revierte el stock de sus productos.
     */
    @Override
    @Transactional
    public void anularVenta(Long id, String motivo) {
        log.info("Anulando venta ID: {}", id);
        Venta venta = buscarPorId(id);

        if (venta.getEstado() == Venta.EstadoVenta.ANULADA) {
            throw new BusinessException("La venta ya esta anulada");
        }

        // Revertir stock de cada item
        for (VentaDetalle detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            Integer stockAnterior = producto.getStockActual();
            Integer stockNuevo = stockAnterior + detalle.getCantidad();

            producto.setStockActual(stockNuevo);
            productoRepository.save(producto);

            // Auditoria de reversa
            AuditoriaStock auditoria = AuditoriaStock.builder()
                    .producto(producto)
                    .tipoMovimiento(AuditoriaStock.TipoMovimiento.ENTRADA)
                    .cantidad(detalle.getCantidad())
                    .stockAnterior(stockAnterior)
                    .stockNuevo(stockNuevo)
                    .motivo("Reversa por anulacion de venta: " + venta.getCodigoVenta()
                            + (StringUtils.isNotBlank(motivo) ? " - " + motivo : ""))
                    .usuario(venta.getUsuario())
                    .venta(venta)
                    .build();
            auditoriaStockRepository.save(auditoria);

            log.debug("Stock revertido: {} +{} (total: {})",
                    producto.getNombre(), detalle.getCantidad(), stockNuevo);
        }

        // Marcar la venta como anulada
        venta.setEstado(Venta.EstadoVenta.ANULADA);
        venta.setObservaciones(
                (StringUtils.isNotBlank(venta.getObservaciones()) ? venta.getObservaciones() + " | " : "")
                + "ANULADA: " + (StringUtils.isNotBlank(motivo) ? motivo : "sin motivo"));
        ventaRepository.save(venta);

        log.info("Venta {} anulada correctamente", venta.getCodigoVenta());
    }

    @Override
    public BigDecimal calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.sumTotalVentasEntreFechas(inicio, fin);
    }

    @Override
    public long contarVentas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.countVentasEntreFechas(inicio, fin);
    }

    /**
     * Genera un codigo de venta unico con formato VTA-YYYY-NNNN
     * Ejemplo: VTA-2026-0001, VTA-2026-0002...
     */
    private String generarCodigoVenta() {
        long totalVentas = ventaRepository.count();
        long siguienteNumero = totalVentas + 1;
        int anio = Year.now().getValue();
        String codigo = String.format("VTA-%d-%04d", anio, siguienteNumero);
        log.debug("Codigo de venta generado: {}", codigo);
        return codigo;
    }
}