package com.example.gymweb.Service;

import com.example.gymweb.Repository.*;
import com.example.gymweb.Repository.ClaseRepository;
import com.example.gymweb.Repository.InvitacionClaseRepository;
import com.example.gymweb.Repository.MensajeClaseRepository;
import com.example.gymweb.Repository.RutinaRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.Repository.UsuarioXClaseRepository;
import com.example.gymweb.dto.Request.ClaseRequest;
import com.example.gymweb.dto.Request.MensajeClaseRequest;
import com.example.gymweb.dto.Response.ClaseResponse;
import com.example.gymweb.dto.Response.InvitacionResponse;
import com.example.gymweb.dto.Response.MensajeClaseResponse;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.model.Clase;
import com.example.gymweb.model.EstadoInvitacion;
import com.example.gymweb.model.InvitacionClase;
import com.example.gymweb.model.MensajeClase;
import com.example.gymweb.model.Rol;
import com.example.gymweb.model.Rutina;
import com.example.gymweb.model.Usuario;
import com.example.gymweb.model.UsuarioXClase;

import java.time.LocalDateTime;
import java.util.List;
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
    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private MensajeClaseRepository mensajeClaseRepository;

    public ClaseService(ClaseRepository claseRepository, UsuarioRepository usuarioRepository, UsuarioXClaseRepository usuarioXClaseRepository, InvitacionClaseRepository invitacionClaseRepository, RutinaRepository rutinaRepository, MensajeClaseRepository mensajeClaseRepository) {
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioXClaseRepository = usuarioXClaseRepository;
        this.invitacionClaseRepository = invitacionClaseRepository;
        this.rutinaRepository = rutinaRepository;
        this.mensajeClaseRepository = mensajeClaseRepository;
    }

    public ClaseResponse crearClase(ClaseRequest request) {
        Usuario creador = (Usuario)this.usuarioRepository.findById(request.getCreadorId()).orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        if (creador.getRol() != Rol.ENTRENADOR) {
            throw new RuntimeException("El usuario no es un entrenador");
        } else {
            Clase clase = new Clase();
            clase.setCreador(creador);
            clase.setTitulo(request.getTitulo());
            clase.setDescripcion(request.getDescripcion());
            clase.setCupo(request.getCupo());
            this.claseRepository.save(clase);
            return this.convertirAResponse(clase);
        }
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

    public String invitarUsuario(int idClase, int idUsuario) {
        Clase clase = (Clase)this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        Usuario usuario = (Usuario)this.usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (this.usuarioXClaseRepository.existsByUsuarioIdAndClaseId(idUsuario, idClase)) {
            throw new RuntimeException("Ese usuario ya está en la clase");
        } else {
            UsuarioXClase relacion = new UsuarioXClase();
            relacion.setUsuario(usuario);
            relacion.setClase(clase);
            this.usuarioXClaseRepository.save(relacion);
            InvitacionClase invit = new InvitacionClase();
            invit.setUsuarioClase(relacion);
            this.invitacionClaseRepository.save(invit);
            return "Invitación enviada";
        }
    }

    public String responderInvitacion(int idInvitacion, boolean aceptar) {
        InvitacionClase invit = (InvitacionClase)this.invitacionClaseRepository.findById(idInvitacion).orElseThrow(() -> new RuntimeException("Invitación no encontrada"));
        invit.setEstado(aceptar ? EstadoInvitacion.ACEPTADA : EstadoInvitacion.RECHAZADA);
        this.invitacionClaseRepository.save(invit);
        if (!aceptar) {
            this.usuarioXClaseRepository.delete(invit.getUsuarioClase());
            return "Invitación rechazada";
        } else {
            UsuarioXClase rel = invit.getUsuarioClase();
            rel.setAprobado(true);
            this.usuarioXClaseRepository.save(rel);
            return "Invitación aceptada";
        }
    }

    public List<ClaseResponse> listar() {
        return this.claseRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public ClaseResponse buscarPorId(int idClase) {
        Clase clase = (Clase)this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        return this.convertirAResponse(clase);
    }

    public List<UsuarioResponse> listarMiembros(int idClase) {
        return this.usuarioXClaseRepository.findByClaseId(idClase).stream().filter(UsuarioXClase::isAprobado).map((uxc) -> this.convertirUsuarioAResponse(uxc.getUsuario())).toList();
    }

    public String agregarRutina(int idClase, int idRutina) {
        Clase clase = (Clase)this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no existe"));
        Rutina rutina = (Rutina)this.rutinaRepository.findById(idRutina).orElseThrow(() -> new RuntimeException("Rutina no existe"));
        clase.getRutinas().add(rutina);
        this.claseRepository.save(clase);
        return "Rutina asignada correctamente";
    }

    public List<InvitacionResponse> listarInvitaciones(int idClase) {
        return this.invitacionClaseRepository.findById(idClase).stream().map((inv) -> {
            InvitacionResponse response = new InvitacionResponse();
            response.setIdInvitacion(inv.getId());
            response.setEstado(inv.getEstado().name());
            response.setFecha(inv.getFecha());
            response.setIdUsuario(inv.getUsuarioClase().getUsuario().getId());
            response.setIdClase(inv.getUsuarioClase().getClase().getId());
            return response;
        }).toList();
    }

    public String enviarMensaje(int idClase, MensajeClaseRequest request) {
        Clase clase = (Clase)this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        Usuario usuario = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        MensajeClase mensaje = new MensajeClase();
        mensaje.setClase(clase);
        mensaje.setMensaje(request.getMensaje());
        mensaje.setFecha(LocalDateTime.now());
        this.mensajeClaseRepository.save(mensaje);
        return "Mensaje enviado";
    }

    public List<MensajeClaseResponse> listarMensajes(int idClase) {
        return this.mensajeClaseRepository.findByClaseId(idClase).stream().map((m) -> {
            MensajeClaseResponse response = new MensajeClaseResponse();
            response.setIdMensaje(m.getId());
            response.setMensaje(m.getMensaje());
            response.setFecha(m.getFecha());
            return response;
        }).toList();
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

    public void eliminar(int id) {
        this.claseRepository.deleteById(id);
    }

    public ClaseResponse editarClase(int idClase, ClaseRequest request) {
        Clase clase = (Clase)this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        clase.setTitulo(request.getTitulo());
        clase.setDescripcion(request.getDescripcion());
        clase.setCupo(request.getCupo());
        this.claseRepository.save(clase);
        return this.convertirAResponse(clase);
    }
}
