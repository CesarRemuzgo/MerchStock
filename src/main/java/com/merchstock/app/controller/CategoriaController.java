package com.merchstock.app.controller;

import com.merchstock.app.entity.Categoria;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para la gestion de Categorias
 *
 * Endpoints:
 *  GET  /categorias              → lista categorias
 *  GET  /categorias/nuevo        → formulario nueva categoria
 *  POST /categorias              → guardar categoria nueva
 *  GET  /categorias/{id}/editar  → formulario edicion
 *  POST /categorias/{id}         → actualizar categoria
 *  POST /categorias/{id}/eliminar → eliminar (logico)
 */
@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Lista de categorias
     */
    @GetMapping
    public String listarCategorias(Model model) {
        log.info("Listando todas las categorias");
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    /**
     * Formulario para nueva categoria
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.debug("Mostrando formulario para nueva categoria");
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("modo", "nuevo");
        return "categorias/formulario";
    }

    /**
     * Guardar categoria nueva
     */
    @PostMapping
    public String guardarCategoria(
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("Errores de validacion al crear categoria");
            model.addAttribute("modo", "nuevo");
            return "categorias/formulario";
        }

        try {
            Categoria creada = categoriaService.crear(categoria);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Categoria creada exitosamente: " + creada.getNombre());
            return "redirect:/categorias";
        } catch (BusinessException ex) {
            log.error("Error al crear categoria: {}", ex.getMessage());
            model.addAttribute("modo", "nuevo");
            model.addAttribute("mensajeError", ex.getMessage());
            return "categorias/formulario";
        }
    }

    /**
     * Formulario de edicion
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", categoria);
            model.addAttribute("modo", "editar");
            return "categorias/formulario";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/categorias";
        }
    }

    /**
     * Actualizar categoria
     */
    @PostMapping("/{id}")
    public String actualizarCategoria(
            @PathVariable Long id,
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("modo", "editar");
            return "categorias/formulario";
        }

        try {
            categoriaService.actualizar(id, categoria);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Categoria actualizada exitosamente");
            return "redirect:/categorias";
        } catch (Exception ex) {
            log.error("Error al actualizar categoria: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/categorias/" + id + "/editar";
        }
    }

    /**
     * Eliminar categoria (eliminacion logica)
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Categoria desactivada exitosamente");
        } catch (Exception ex) {
            log.error("Error al eliminar categoria: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/categorias";
    }
}