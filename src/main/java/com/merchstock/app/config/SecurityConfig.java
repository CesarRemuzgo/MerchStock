package com.merchstock.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de Spring Security para MerchStock
 *
 * ESTADO ACTUAL: Acceso libre (sin login) para desarrollo del APF3.
 * Se permite acceso a todas las rutas sin autenticacion.
 *
 * NOTA: En la siguiente iteracion se configurara el login real con
 * BCrypt, control de roles (ADMIN/VENDEDOR) y bloqueo de rutas.
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean para encriptacion de contrasenas con BCrypt
     * Lo usaremos cuando implementemos el login real
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de filtros de seguridad - Configuracion temporal de desarrollo
     *
     * Permite acceso libre a todas las rutas
     * Deshabilita CSRF temporalmente (para que los formularios POST funcionen sin token)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF temporalmente (necesario porque aun no hay login con token)
            .csrf(AbstractHttpConfigurer::disable)
            // Permitir todas las rutas sin autenticacion
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Deshabilitar el login form por defecto de Spring Security
            .formLogin(AbstractHttpConfigurer::disable)
            // Deshabilitar la autenticacion HTTP basica
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}