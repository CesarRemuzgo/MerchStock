package com.merchstock.app.controller;

import com.merchstock.app.entity.Usuario;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para la gestion de Usuarios
 */
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        log.info("Listando todos los usuarios");
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.debug("Mostrando formulario para nuevo usuario");
        Usuario usuario = new Usuario();
        usuario.setRol(Usuario.RolUsuario.VENDEDOR);
        usuario.setActivo(true);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Usuario.RolUsuario.values());
        model.addAttribute("modo", "nuevo");
        return "usuarios/formulario";
    }

    @PostMapping
    public String guardarUsuario(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult,
            @RequestParam("passwordPlano") String passwordPlano,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Ignorar errores de validacion en passwordHash (lo manejamos nosotros)
        if (bindingResult.hasFieldErrors("passwordHash")) {
            bindingResult.getFieldErrors("passwordHash").forEach(err ->
                    log.debug("Ignorando error de validacion en passwordHash"));
        }

        if (bindingResult.hasErrors() && !soloPasswordHashError(bindingResult)) {
            log.warn("Errores de validacion al crear usuario");
            model.addAttribute("roles", Usuario.RolUsuario.values());
            model.addAttribute("modo", "nuevo");
            return "usuarios/formulario";
        }

        try {
            Usuario creado = usuarioService.crear(usuario, passwordPlano);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Usuario creado exitosamente: " + creado.getUsername());
            return "redirect:/usuarios";
        } catch (BusinessException | IllegalArgumentException ex) {
            log.error("Error al crear usuario: {}", ex.getMessage());
            model.addAttribute("roles", Usuario.RolUsuario.values());
            model.addAttribute("modo", "nuevo");
            model.addAttribute("mensajeError", ex.getMessage());
            return "usuarios/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            // No exponer el password hash al frontend
            usuario.setPasswordHash("");
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", Usuario.RolUsuario.values());
            model.addAttribute("modo", "editar");
            return "usuarios/formulario";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/{id}")
    public String actualizarUsuario(
            @PathVariable Long id,
            @ModelAttribute("usuario") Usuario usuario,
            @RequestParam(value = "passwordPlano", required = false) String passwordPlano,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.actualizar(id, usuario, passwordPlano);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Usuario actualizado exitosamente");
            return "redirect:/usuarios";
        } catch (Exception ex) {
            log.error("Error al actualizar usuario: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/usuarios/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Usuario desactivado exitosamente");
        } catch (Exception ex) {
            log.error("Error al eliminar usuario: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/usuarios";
    }

    /**
     * Verifica si los unicos errores de validacion son del campo passwordHash
     * (que ignoramos porque manejamos el password aparte)
     */
    private boolean soloPasswordHashError(BindingResult result) {
        return result.getFieldErrorCount() == result.getFieldErrorCount("passwordHash");
    }
}