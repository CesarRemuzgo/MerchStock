package com.merchstock.app.mantenimiento;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controlador del panel de mantenimiento: permite generar backups
 * manuales, listarlos y descargarlos. Acceso restringido a ADMIN
 * (ver SecurityConfig).
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MantenimientoController {

    private final BackupService backupService;

    /** Muestra el panel de mantenimiento con la lista de backups existentes. */
    @GetMapping("/mantenimiento")
    public String index(Model model) {
        List<String> backups = backupService.listarBackups();
        model.addAttribute("backups", backups);
        model.addAttribute("totalBackups", backups.size());
        return "mantenimiento/index";
    }

    /** Genera un backup manual al hacer clic en el boton del panel. */
    @PostMapping("/mantenimiento/backup")
    public String generarBackupManual(RedirectAttributes redirectAttributes) {
        try {
            String archivo = backupService.generarBackup();
            redirectAttributes.addFlashAttribute("exito",
                    "Backup generado correctamente: " + archivo);
        } catch (IOException e) {
            log.error("Error de E/S al generar backup manual", e);
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo generar el backup: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Backup manual interrumpido", e);
            redirectAttributes.addFlashAttribute("error",
                    "El proceso de backup fue interrumpido.");
        }
        return "redirect:/mantenimiento";
    }

    /** Descarga un archivo de backup especifico. */
    @GetMapping("/mantenimiento/descargar/{nombreArchivo}")
    public ResponseEntity<Resource> descargarBackup(@PathVariable String nombreArchivo) {
        // Se valida que el nombre no contenga rutas para evitar path traversal
        if (nombreArchivo.contains("..") || nombreArchivo.contains("/") || nombreArchivo.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }

        File archivo = Paths.get("backups", nombreArchivo).toFile();
        if (!archivo.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource recurso = new FileSystemResource(archivo);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .body(recurso);
    }
}