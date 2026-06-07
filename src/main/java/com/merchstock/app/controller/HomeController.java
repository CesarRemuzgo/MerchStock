package com.merchstock.app.controller;

import com.merchstock.app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la pagina principal del sistema
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;

    /**
     * Pagina de inicio
     * URL: GET /
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalAlertas", productoService.contarProductosConStockBajo());
        return "home";
    }
}