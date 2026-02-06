package com.example.gymweb.Config;

import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;
import com.example.gymweb.model.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
public class DataInitializer {

    /**
     * Garantiza que exista un admin y con contraseña conocida (por defecto 123456).
     * Solo se ejecuta fuera del perfil de test.
     */
    @Bean
    @Profile("!test")
    public CommandLineRunner ensureAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@gym.com";
            String rawPass = "123456";
            Usuario admin = usuarioRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
                Usuario u = new Usuario();
                u.setNombre("Administrador del Sistema");
                u.setEmail(email);
                u.setFechaAlta(new Date());
                u.setEstado(Estado.ACTIVO);
                u.setRol(Rol.ADMIN);
                return u;
            });

            // Aseguramos estado y rol correctos
            admin.setEstado(Estado.ACTIVO);
            admin.setRol(Rol.ADMIN);

            // Reescribimos password siempre para evitar hashes inconsistentes
            admin.setPassword_hash(passwordEncoder.encode(rawPass));

            usuarioRepository.save(admin);
        };
    }
}
