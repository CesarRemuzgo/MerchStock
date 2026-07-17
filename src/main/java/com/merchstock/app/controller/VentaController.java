package com.merchstock.app.controller;

import com.merchstock.app.dto.VentaForm;
import com.merchstock.app.entity.Producto;
import com.merchstock.app.entity.Venta;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.service.ClienteService;
import com.merchstock.app.service.ProductoService;
import com.merchstock.app.service.UsuarioService;
import com.merchstock.app.service.VentaPdfService;
import com.merchstock.app.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Controlador MVC para la gestion de Ventas.
 *
 * Maneja la cabecera (Venta) y los detalles (carrito de productos).
 * Incluye un endpoint REST auxiliar /ventas/api/productos para que
 * el JavaScript del formulario pueda obtener la lista de productos.
 */
@Controller
@RequestMapping("/ventas")
@RequiredArgsConstructor
@Slf4j
public class VentaController {

    private final VentaService ventaService;
    private final VentaPdfService ventaPdfService;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    /**
     * Lista de ventas
     */
    @GetMapping
    public String listarVentas(Model model) {
        log.info("Listando todas las ventas");
        model.addAttribute("ventas", ventaService.listarTodas());
        return "ventas/lista";
    }

    /**
     * Formulario para nueva venta
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        log.debug("Mostrando formulario para nueva venta");

        model.addAttribute("ventaForm", new VentaForm());
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("vendedores", usuarioService.listarActivos());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("metodosPago", Venta.MetodoPago.values());

        return "ventas/formulario";
    }

    /**
     * Registrar nueva venta
     */
    @PostMapping
    public String registrarVenta(@ModelAttribute VentaForm ventaForm,
                                  RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.registrarVenta(ventaForm);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Venta registrada exitosamente: " + venta.getCodigoVenta()
                    + " (Total: S/ " + venta.getTotal() + ")");
            return "redirect:/ventas/" + venta.getId();
        } catch (BusinessException | ResourceNotFoundException ex) {
            log.error("Error al registrar venta: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/ventas/nueva";
        } catch (Exception ex) {
            log.error("Error inesperado al registrar venta", ex);
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al procesar la venta: " + ex.getMessage());
            return "redirect:/ventas/nueva";
        }
    }

    /**
     * Ver detalle de una venta
     */
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.buscarPorId(id);
            model.addAttribute("venta", venta);
            return "ventas/detalle";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/ventas";
        }
    }

    /**
     * Descarga el PDF de la boleta/comprobante de una venta.
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarBoletaPdf(@PathVariable Long id) throws IOException {
        log.info("Descargando boleta PDF de la venta {}", id);

        Venta venta = ventaService.buscarPorId(id);
        byte[] pdfBytes = ventaPdfService.generarBoletaPdf(venta);

        String nombreArchivo = "MerchStock_Boleta_" + venta.getCodigoVenta() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", nombreArchivo);
        headers.setContentLength(pdfBytes.length);

        log.info("Enviando boleta PDF: {} ({} bytes)", nombreArchivo, pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    /**
     * Anular una venta
     */
    @PostMapping("/{id}/anular")
    public String anularVenta(@PathVariable Long id,
                               @RequestParam(value = "motivo", required = false) String motivo,
                               RedirectAttributes redirectAttributes) {
        try {
            ventaService.anularVenta(id, motivo);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Venta anulada exitosamente. Stock revertido.");
        } catch (Exception ex) {
            log.error("Error al anular venta: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/ventas/" + id;
    }

    /**
     * Endpoint REST auxiliar - devuelve los productos como JSON
     * para el JavaScript del formulario de nueva venta.
     */
    @GetMapping("/api/productos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiListarProductos() {
        log.debug("API: solicitando lista de productos para formulario de venta");

        List<Map<String, Object>> productosJson = productoService.listarTodos().stream()
                .map(this::productoToMap)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productosJson);
    }

    /**
     * Convierte un Producto en un Map simple (para JSON limpio sin lazy loading issues)
     */
    private Map<String, Object> productoToMap(Producto p) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("sku", p.getSku());
        map.put("nombre", p.getNombre());
        map.put("precioVenta", p.getPrecioVenta());
        map.put("stockActual", p.getStockActual());
        return map;
    }
}