package com.example.gymweb.Service;

import com.example.gymweb.Repository.PerfilUsuarioRepository;
import com.example.gymweb.Repository.MembresiaRepository;
import com.example.gymweb.Service.NotificacionService;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.UsuarioChangeEstadoRequest;
import com.example.gymweb.dto.Request.UsuarioChangeRolRequest;
import com.example.gymweb.dto.Request.UsuarioPerfilUpdateRequest;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.model.PerfilUsuario;
import com.example.gymweb.model.Membresia;
import com.example.gymweb.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final MembresiaRepository membresiaRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificacionService notificacionService;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilUsuarioRepository perfilUsuarioRepository,
                          MembresiaRepository membresiaRepository, PasswordEncoder passwordEncoder,
                          NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.membresiaRepository = membresiaRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificacionService = notificacionService;
    }

    private UsuarioResponse convertirAResponse(Usuario u) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(u.getId());
        response.setNombre(u.getNombre());
        response.setEmail(u.getEmail());
        response.setRol(u.getRol());
        response.setEstado(u.getEstado());
        response.setFechaAlta(u.getFechaAlta());
        Optional<PerfilUsuario> perfilOpt = perfilUsuarioRepository.findByUsuarioId(u.getId());
        perfilOpt.ifPresent(perfil -> {
            response.setDescripcion(perfil.getDescripcion());
            response.setFotoUrl(perfil.getFoto_url());
            response.setTelefono(perfil.getTelefono());
        });
        // Completar estado de membresia solo para clientes
        Optional<Membresia> membresiaOpt = membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(u.getId());
        if (membresiaOpt.isPresent()) {
            Membresia m = membresiaOpt.get();
            boolean vigentePorFecha = m.getFechaFin() != null && m.getFechaFin().isAfter(LocalDateTime.now());
            boolean activaPorEstado = m.getEstado() != null && m.getEstado().name().equalsIgnoreCase("ACTIVA");
            response.setMiembroActivo(vigentePorFecha && activaPorEstado);
            response.setEstadoMembresia(m.getEstado() != null ? m.getEstado().name() : (vigentePorFecha ? "ACTIVA" : "VENCIDA"));
            response.setNombrePlan(m.getPlan() != null ? m.getPlan().getNombre() : null);
        } else {
            response.setMiembroActivo(false);
            response.setEstadoMembresia(null);
        }
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
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String nuevoEmail = request.getEmail().trim();
            Optional<Usuario> existente = this.usuarioRepository.findByEmailIgnoreCase(nuevoEmail);
            if (existente.isPresent() && existente.get().getId() != u.getId()) {
                throw new RuntimeException("Ya existe un usuario con ese email");
            }
            u.setEmail(nuevoEmail);
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPassword_hash(this.passwordEncoder.encode(request.getPassword()));
        }

        this.usuarioRepository.save(u);

        PerfilUsuario perfil = this.perfilUsuarioRepository.findByUsuarioId(u.getId())
                .orElseGet(() -> {
                    PerfilUsuario nuevo = new PerfilUsuario();
                    nuevo.setUsuario(u);
                    return nuevo;
                });
        // Aseguramos que la relación siempre quede seteada (por si viniera null en un perfil existente)
        if (perfil.getUsuario() == null) {
            perfil.setUsuario(u);
        }

        if (request.getDescripcion() != null) {
            perfil.setDescripcion(request.getDescripcion());
        }
        if (request.getFotoUrl() != null) {
            perfil.setFoto_url(request.getFotoUrl());
        }
        if (request.getTelefono() != null) {
            perfil.setTelefono(request.getTelefono());
        }

        this.perfilUsuarioRepository.save(perfil);
        return this.convertirAResponse(u);
    }

    public UsuarioResponse cambiarRol(UsuarioChangeRolRequest request) {
        Usuario u = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setRol(request.getNuevoRol());
        this.usuarioRepository.save(u);
        // Notificar ascenso a entrenador
        if (request.getNuevoRol() == com.example.gymweb.model.Rol.ENTRENADOR) {
            this.notificacionService.crear(new com.example.gymweb.dto.Request.NotificacionRequest() {{
                setIdUsuario(u.getId());
                setMensaje("Fuiste ascendido a entrenador. Ya puedes crear y gestionar clases.");
            }});
        }
        return this.convertirAResponse(u);
    }

    public UsuarioResponse cambiarEstado(UsuarioChangeEstadoRequest request) {
        Usuario u = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setEstado(request.getNuevoEstado());
        this.usuarioRepository.save(u);
        return this.convertirAResponse(u);
    }
}

