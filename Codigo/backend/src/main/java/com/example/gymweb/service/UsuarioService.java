package com.example.gymweb.service;

import com.example.gymweb.dto.Request.UsuarioChangeEstadoRequest;
import com.example.gymweb.dto.Request.UsuarioChangeRolRequest;
import com.example.gymweb.dto.Request.UsuarioPerfilUpdateRequest;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.model.Usuario;
import com.example.gymweb.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirAResponse(u);
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse actualizarPerfil(Integer idUsuario, UsuarioPerfilUpdateRequest request) {

        Usuario u = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getNombre() != null) {
            u.setNombre(request.getNombre());
        }

        // TODO: acá podés actualizar la entidad PerfilUsuario si la tenés modelada.
        // De momento sólo actualizamos el nombre como demo.

        usuarioRepository.save(u);
        return convertirAResponse(u);
    }

    public UsuarioResponse cambiarRol(UsuarioChangeRolRequest request) {

        Usuario u = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setRol(request.getNuevoRol());
        usuarioRepository.save(u);

        return convertirAResponse(u);
    }

    public UsuarioResponse cambiarEstado(UsuarioChangeEstadoRequest request) {

        Usuario u = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setEstado(request.getNuevoEstado());
        usuarioRepository.save(u);

        return convertirAResponse(u);
    }
}
