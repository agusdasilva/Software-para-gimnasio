package com.example.gymweb.Auth;

import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.UsuarioLoginRequest;
import com.example.gymweb.dto.Request.UsuarioRegisterRequest;
import com.example.gymweb.dto.Response.AuthResponse;
import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;
import com.example.gymweb.model.Usuario;

import java.util.Date;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(UsuarioRegisterRequest request) {
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
    }
}
