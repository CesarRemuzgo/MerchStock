package com.merchstock.app.mantenimiento;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Tareas programadas de mantenimiento: backup automatico diario de la
 * base de datos y limpieza de respaldos que superan la politica de
 * retencion configurada.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MantenimientoProgramado {

    private final BackupService backupService;

    /**
     * Genera un backup automatico todos los dias a las 2:00 a.m.
     * Cron: segundo minuto hora dia-mes mes dia-semana
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void backupAutomaticoDiario() {
        log.info("Ejecutando backup automatico programado...");
        try {
            String archivo = backupService.generarBackup();
            log.info("Backup automatico completado: {}", archivo);
        } catch (IOException e) {
            log.error("Error de E/S durante el backup automatico", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("El backup automatico fue interrumpido", e);
        }
    }

    /**
     * Limpia los backups antiguos todos los dias a las 3:00 a.m.,
     * una hora despues del backup, segun la politica de retencion.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void limpiezaAutomaticaDiaria() {
        log.info("Ejecutando limpieza de backups antiguos...");
        int eliminados = backupService.limpiarBackupsAntiguos();
        log.info("Limpieza completada: {} backup(s) eliminado(s)", eliminados);
    }
}