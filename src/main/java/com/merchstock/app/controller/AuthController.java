package com.merchstock.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para autenticacion (login, logout, errores de acceso).
 *
 * Spring Security maneja la logica del login internamente (POST /login),
 * pero necesitamos un controlador que sirva la pagina de login (GET /login)
 * y la pagina de acceso denegado.
 */
@Controller
@Slf4j
public class AuthController {

    /**
     * Pagina de login.
     * Acepta query parameters opcionales:
     *  - ?error=true   : credenciales incorrectas
     *  - ?logout=true  : sesion cerrada exitosamente
     *  - ?expired=true : sesion expiro por inactividad
     */
    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String expired,
            @RequestParam(required = false) String locked,
            Model model) {

        if (error != null) {
            log.debug("Mostrando pagina de login con error de credenciales");
            model.addAttribute("mensajeError", "Usuario o contrasena incorrectos");
        }

        if (locked != null) {
            log.debug("Mostrando pagina de login con cuenta bloqueada");
            model.addAttribute("mensajeError", "Cuenta bloqueada temporalmente por multiples intentos fallidos. Intenta nuevamente en 15 minutos.");
        }

        if (logout != null) {
            log.debug("Mostrando pagina de login despues de logout");
            model.addAttribute("mensajeExito", "Sesion cerrada correctamente");
        }

        if (expired != null) {
            log.debug("Mostrando pagina de login despues de expiracion de sesion");
            model.addAttribute("mensajeAlerta", "Tu sesion expiro por inactividad");
        }

        return "auth/login";
    }

    /**
     * Pagina de acceso denegado (403).
     */
    @GetMapping("/access-denied")
    public String mostrarAccesoDenegado() {
        log.warn("Acceso denegado mostrado al usuario");
        return "error/access-denied";
    }
}