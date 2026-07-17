package com.merchstock.app.monitoreo;

import com.merchstock.app.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tarea programada que registra periodicamente el estado del sistema.
 */
@Component
@RequiredArgsConstructor
public class MonitoreoProgramado {

    private static final Logger NEGOCIO = LoggerFactory.getLogger("NEGOCIO");
    private static final double MB = 1024.0 * 1024.0;

    private final ProductoRepository productoRepository;

    /** Se ejecuta cada 15 minutos (900000 ms). */
    @Scheduled(fixedRate = 900000)
    public void registrarEstadoDelSistema() {
        Runtime runtime = Runtime.getRuntime();
        long usada = (runtime.totalMemory() - runtime.freeMemory());
        long maxima = runtime.maxMemory();
        long enAlerta = productoRepository.contarProductosEnAlerta();

        NEGOCIO.info("HEARTBEAT usoMemoriaMB={} productosEnAlerta={}",
                Math.round(usada / MB), enAlerta);
    }
}