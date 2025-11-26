package com.example.gymweb.Auth;

<<<<<<< Updated upstream
import com.example.gymweb.Auth.JwtService;
=======
import com.example.gymweb.Repository.UsuarioRepository;
>>>>>>> Stashed changes
import com.example.gymweb.dto.Request.UsuarioLoginRequest;
import com.example.gymweb.dto.Request.UsuarioRegisterRequest;
import com.example.gymweb.dto.Response.AuthResponse;
import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;
import com.example.gymweb.model.Usuario;
<<<<<<< Updated upstream
import com.example.gymweb.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {

=======

import java.util.Date;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
>>>>>>> Stashed changes
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

<<<<<<< Updated upstream
    public AuthenticationService(UsuarioRepository usuarioRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService) {
=======
    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
>>>>>>> Stashed changes
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(UsuarioRegisterRequest request) {
<<<<<<< Updated upstream

        if (usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword_hash(passwordEncoder.encode(request.getPassword()));
        usuario.setFechaAlta(new Date());
        usuario.setEstado(Estado.PENDIENTE);

        Rol rol = request.getRol() != null ? request.getRol() : Rol.CLIENTE;
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        // 👉 generamos token usando el email
        String token = jwtService.generarToken(usuario.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol());

        return response;
    }

    public AuthResponse login(UsuarioLoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword_hash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (usuario.getEstado() == Estado.BLOQUEADO) {
            throw new RuntimeException("Usuario bloqueado");
        }

        // 👉 de nuevo, token con el email
        String token = jwtService.generarToken(usuario.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol());

        return response;
    }

    public void activarCuentaPorMembresia(Integer idUsuario, String codigoMembresia) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (codigoMembresia == null || codigoMembresia.isBlank()) {
            throw new RuntimeException("Código inválido");
        }

        usuario.setEstado(Estado.ACTIVO);
        usuarioRepository.save(usuario);
=======
        if (this.usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        } else {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setPassword_hash(this.passwordEncoder.encode(request.getPassword()));
            usuario.setFechaAlta(new Date());
            usuario.setEstado(Estado.PENDIENTE);
            Rol rol = request.getRol() != null ? request.getRol() : Rol.CLIENTE;
            usuario.setRol(rol);
            this.usuarioRepository.save(usuario);
            String token = this.jwtService.generarToken(usuario.getEmail());
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setId(usuario.getId());
            response.setEmail(usuario.getEmail());
            response.setRol(usuario.getRol());
            return response;
        }
    }

    public AuthResponse login(UsuarioLoginRequest request) {
        Usuario usuario = (Usuario)this.usuarioRepository.findByEmailIgnoreCase(request.getEmail()).orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        if (!this.passwordEncoder.matches(request.getPassword(), usuario.getPassword_hash())) {
            throw new RuntimeException("Credenciales inválidas");
        } else if (usuario.getEstado() == Estado.BLOQUEADO) {
            throw new RuntimeException("Usuario bloqueado");
        } else {
            String token = this.jwtService.generarToken(usuario.getEmail());
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setId(usuario.getId());
            response.setEmail(usuario.getEmail());
            response.setRol(usuario.getRol());
            return response;
        }
    }

    public void activarCuentaPorMembresia(Integer idUsuario, String codigoMembresia) {
        Usuario usuario = (Usuario)this.usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (codigoMembresia != null && !codigoMembresia.isBlank()) {
            usuario.setEstado(Estado.ACTIVO);
            this.usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Código inválido");
        }
>>>>>>> Stashed changes
    }
}
