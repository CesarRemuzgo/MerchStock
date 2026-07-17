package com.merchstock.app.controller;

import com.merchstock.app.entity.Producto;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.service.CategoriaService;
import com.merchstock.app.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.merchstock.app.dto.ResultadoImportacion;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador MVC para la gestion de Productos (capa View - Controller)
 *
 * Endpoints:
 *  GET  /productos              → lista productos
 *  GET  /productos/nuevo        → formulario para nuevo producto
 *  POST /productos              → guardar producto nuevo
 *  GET  /productos/{id}/editar  → formulario de edicion
 *  POST /productos/{id}         → actualizar producto
 *  POST /productos/{id}/eliminar → eliminar producto
 *  GET  /productos/alertas      → lista de productos con stock bajo (RF15)
 */
@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    /**
     * Lista de productos
     * URL: GET /productos
     */
    @GetMapping
    public String listarProductos(
            @RequestParam(value = "buscar", required = false) String buscar,
            Model model) {
        log.info("Listando productos. Filtro: {}", buscar);

        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("productos", productoService.buscarPorNombre(buscar));
            model.addAttribute("buscar", buscar);
        } else {
            model.addAttribute("productos", productoService.listarTodos());
        }

        model.addAttribute("totalAlertas", productoService.contarProductosConStockBajo());
        return "productos/lista";
    }

    /**
     * Formulario para nuevo producto
     * URL: GET /productos/nuevo
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.debug("Mostrando formulario para nuevo producto");
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("modo", "nuevo");
        return "productos/formulario";
    }

    /**
     * Guardar producto nuevo
     * URL: POST /productos
     */
    @PostMapping
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("Errores de validacion al crear producto");
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("modo", "nuevo");
            return "productos/formulario";
        }

        try {
            Producto creado = productoService.crear(producto);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Producto creado exitosamente: " + creado.getNombre());
            return "redirect:/productos";
        } catch (BusinessException ex) {
            log.error("Error al crear producto: {}", ex.getMessage());
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("modo", "nuevo");
            model.addAttribute("mensajeError", ex.getMessage());
            return "productos/formulario";
        }
    }

    /**
     * Formulario de edicion
     * URL: GET /productos/{id}/editar
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id);
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("modo", "editar");
            return "productos/formulario";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos";
        }
    }

    /**
     * Actualizar producto existente
     * URL: POST /productos/{id}
     */
    @PostMapping("/{id}")
    public String actualizarProducto(
            @PathVariable Long id,
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("modo", "editar");
            return "productos/formulario";
        }

        try {
            productoService.actualizar(id, producto);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Producto actualizado exitosamente");
            return "redirect:/productos";
        } catch (Exception ex) {
            log.error("Error al actualizar: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/" + id + "/editar";
        }
    }

    /**
     * Eliminar producto (eliminacion logica)
     * URL: POST /productos/{id}/eliminar
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Producto eliminado exitosamente");
        } catch (Exception ex) {
            log.error("Error al eliminar: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Lista de alertas de stock bajo (RF15)
     * URL: GET /productos/alertas
     */
    @GetMapping("/alertas")
    public String listarAlertasStock(Model model) {
        log.info("Mostrando alertas de stock bajo");
        model.addAttribute("productos", productoService.obtenerProductosConStockBajo());
        model.addAttribute("productosSinStock", productoService.obtenerProductosSinStock());
        return "productos/alertas";
    }

    /**
     * Muestra el formulario para importar productos desde CSV.
     * URL: GET /productos/importar
     */
    @GetMapping("/importar")
    public String mostrarFormularioImportar() {
        log.debug("Mostrando formulario de importacion CSV");
        return "productos/importar";
    }

    /**
     * Procesa el archivo CSV subido e importa los productos.
     * URL: POST /productos/importar
     */
    @PostMapping("/importar")
    public String importarCsv(@RequestParam("archivo") MultipartFile archivo,
                              RedirectAttributes redirectAttributes) {
        log.info("Recibido archivo CSV para importar: {}", archivo.getOriginalFilename());

        // Validar que se haya subido un archivo
        if (archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Debe seleccionar un archivo CSV");
            return "redirect:/productos/importar";
        }

        // Validar extension .csv
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "El archivo debe tener extension .csv");
            return "redirect:/productos/importar";
        }

        try {
            ResultadoImportacion resultado =
                    productoService.importarDesdeCsv(archivo.getInputStream());

            // Mensaje de exito con el resumen
            redirectAttributes.addFlashAttribute("mensajeExito",
                    resultado.getExitosos() + " producto(s) importado(s) correctamente");

            // Si hubo errores, los pasamos para mostrarlos en detalle
            if (resultado.tieneErrores()) {
                redirectAttributes.addFlashAttribute("erroresImportacion", resultado.getErrores());
            }

            return "redirect:/productos";

        } catch (Exception ex) {
            log.error("Error al importar CSV: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al procesar el archivo: " + ex.getMessage());
            return "redirect:/productos/importar";
        }
    }
}