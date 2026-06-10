package com.merchstock.app.service.impl;

import com.merchstock.app.entity.Usuario;
import com.merchstock.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Servicio que conecta la entidad Usuario de MerchStock con Spring Security.
 *
 * Cuando un usuario intenta iniciar sesion, Spring Security llama a
 * loadUserByUsername() pasandole el username del formulario. Esta clase:
 *  1. Busca el Usuario en MySQL via UsuarioRepository
 *  2. Verifica que este activo
 *  3. Construye un UserDetails con username, passwordHash (BCrypt) y autoridades
 *  4. Lo retorna a Spring Security
 *
 * Spring Security luego compara el password ingresado contra el hash
 * almacenado usando BCryptPasswordEncoder. Si coinciden, el login es exitoso.
 *
 * IMPORTANTE: Nunca se desencripta el password. BCrypt es one-way.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando autenticar al usuario: {}", username);

        // Buscar el usuario por username
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Intento de login fallido. Usuario no existe: {}", username);
                    return new UsernameNotFoundException(
                            "Usuario no encontrado: " + username);
                });

        // Validar que el usuario este activo
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            log.warn("Intento de login con usuario inactivo: {}", username);
            throw new UsernameNotFoundException(
                    "El usuario " + username + " esta inactivo");
        }

        // Construir las autoridades (roles) - Spring Security requiere prefijo "ROLE_"
        List<GrantedAuthority> autoridades = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );

        log.info("Usuario autenticado: {} con rol: {}", username, usuario.getRol());

        // Retornar el UserDetails que Spring Security entiende
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPasswordHash())  // Hash BCrypt
                .authorities(autoridades)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
}