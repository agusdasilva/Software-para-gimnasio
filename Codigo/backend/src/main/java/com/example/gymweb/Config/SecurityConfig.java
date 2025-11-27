package com.example.gymweb.Config;

import com.example.gymweb.Auth.JwtAuthenticationFilter;
import com.example.gymweb.Auth.JwtService;
import com.example.gymweb.Repository.UsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
                            .requestMatchers(HttpMethod.POST, "/api/pagos/mercadopago/confirmar").permitAll()
                            .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Desarrollo: habilitamos cualquier origen (útil si corres el front en otro puerto)
        config.setAllowedOriginPatterns(java.util.List.of("*"));
        config.setAllowedMethods(java.util.List.of("*"));
        config.setAllowedHeaders(java.util.List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
