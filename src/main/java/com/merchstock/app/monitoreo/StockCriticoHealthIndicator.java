package com.merchstock.app.monitoreo;

import com.merchstock.app.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Indicador de salud de negocio: reporta el estado del inventario.
 * Se expone en /actuator/health bajo la clave "stockCritico".
 */
@Component("stockCritico")
@RequiredArgsConstructor
@Slf4j
public class StockCriticoHealthIndicator implements HealthIndicator {

    /** Umbral de productos en alerta que degrada el estado del sistema. */
    private static final long UMBRAL_ALERTA = 5L;

    private final ProductoRepository productoRepository;

    @Override
    public Health health() {
        try {
            long enAlerta = productoRepository.contarProductosEnAlerta();
            long sinStock = productoRepository.contarProductosSinStock();

            Health.Builder estado = (enAlerta >= UMBRAL_ALERTA)
                    ? Health.status("ALERTA")
                    : Health.up();

            return estado
                    .withDetail("productosEnAlerta", enAlerta)
                    .withDetail("productosSinStock", sinStock)
                    .withDetail("umbral", UMBRAL_ALERTA)
                    .build();

        } catch (Exception e) {
            log.error("Fallo al evaluar la salud del inventario", e);
            return Health.down(e).build();
        }
    }
}