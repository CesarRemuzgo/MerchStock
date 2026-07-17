package com.merchstock.app.service.impl;

import com.google.common.base.Preconditions;
import com.merchstock.app.entity.Usuario;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.repository.UsuarioRepository;
import com.merchstock.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del servicio de Usuarios
 *
 * IMPORTANTE: el password se encripta con BCrypt antes de guardarse.
 * El password en texto plano NUNCA se persiste ni se devuelve.
 *
 * Librerias APF3:
 * - Google Guava: Preconditions
 * - Apache Commons Lang: StringUtils
 * - Logback: @Slf4j
 * - Spring Security: BCryptPasswordEncoder (via PasswordEncoder bean)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        log.debug("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarActivos() {
        log.debug("Listando usuarios activos");
        return usuarioRepository.findByActivoTrueOrderByNombreCompletoAsc();
    }

    @Override
    public List<Usuario> listarPorRol(Usuario.RolUsuario rol) {
        Preconditions.checkNotNull(rol, "El rol no puede ser nulo");
        return usuarioRepository.findByRol(rol);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        Preconditions.checkNotNull(id, "El ID del usuario no puede ser nulo");
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        Preconditions.checkArgument(StringUtils.isNotBlank(username),
                "El username no puede estar vacio");

        return usuarioRepository.findByUsername(username.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario", "username", username));
    }

    @Override
    @Transactional
    public Usuario crear(Usuario usuario, String passwordPlano) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());
        Preconditions.checkNotNull(usuario, "El usuario no puede ser nulo");
        Preconditions.checkArgument(StringUtils.isNotBlank(usuario.getUsername()),
                "El username es obligatorio");
        Preconditions.checkArgument(StringUtils.isNotBlank(passwordPlano),
                "El password es obligatorio para crear un usuario");
        Preconditions.checkArgument(passwordPlano.length() >= 6,
                "El password debe tener al menos 6 caracteres");

        // Normalizar username a minusculas
        usuario.setUsername(usuario.getUsername().trim().toLowerCase());

        // Validar username unico
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new BusinessException("Ya existe un usuario con username: "
                    + usuario.getUsername());
        }

        // Validar email unico (si se proporciono)
        if (StringUtils.isNotBlank(usuario.getEmail())
                && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Ya existe un usuario con email: "
                    + usuario.getEmail());
        }

        // ⭐ ENCRIPTAR EL PASSWORD CON BCRYPT
        String passwordEncriptado = passwordEncoder.encode(passwordPlano);
        usuario.setPasswordHash(passwordEncriptado);
        log.debug("Password encriptado con BCrypt para usuario: {}", usuario.getUsername());

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con ID: {} y rol: {}", guardado.getId(), guardado.getRol());
        return guardado;
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado, String passwordPlano) {
        log.info("Actualizando usuario ID: {}", id);
        Usuario existente = buscarPorId(id);

        existente.setNombreCompleto(usuarioActualizado.getNombreCompleto());
        existente.setEmail(usuarioActualizado.getEmail());
        existente.setRol(usuarioActualizado.getRol());

        // Si el password fue proporcionado, se actualiza
        if (StringUtils.isNotBlank(passwordPlano)) {
            Preconditions.checkArgument(passwordPlano.length() >= 6,
                    "El password debe tener al menos 6 caracteres");
            log.info("Actualizando password del usuario ID: {}", id);
            existente.setPasswordHash(passwordEncoder.encode(passwordPlano));
        }

        // Reactivar automaticamente al editar
        if (Boolean.FALSE.equals(existente.getActivo())) {
            log.info("Reactivando usuario ID: {}", id);
            existente.setActivo(true);
        }

        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Desactivando usuario ID: {}", id);
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}