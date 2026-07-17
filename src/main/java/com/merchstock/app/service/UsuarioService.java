package com.merchstock.app.service;

import com.merchstock.app.entity.Usuario;

import java.util.List;

/**
 * Servicio de Usuarios - Interface (SOLID: Interface Segregation)
 */
public interface UsuarioService {

    /**
     * Lista todos los usuarios (activos e inactivos)
     */
    List<Usuario> listarTodos();

    /**
     * Lista solo usuarios activos
     */
    List<Usuario> listarActivos();

    /**
     * Lista usuarios por rol especifico
     */
    List<Usuario> listarPorRol(Usuario.RolUsuario rol);

    /**
     * Busca un usuario por ID
     */
    Usuario buscarPorId(Long id);

    /**
     * Busca un usuario por username
     */
    Usuario buscarPorUsername(String username);

    /**
     * Crea un nuevo usuario (encripta el password con BCrypt)
     */
    Usuario crear(Usuario usuario, String passwordPlano);

    /**
     * Actualiza un usuario existente.
     * Si passwordPlano es null o vacio, NO cambia el password.
     */
    Usuario actualizar(Long id, Usuario usuario, String passwordPlano);

    /**
     * Elimina logicamente un usuario
     */
    void eliminar(Long id);
}