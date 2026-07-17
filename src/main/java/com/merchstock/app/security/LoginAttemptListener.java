package com.merchstock.app.security;

import com.merchstock.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Escucha los eventos de autenticacion de Spring Security para implementar
 * el bloqueo de cuenta tras multiples intentos fallidos (proteccion contra
 * fuerza bruta).
 *
 * Politica: 5 intentos fallidos consecutivos bloquean la cuenta por 15 minutos.
 * El contador se reinicia automaticamente tras un login exitoso.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptListener {

    private static final int MAX_INTENTOS = 5;
    private static final long MINUTOS_BLOQUEO = 15;

    private final UsuarioRepository usuarioRepository;

    @EventListener
    @Transactional
    public void onLoginExitoso(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueadoHasta(null);
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
            log.debug("Login exitoso, contador de intentos reiniciado: {}", username);
        });
    }

    @EventListener
    @Transactional
    public void onCredencialesIncorrectas(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            int intentos = (usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos()) + 1;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= MAX_INTENTOS) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
                log.warn("Usuario {} bloqueado por {} intentos fallidos hasta {}",
                        username, intentos, usuario.getBloqueadoHasta());
            } else {
                log.warn("Intento fallido {}/{} para el usuario {}", intentos, MAX_INTENTOS, username);
            }

            usuarioRepository.save(usuario);
        });
    }

    @EventListener
    public void onIntentoConCuentaBloqueada(AuthenticationFailureLockedEvent event) {
        log.warn("Intento de login rechazado: cuenta bloqueada temporalmente: {}",
                event.getAuthentication().getName());
    }
}