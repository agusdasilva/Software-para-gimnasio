package com.example.gymweb.Config;

import com.example.gymweb.Auth.JwtAuthenticationFilter;
import com.example.gymweb.Auth.JwtService;
import com.example.gymweb.Repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(this.jwtService, this.usuarioRepository);
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((auth) ->
                        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/planes/**").permitAll()
                        // Confirmacion de pago: validamos token de webhook en el controlador
                            .requestMatchers(HttpMethod.POST, "/api/pagos/mercadopago/confirmar").permitAll()
                            // Administración general
                            .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/usuarios/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        // Aptos medicos
                            .requestMatchers("/api/aptos/pendientes", "/api/aptos/aprobados", "/api/aptos/*/aprobar", "/api/aptos/*/rechazar", "/api/aptos/*/cancelar").hasRole("ADMIN")
                            .requestMatchers("/api/aptos/**").authenticated()
                        // Perfil y obtencion de usuario: cualquier autenticado, control de dueno/rol en controlador
                            .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/perfil").authenticated()
                            .requestMatchers(HttpMethod.POST, "/api/planes/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/planes/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/planes/**").hasRole("ADMIN")
                            // Clases y rutinas
                            .requestMatchers(HttpMethod.POST, "/api/clases/*/rutinas/*/guardar").authenticated()
                            .requestMatchers(HttpMethod.POST, "/api/clases/*/solicitar").authenticated()
                            .requestMatchers(HttpMethod.POST, "/api/clases/**").hasAnyRole("ADMIN", "ENTRENADOR")
                            .requestMatchers(HttpMethod.PUT, "/api/clases/**").hasAnyRole("ADMIN", "ENTRENADOR")
                            .requestMatchers(HttpMethod.DELETE, "/api/clases/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/rutina/**").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/api/rutina/**").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/api/rutina/**").authenticated()
                            .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Limitar orígenes conocidos. Se puede extender con spring.web.cors.allowed-origins
        config.setAllowedOriginPatterns(java.util.List.of("http://localhost:4200"));
        config.setAllowedMethods(java.util.List.of("*"));
        config.setAllowedHeaders(java.util.List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
