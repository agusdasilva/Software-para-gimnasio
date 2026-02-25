package com.example.gymweb.Service;

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
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private RutinaService rutinaService;

    public ClaseService(ClaseRepository claseRepository, UsuarioRepository usuarioRepository, UsuarioXClaseRepository usuarioXClaseRepository, InvitacionClaseRepository invitacionClaseRepository, RutinaRepository rutinaRepository, MensajeClaseRepository mensajeClaseRepository) {
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioXClaseRepository = usuarioXClaseRepository;
        this.invitacionClaseRepository = invitacionClaseRepository;
        this.rutinaRepository = rutinaRepository;
        this.mensajeClaseRepository = mensajeClaseRepository;
    }

    private Usuario currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        return (Usuario) auth.getPrincipal();
    }

    private void assertStaff(Usuario user) {
        if (user.getRol() != Rol.ADMIN && user.getRol() != Rol.ENTRENADOR) {
            throw new RuntimeException("Solo entrenadores o administradores pueden realizar esta acciÃ³n");
        }
    }

    public ClaseResponse crearClase(ClaseRequest request) {
        Usuario creador = currentUser();
        assertStaff(creador);
        Clase clase = new Clase();
        clase.setCreador(creador);
        clase.setTitulo(request.getTitulo());
        clase.setDescripcion(request.getDescripcion());
        clase.setCupo(request.getCupo());
        this.claseRepository.save(clase);
        return this.convertirAResponse(clase);
    }

    private ClaseResponse convertirAResponse(Clase clase) {
        ClaseResponse res = new ClaseResponse();
        res.setCreadorId(clase.getCreador().getId());
        res.setCreadorNombre(clase.getCreador().getNombre());
        res.setId(clase.getId());
        res.setTitulo(clase.getTitulo());
        res.setDescripcion(clase.getDescripcion());
        res.setCupo(clase.getCupo());
        int ocupados = 0;
        if (clase.getUsuarios() != null) {
            ocupados = (int) clase.getUsuarios().stream().filter(UsuarioXClase::isAprobado).count();
        }
        res.setOcupados(ocupados);
        return res;
    }

    public String invitarUsuario(int idClase, int idUsuario) {
        Usuario solicitante = currentUser();
        assertStaff(solicitante);
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        if (!Objects.equals(clase.getCreador().getId(), solicitante.getId()) && solicitante.getRol() != Rol.ADMIN) {
            throw new RuntimeException("Solo el creador o un administrador pueden invitar usuarios");
        }
        Usuario usuario = this.usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (this.usuarioXClaseRepository.existsByUsuarioIdAndClaseId(idUsuario, idClase)) {
            throw new RuntimeException("Ese usuario ya estÃ¡ en la clase");
        } else {
            UsuarioXClase relacion = new UsuarioXClase();
            relacion.setUsuario(usuario);
            relacion.setClase(clase);
            this.usuarioXClaseRepository.save(relacion);
            InvitacionClase invit = new InvitacionClase();
            invit.setUsuarioClase(relacion);
            invit.setEstado(EstadoInvitacion.PENDIENTE);
            invit.setFecha(LocalDateTime.now());
            this.invitacionClaseRepository.save(invit);
            return "InvitaciÃ³n enviada";
        }
    }

    public String solicitarUnirse(int idClase) {
        Usuario solicitante = currentUser();
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        UsuarioXClase existente = this.usuarioXClaseRepository.findByUsuarioIdAndClaseId(solicitante.getId(), idClase).orElse(null);
        if (existente != null) {
            if (existente.isAprobado()) {
                return "Ya estÃ¡s en la clase";
            }
            InvitacionClase invExistente = this.invitacionClaseRepository.findByUsuarioClase_Id(existente.getId()).orElse(null);
            if (invExistente == null) {
                InvitacionClase invit = new InvitacionClase();
                invit.setUsuarioClase(existente);
                invit.setEstado(EstadoInvitacion.PENDIENTE);
                invit.setFecha(LocalDateTime.now());
                this.invitacionClaseRepository.save(invit);
            }
            return "Solicitud ya enviada";
        }
        UsuarioXClase relacion = new UsuarioXClase();
        relacion.setUsuario(solicitante);
        relacion.setClase(clase);
        this.usuarioXClaseRepository.save(relacion);
        InvitacionClase invit = new InvitacionClase();
        invit.setUsuarioClase(relacion);
            invit.setEstado(EstadoInvitacion.PENDIENTE);
            invit.setFecha(LocalDateTime.now());
            this.invitacionClaseRepository.save(invit);
        return "Solicitud enviada";
    }

    public String responderInvitacion(int idInvitacion, boolean aceptar) {
        Usuario usuarioActual = currentUser();
        InvitacionClase invit = this.invitacionClaseRepository.findById(idInvitacion).orElseThrow(() -> new RuntimeException("InvitaciÃ³n no encontrada"));
        boolean esDueno = Objects.equals(invit.getUsuarioClase().getUsuario().getId(), usuarioActual.getId());
        boolean esStaff = usuarioActual.getRol() == Rol.ADMIN || usuarioActual.getRol() == Rol.ENTRENADOR;
        if (!esDueno && !esStaff) {
            throw new RuntimeException("No puedes responder invitaciones de otros usuarios");
        }
        invit.setEstado(aceptar ? EstadoInvitacion.ACEPTADA : EstadoInvitacion.RECHAZADA);
        this.invitacionClaseRepository.save(invit);
        if (!aceptar) {
            this.usuarioXClaseRepository.delete(invit.getUsuarioClase());
            return "InvitaciÃ³n rechazada";
        } else {
            UsuarioXClase rel = invit.getUsuarioClase();
            rel.setAprobado(true);
            this.usuarioXClaseRepository.save(rel);
            return "InvitaciÃ³n aceptada";
        }
    }

    public List<ClaseResponse> listar() {
        return this.claseRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public ClaseResponse buscarPorId(int idClase) {
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        return this.convertirAResponse(clase);
    }

    public List<UsuarioResponse> listarMiembros(int idClase) {
        return this.usuarioXClaseRepository.findByClaseId(idClase).stream()
                .filter(UsuarioXClase::isAprobado)
                .map((uxc) -> this.convertirUsuarioAResponse(uxc.getUsuario()))
                .toList();
    }

    public String agregarRutina(int idClase, int idRutina) {
        Usuario solicitante = currentUser();
        assertStaff(solicitante);
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no existe"));
        // Entrenadores y admins pueden asignar rutinas a clases
        Rutina rutina = this.rutinaRepository.findById(idRutina).orElseThrow(() -> new RuntimeException("Rutina no existe"));
        clase.getRutinas().add(rutina);
        this.claseRepository.save(clase);
        return "Rutina asignada correctamente";
    }

    public com.example.gymweb.dto.Response.RutinaResponse guardarRutinaDeClase(int idClase, int idRutina) {
        Usuario usuario = currentUser();
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no existe"));
        boolean esMiembro = this.usuarioXClaseRepository.existsByUsuarioIdAndClaseId(usuario.getId(), idClase);
        boolean esStaff = usuario.getRol() == Rol.ADMIN || usuario.getRol() == Rol.ENTRENADOR;
        if (!esMiembro && !esStaff) {
            throw new RuntimeException("No puedes guardar rutinas de una clase en la que no estÃ¡s");
        }
        boolean asignada = clase.getRutinas().stream().anyMatch(r -> r.getId() == idRutina);
        if (!asignada) {
            throw new RuntimeException("La rutina no estÃ¡ asignada a esta clase");
        }
        return this.rutinaService.clonarParaUsuario(idRutina, usuario.getId());
    }

    public List<com.example.gymweb.dto.Response.RutinaResponse> listarRutinasClase(int idClase) {
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no existe"));
        return clase.getRutinas().stream().map(this::convertirRutinaAResponse).toList();
    }

    private com.example.gymweb.dto.Response.RutinaResponse convertirRutinaAResponse(Rutina rutina) {
        com.example.gymweb.dto.Response.RutinaResponse response = new com.example.gymweb.dto.Response.RutinaResponse();
        response.setId(rutina.getId());
        response.setNombre(rutina.getNombre());
        response.setCreador(rutina.getCreador() != null ? rutina.getCreador().getNombre() : "Sin datos");
        response.setEsGlobal(rutina.isEsGlobal());
        if (rutina.getRutinaDetalle() != null) {
            com.example.gymweb.dto.Response.RutinaDetalleResponse det = new com.example.gymweb.dto.Response.RutinaDetalleResponse();
            det.setId(rutina.getRutinaDetalle().getId());
            det.setRutina(rutina.getNombre());
            det.setDescanso_seg(rutina.getRutinaDetalle().getDescanso_seg());
            det.setDescripcion(rutina.getRutinaDetalle().getDescripcion());
            det.setImagen(rutina.getRutinaDetalle().getImagen());
            det.setEjercicios(new java.util.ArrayList<>());
            response.setDetalle(det);
        }
        return response;
    }

    public List<InvitacionResponse> listarInvitaciones(int idClase) {
        Usuario solicitante = currentUser();
        assertStaff(solicitante);
        return this.invitacionClaseRepository.findByUsuarioClase_Clase_Id(idClase).stream().map((inv) -> {
            InvitacionResponse response = new InvitacionResponse();
            response.setIdInvitacion(inv.getId());
            response.setEstado(inv.getEstado().name());
            response.setFecha(inv.getFecha());
            response.setIdUsuario(inv.getUsuarioClase().getUsuario().getId());
            response.setNombreUsuario(inv.getUsuarioClase().getUsuario().getNombre());
            response.setIdClase(inv.getUsuarioClase().getClase().getId());
            return response;
        }).toList();
    }

    public String enviarMensaje(int idClase, MensajeClaseRequest request) {
        Usuario autor = currentUser();
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        // Solo miembros o staff pueden enviar mensaje
        boolean esMiembro = this.usuarioXClaseRepository.existsByUsuarioIdAndClaseId(autor.getId(), idClase);
        if (!esMiembro && autor.getRol() == Rol.CLIENTE) {
            throw new RuntimeException("No puedes enviar mensajes a una clase en la que no estÃ¡s");
        }
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
        Usuario solicitante = currentUser();
        Clase clase = this.claseRepository.findById(id).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        if (!Objects.equals(clase.getCreador().getId(), solicitante.getId()) && solicitante.getRol() != Rol.ADMIN) {
            throw new RuntimeException("Solo el creador o un administrador pueden eliminar la clase");
        }
        this.claseRepository.deleteById(id);
    }

    public ClaseResponse editarClase(int idClase, ClaseRequest request) {
        Usuario solicitante = currentUser();
        Clase clase = this.claseRepository.findById(idClase).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        if (!Objects.equals(clase.getCreador().getId(), solicitante.getId()) && solicitante.getRol() != Rol.ADMIN) {
            throw new RuntimeException("Solo el creador o un administrador pueden editar la clase");
        }
        clase.setTitulo(request.getTitulo());
        clase.setDescripcion(request.getDescripcion());
        clase.setCupo(request.getCupo());
        this.claseRepository.save(clase);
        return this.convertirAResponse(clase);
    }
}




