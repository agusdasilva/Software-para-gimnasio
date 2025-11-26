package com.example.gymweb.Service;

import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.UsuarioChangeEstadoRequest;
import com.example.gymweb.dto.Request.UsuarioChangeRolRequest;
import com.example.gymweb.dto.Request.UsuarioPerfilUpdateRequest;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private UsuarioResponse convertirAResponse(Usuario u) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(u.getId());
        response.setNombre(u.getNombre());
        response.setEmail(u.getEmail());
        response.setRol(u.getRol());
        response.setEstado(u.getEstado());
        response.setFechaAlta(u.getFechaAlta());
        return response;
    }

    public UsuarioResponse buscarPorId(Integer id) {
        Usuario u = (Usuario)this.usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return this.convertirAResponse(u);
    }

    public List<UsuarioResponse> listarTodos() {
        return (List)this.usuarioRepository.findAll().stream().map(this::convertirAResponse).collect(Collectors.toList());
    }

    public UsuarioResponse actualizarPerfil(Integer idUsuario, UsuarioPerfilUpdateRequest request) {
        Usuario u = (Usuario)this.usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (request.getNombre() != null) {
            u.setNombre(request.getNombre());
        }

        this.usuarioRepository.save(u);
        return this.convertirAResponse(u);
    }

    public UsuarioResponse cambiarRol(UsuarioChangeRolRequest request) {
        Usuario u = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setRol(request.getNuevoRol());
        this.usuarioRepository.save(u);
        return this.convertirAResponse(u);
    }

    public UsuarioResponse cambiarEstado(UsuarioChangeEstadoRequest request) {
        Usuario u = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setEstado(request.getNuevoEstado());
        this.usuarioRepository.save(u);
        return this.convertirAResponse(u);
    }
}

