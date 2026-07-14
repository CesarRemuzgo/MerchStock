package com.merchstock.app.mantenimiento;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Servicio de mantenimiento: genera respaldos de la base de datos con
 * mysqldump y elimina los respaldos antiguos segun la politica de retencion.
 */
@Service
@Slf4j
public class BackupService {

    private static final Logger NEGOCIO = org.slf4j.LoggerFactory.getLogger("NEGOCIO");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Value("${mantenimiento.backup.directorio}")
    private String directorio;
    @Value("${mantenimiento.backup.dias-retencion}")
    private int diasRetencion;
    @Value("${mantenimiento.backup.host}")
    private String host;
    @Value("${mantenimiento.backup.puerto}")
    private String puerto;
    @Value("${mantenimiento.backup.database}")
    private String database;
    @Value("${mantenimiento.backup.usuario}")
    private String usuario;
    @Value("${mantenimiento.backup.password}")
    private String password;

    /** Crea el directorio de backups al iniciar la aplicacion. */
    @PostConstruct
    public void inicializar() {
        try {
            Files.createDirectories(Paths.get(directorio));
        } catch (IOException e) {
            log.error("No se pudo crear el directorio de backups", e);
        }
    }

    /**
     * Genera un respaldo de la base de datos usando mysqldump.
     *
     * @return el nombre del archivo generado
     * @throws IOException          si falla la escritura del archivo
     * @throws InterruptedException si el proceso es interrumpido
     */
    public String generarBackup() throws IOException, InterruptedException {
        String nombreArchivo = "backup_" + database + "_" + LocalDateTime.now().format(FMT) + ".sql";
        Path rutaSalida = Paths.get(directorio, nombreArchivo);

        ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-h", host,
                "-P", puerto,
                "-u", usuario,
                "-p" + password,
                "--databases", database,
                "--result-file=" + rutaSalida.toAbsolutePath()
        );
        pb.redirectErrorStream(true);

        log.info("Iniciando backup de la base de datos {}", database);
        Process proceso = pb.start();
        int codigo = proceso.waitFor();

        if (codigo == 0) {
            long tamano = Files.size(rutaSalida) / 1024;
            NEGOCIO.info("BACKUP_GENERADO archivo={} tamanoKB={}", nombreArchivo, tamano);
            log.info("Backup generado correctamente: {} ({} KB)", nombreArchivo, tamano);
            return nombreArchivo;
        } else {
            log.error("mysqldump termino con codigo {}", codigo);
            throw new IOException("Fallo la generacion del backup (codigo " + codigo + ")");
        }
    }

    /**
     * Elimina los respaldos mas antiguos que la politica de retencion.
     *
     * @return la cantidad de archivos eliminados
     */
    public int limpiarBackupsAntiguos() {
        File carpeta = new File(directorio);
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".sql"));
        if (archivos == null) return 0;

        long limite = System.currentTimeMillis() - (diasRetencion * 24L * 60 * 60 * 1000);
        int eliminados = 0;

        for (File archivo : archivos) {
            if (archivo.lastModified() < limite && archivo.delete()) {
                eliminados++;
                log.info("Backup antiguo eliminado: {}", archivo.getName());
            }
        }
        if (eliminados > 0) {
            NEGOCIO.info("BACKUPS_LIMPIADOS cantidad={}", eliminados);
        }
        return eliminados;
    }

    /** Lista los backups existentes, del mas reciente al mas antiguo. */
    public List<String> listarBackups() {
        File carpeta = new File(directorio);
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".sql"));
        List<String> nombres = new ArrayList<>();
        if (archivos != null) {
            java.util.Arrays.stream(archivos)
                    .sorted(Comparator.comparingLong(File::lastModified).reversed())
                    .forEach(f -> nombres.add(f.getName()));
        }
        return nombres;
    }
}