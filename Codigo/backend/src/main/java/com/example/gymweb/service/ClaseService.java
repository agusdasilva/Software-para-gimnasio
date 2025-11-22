package com.example.gymweb.service;

import com.example.gymweb.dto.Request.ClaseRequest;
import com.example.gymweb.dto.Response.ClaseResponse;
import com.example.gymweb.model.*;
import com.example.gymweb.repository.ClaseRepository;
import com.example.gymweb.repository.InvitacionClaseRepository;
import com.example.gymweb.repository.UsuarioRepository;
import com.example.gymweb.repository.UsuarioXClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioXClaseRepository usuarioXClaseRepository;

    @Autowired
    private InvitacionClaseRepository invitacionClaseRepository;

    // Crear clase
    public ClaseResponse crearClase(ClaseRequest request) {

        Usuario creador = usuarioRepository.findById(request.getCreadorId())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));

        if(creador.getRol() != Rol.ENTRENADOR){
            throw new RuntimeException("El usuario no es un entrenador");
        }

        Clase clase = new Clase();
        clase.setCreador(creador);
        clase.setTitulo(request.getTitulo());
        clase.setDescripcion(request.getDescripcion());
        clase.setCupo(request.getCupo());

        claseRepository.save(clase);

        return convertirAResponse(clase);
    }

    private ClaseResponse convertirAResponse(Clase clase) {
        ClaseResponse res = new ClaseResponse();
        res.setCreadorId(clase.getCreador().getId());
        res.setCreadorNombre(clase.getCreador().getNombre());
        res.setId(clase.getId());
        res.setTitulo(clase.getTitulo());
        res.setDescripcion(clase.getDescripcion());
        res.setCupo(clase.getCupo());
        return res;
    }

    // Invitar usuario
    public String invitarUsuario(int idClase, int idUsuario) {

        Clase clase = claseRepository.findById(idClase)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioXClaseRepository.existsByUsuarioIdAndClaseId(idUsuario, idClase)) {
            throw new RuntimeException("Ese usuario ya está en la clase");
        }

        UsuarioXClase relacion = new UsuarioXClase();
        relacion.setUsuario(usuario);
        relacion.setClase(clase);
        usuarioXClaseRepository.save(relacion);

        InvitacionClase invit = new InvitacionClase();
        invit.setUsuarioClase(relacion);
        invitacionClaseRepository.save(invit);

        return "Invitación enviada";
    }

    // Aceptar / Rechazar invitación
    public String responderInvitacion(int idInvitacion, boolean aceptar) {

        InvitacionClase invit = invitacionClaseRepository.findById(idInvitacion)
                .orElseThrow(() -> new RuntimeException("Invitación no encontrada"));

        invit.setEstado(aceptar ? EstadoInvitacion.ACEPTADA : EstadoInvitacion.RECHAZADA);
        invitacionClaseRepository.save(invit);

        if (!aceptar) {
            usuarioXClaseRepository.delete(invit.getUsuarioClase());
            return "Invitación rechazada";
        }

        // marcar como aprobado
        UsuarioXClase rel = invit.getUsuarioClase();
        rel.setAprobado(true);
        usuarioXClaseRepository.save(rel);

        return "Invitación aceptada";
    }
}
