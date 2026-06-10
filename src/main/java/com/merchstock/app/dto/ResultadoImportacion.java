package com.merchstock.app.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO que resume el resultado de una importacion masiva de productos desde CSV.
 * Acumula la cantidad de productos creados con exito y la lista de errores
 * encontrados (por linea), para mostrar feedback claro al usuario.
 */
@Getter
public class ResultadoImportacion {

    private int exitosos = 0;
    private final List<String> errores = new ArrayList<>();

    public void registrarExito() {
        this.exitosos++;
    }

    public void registrarError(int numeroLinea, String mensaje) {
        this.errores.add("Linea " + numeroLinea + ": " + mensaje);
    }

    public int getTotalErrores() {
        return errores.size();
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }
}