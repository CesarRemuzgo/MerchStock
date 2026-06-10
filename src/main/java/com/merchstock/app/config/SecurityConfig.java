package com.merchstock.app.config;

import com.merchstock.app.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad de Spring Security para MerchStock.
 *
 * CARACTERISTICAS:
 * - Autenticacion via formulario (no Basic Auth)
 * - Passwords encriptados con BCrypt (cost-factor 10, estandar OWASP)
 * - Control de acceso basado en roles (RBAC):
 *   - ADMIN: acceso total al sistema
 *   - VENDEDOR: acceso a Productos (lectura), Ventas, Clientes, Reportes, Alertas
 *               sin permisos sobre Usuarios ni Categorias ni operaciones CRUD de Productos
 * - Sesion HTTP con cookies (sesion stateful)
 * - Recursos publicos: CSS, JS, imagenes, pagina de login y errores
 * - CSRF habilitado para proteger contra ataques de Cross-Site Request Forgery
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Bean para encriptar passwords con BCrypt.
     * Cost-factor 10 = 2^10 = 1024 iteraciones (estandar OWASP).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Authentication Provider que conecta el CustomUserDetailsService
     * con el PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Cadena de filtros de seguridad - aqui se define que rutas son publicas
     * y cuales requieren autenticacion / roles especificos.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando SecurityFilterChain con autenticacion habilitada");

        http
            // CSRF deshabilitado temporalmente para simplificar el desarrollo
            // En produccion: habilitar y usar tokens en los formularios
            .csrf(csrf -> csrf.disable())

            // Reglas de autorizacion por ruta
            .authorizeHttpRequests(auth -> auth
                // Recursos publicos (no requieren login)
                .requestMatchers(
                        "/login",
                        "/logout",
                        "/error",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/favicon.ico"
                ).permitAll()

                // Solo ADMIN: modulos sensibles
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/categorias/**").hasRole("ADMIN")

                // Solo ADMIN: operaciones de modificacion de productos
                .requestMatchers("/productos/nuevo", "/productos/editar/**",
                                 "/productos/eliminar/**").hasRole("ADMIN")

                // ADMIN y VENDEDOR: el resto del sistema
                .requestMatchers("/", "/productos", "/productos/alertas",
                                 "/ventas/**", "/clientes/**", "/reportes/**")
                        .hasAnyRole("ADMIN", "VENDEDOR")

                // Cualquier otra ruta requiere autenticacion
                .anyRequest().authenticated()
            )

            // Configuracion del login
            .formLogin(form -> form
                .loginPage("/login")              // GET /login muestra el form
                .loginProcessingUrl("/login")     // POST /login procesa el form
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)     // si OK, redirigir al home
                .failureUrl("/login?error=true")  // si falla, volver al login con error
                .permitAll()
            )

            // Configuracion del logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Manejo de acceso denegado (403)
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )

            // Configuracion de sesion
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?expired=true")
                .maximumSessions(1)               // 1 sesion por usuario
                .expiredUrl("/login?expired=true")
            )

            // Authentication provider personalizado
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}