package com.merchstock.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Panel de monitoreo del sistema. Consume los datos de Spring Boot Actuator
 * y los presenta en una vista Thymeleaf para el rol ADMIN.
 */
@Controller
@RequestMapping("/monitoreo")
@RequiredArgsConstructor
@Slf4j
public class MonitoreoController {

    private static final double MB = 1024.0 * 1024.0;

    private final HealthEndpoint healthEndpoint;
    private final MetricsEndpoint metricsEndpoint;

    @GetMapping
    public String panel(Model model) {
        log.info("Acceso al panel de monitoreo del sistema");

        String estado = healthEndpoint.health().getStatus().getCode();
        model.addAttribute("estadoGeneral", estado);

        double heapUsado = leerMetrica("jvm.memory.used", "area:heap") / MB;
        double heapMaximo = leerMetrica("jvm.memory.max", "area:heap") / MB;
        double porcentajeHeap = (heapMaximo > 0) ? (heapUsado / heapMaximo) * 100 : 0;

        model.addAttribute("heapUsado", Math.round(heapUsado));
        model.addAttribute("heapMaximo", Math.round(heapMaximo));
        model.addAttribute("porcentajeHeap", Math.round(porcentajeHeap));

        long segundos = (long) leerMetrica("process.uptime", null);
        model.addAttribute("uptime", String.format("%dh %dm", segundos / 3600, (segundos % 3600) / 60));

        model.addAttribute("peticiones", (long) leerMetrica("http.server.requests", null));

        double cpu = leerMetrica("process.cpu.usage", null) * 100;
        model.addAttribute("cpu", Math.round(cpu * 10) / 10.0);

        return "monitoreo/index";
    }

    /**
     * Lee una metrica de Micrometer y devuelve su primer valor.
     *
     * @param nombre nombre de la metrica (ej. jvm.memory.used)
     * @param tag    filtro opcional en formato clave:valor, o null
     * @return el valor de la metrica, o 0 si no esta disponible
     */
    private double leerMetrica(String nombre, String tag) {
        MetricsEndpoint.MetricDescriptor descriptor =
                metricsEndpoint.metric(nombre, (tag == null) ? null : List.of(tag));

        if (descriptor == null || descriptor.getMeasurements().isEmpty()) {
            return 0d;
        }
        return descriptor.getMeasurements().get(0).getValue();
    }
}