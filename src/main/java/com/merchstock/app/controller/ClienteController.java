package com.merchstock.app.controller;

import com.merchstock.app.entity.Cliente;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para la gestion de Clientes
 *
 * Endpoints:
 *  GET  /clientes              → lista clientes
 *  GET  /clientes/nuevo        → formulario nuevo cliente
 *  POST /clientes              → guardar cliente nuevo
 *  GET  /clientes/{id}/editar  → formulario edicion
 *  POST /clientes/{id}         → actualizar cliente
 *  POST /clientes/{id}/eliminar → desactivar cliente
 */
@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listarClientes(
            @RequestParam(value = "buscar", required = false) String buscar,
            Model model) {
        log.info("Listando clientes. Filtro: {}", buscar);

        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("clientes", clienteService.buscarPorNombre(buscar));
            model.addAttribute("buscar", buscar);
        } else {
            model.addAttribute("clientes", clienteService.listarTodos());
        }

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.debug("Mostrando formulario para nuevo cliente");
        Cliente cliente = new Cliente();
        cliente.setTipoDocumento(Cliente.TipoDocumento.DNI);
        cliente.setActivo(true);
        model.addAttribute("cliente", cliente);
        model.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
        model.addAttribute("modo", "nuevo");
        return "clientes/formulario";
    }

    @PostMapping
    public String guardarCliente(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("Errores de validacion al crear cliente");
            model.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
            model.addAttribute("modo", "nuevo");
            return "clientes/formulario";
        }

        try {
            Cliente creado = clienteService.crear(cliente);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Cliente creado exitosamente: " + creado.getNombreCompleto());
            return "redirect:/clientes";
        } catch (BusinessException ex) {
            log.error("Error al crear cliente: {}", ex.getMessage());
            model.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
            model.addAttribute("modo", "nuevo");
            model.addAttribute("mensajeError", ex.getMessage());
            return "clientes/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            model.addAttribute("cliente", cliente);
            model.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
            model.addAttribute("modo", "editar");
            return "clientes/formulario";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/{id}")
    public String actualizarCliente(
            @PathVariable Long id,
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
            model.addAttribute("modo", "editar");
            return "clientes/formulario";
        }

        try {
            clienteService.actualizar(id, cliente);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Cliente actualizado exitosamente");
            return "redirect:/clientes";
        } catch (Exception ex) {
            log.error("Error al actualizar cliente: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/clientes/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Cliente desactivado exitosamente");
        } catch (Exception ex) {
            log.error("Error al eliminar cliente: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/clientes";
    }
}