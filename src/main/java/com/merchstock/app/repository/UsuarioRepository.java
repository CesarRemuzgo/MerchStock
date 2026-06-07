package com.merchstock.app.repository;

import com.merchstock.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio DAO para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su username (utilizado en login)
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si existe un usuario con ese username
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con ese email
     */
    boolean existsByEmail(String email);

    /**
     * Lista usuarios por rol
     */
    List<Usuario> findByRol(Usuario.RolUsuario rol);

    /**
     * Lista solo usuarios activos
     */
    List<Usuario> findByActivoTrueOrderByNombreCompletoAsc();
}