package com.example.gymweb.Config;

<<<<<<< Updated upstream
import com.example.gymweb.Auth.JwtAuthenticationFilter;
import com.example.gymweb.Auth.JwtService;
import com.example.gymweb.repository.UsuarioRepository;
=======

import com.example.gymweb.Auth.JwtAuthenticationFilter;
import com.example.gymweb.Auth.JwtService;
import com.example.gymweb.Repository.UsuarioRepository;

>>>>>>> Stashed changes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
<<<<<<< Updated upstream
=======
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
>>>>>>> Stashed changes
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
<<<<<<< Updated upstream

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, usuarioRepository);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Todo lo demás requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
=======
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, usuarioRepository);
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((auth) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.requestMatchers(new String[]{"/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**"})).permitAll().anyRequest()).authenticated()).formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
>>>>>>> Stashed changes
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
