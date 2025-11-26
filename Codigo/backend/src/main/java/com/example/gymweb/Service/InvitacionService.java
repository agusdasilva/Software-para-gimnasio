package com.example.gymweb.Service;

import com.example.gymweb.Repository.InvitacionClaseRepository;
import com.example.gymweb.Repository.UsuarioXClaseRepository;
import com.example.gymweb.dto.Response.InvitacionResponse;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.model.InvitacionClase;
import com.example.gymweb.model.Usuario;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitacionService {
    @Autowired
    private InvitacionClaseRepository repo;
    @Autowired
    private UsuarioXClaseRepository usuarioXClaseRepo;

    public InvitacionResponse buscarPorId(int id) {
        InvitacionClase i = (InvitacionClase)this.repo.findById(id).orElseThrow(() -> new RuntimeException("Invitación no encontrada"));
        return this.convertirAResponse(i);
    }

    public List<InvitacionResponse> listarPorUsuario(int idUsuario) {
        List<InvitacionClase> invitaciones = this.repo.findByUsuarioClase_Usuario_Id(idUsuario);
        return invitaciones.stream().map((i) -> this.convertirAResponse(i)).toList();
    }

    public InvitacionResponse convertirAResponse(InvitacionClase invitacionClase) {
        InvitacionResponse response = new InvitacionResponse();
        response.setEstado(invitacionClase.getEstado().name());
        response.setIdInvitacion(invitacionClase.getId());
        response.setIdUsuario(invitacionClase.getId());
        response.setFecha(invitacionClase.getFecha());
        response.setIdClase(invitacionClase.getUsuarioClase().getClase().getId());
        return response;
    }

    public UsuarioResponse convertirUsuarioAResponse(Usuario u) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(u.getId());
        response.setNombre(u.getNombre());
        response.setEmail(u.getEmail());
        response.setRol(u.getRol());
        response.setEstado(u.getEstado());
        response.setFechaAlta(u.getFechaAlta());
        return response;
    }
}
