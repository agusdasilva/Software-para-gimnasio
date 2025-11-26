package com.example.gymweb.Service;

import com.example.gymweb.Repository.MembresiaRepository;
import com.example.gymweb.Repository.PlanRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.MembresiaRequest;
import com.example.gymweb.dto.Response.MembresiaResponse;
import com.example.gymweb.model.EstadoMembresia;
import com.example.gymweb.model.Membresia;
import com.example.gymweb.model.Plan;
import com.example.gymweb.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembresiaService {
    @Autowired
    private MembresiaRepository membresiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlanRepository planRepository;

    public MembresiaService(MembresiaRepository membresiaRepository, UsuarioRepository usuarioRepository, PlanRepository planRepository) {
        this.membresiaRepository = membresiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    private MembresiaResponse convertirAResponse(Membresia m) {
        MembresiaResponse res = new MembresiaResponse();
        res.setId(m.getId());
        res.setIdUsuario(m.getUsuario().getId());
        res.setNombreUsuario(m.getUsuario().getNombre());
        res.setIdPlan(m.getPlan().getId());
        res.setNombrePlan(m.getPlan().getNombre());
        res.setEstado(m.getEstado().name());
        res.setFechaInicio(m.getFechaInicio());
        res.setFechaFin(m.getFechaFin());
        return res;
    }

    public MembresiaResponse crearMembresia(MembresiaRequest request) {
        Usuario usuario = (Usuario)this.usuarioRepository.findById(request.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Plan plan = (Plan)this.planRepository.findById(request.getIdPlan()).orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        Membresia membresia = new Membresia();
        membresia.setUsuario(usuario);
        membresia.setPlan(plan);
        LocalDateTime ahora = LocalDateTime.now();
        membresia.setFechaInicio(ahora);
        membresia.setFechaFin(ahora.plusMonths(1L));
        membresia.setEstado(EstadoMembresia.ACTIVA);
        this.membresiaRepository.save(membresia);
        return this.convertirAResponse(membresia);
    }

    public MembresiaResponse obtenerMembresiaActualPorUsuario(int idUsuario) {
        Membresia m = (Membresia)this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario).orElseThrow(() -> new RuntimeException("El usuario no tiene membresías"));
        if (m.getFechaFin() != null && m.getFechaFin().isBefore(LocalDateTime.now())) {
            m.setEstado(EstadoMembresia.VENCIDA);
            this.membresiaRepository.save(m);
        }

        return this.convertirAResponse(m);
    }

    public List<MembresiaResponse> listarMembresiasPorUsuario(int idUsuario) {
        return this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario).stream().map(this::convertirAResponse).toList();
    }

    public List<MembresiaResponse> listarTodas() {
        return this.membresiaRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public void cambiarEstado(int idMembresia, EstadoMembresia nuevoEstado) {
        Membresia m = (Membresia)this.membresiaRepository.findById(idMembresia).orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        m.setEstado(nuevoEstado);
        this.membresiaRepository.save(m);
    }
}

