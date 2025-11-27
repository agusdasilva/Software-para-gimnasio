package com.example.gymweb.Service;

import com.example.gymweb.Repository.NotificacionRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.NotificacionRequest;
import com.example.gymweb.dto.Response.NotificacionResponse;
import com.example.gymweb.model.Notificacion;
import com.example.gymweb.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository repo;
    @Autowired
    private UsuarioRepository usuarioRepo;

    public List<NotificacionResponse> listar(int idUsuario) {
        return this.repo.findByUsuario_IdOrderByFechaDesc(idUsuario).stream().map((n) -> {
            NotificacionResponse response = new NotificacionResponse();
            response.setId(n.getId());
            response.setMensaje(n.getMensaje());
            response.setLeida(n.isLeida());
            response.setFecha(n.getFecha());
            return response;
        }).toList();
    }

    public NotificacionResponse crear(NotificacionRequest req) {
        Usuario u = (Usuario)this.usuarioRepo.findById(req.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Notificacion n = new Notificacion();
        n.setUsuario(u);
        n.setMensaje(req.getMensaje());
        n.setFecha(LocalDateTime.now());
        this.repo.save(n);
        NotificacionResponse response = new NotificacionResponse();
        response.setId(n.getId());
        response.setMensaje(n.getMensaje());
        response.setLeida(n.isLeida());
        response.setFecha(n.getFecha());
        return response;
    }

    public String marcarLeida(int id) {
        Notificacion n = (Notificacion)this.repo.findById(id).orElseThrow(() -> new RuntimeException("No existe notificación"));
        n.setLeida(true);
        this.repo.save(n);
        return "Notificación marcada como leída";
    }

    public void eliminar(int id) {
        this.repo.deleteById(id);
    }
}
