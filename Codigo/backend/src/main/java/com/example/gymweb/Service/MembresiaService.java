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
import java.time.temporal.ChronoUnit;
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
        Usuario usuario = this.usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Plan plan = this.planRepository.findById(request.getIdPlan())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        Membresia membresia = new Membresia();
        membresia.setUsuario(usuario);
        membresia.setPlan(plan);
        LocalDateTime ahora = LocalDateTime.now();
        membresia.setFechaInicio(ahora);
        membresia.setFechaFin(this.calcularFechaFin(plan, ahora));
        membresia.setEstado(EstadoMembresia.ACTIVA);
        this.membresiaRepository.save(membresia);
        return this.convertirAResponse(membresia);
    }

    public MembresiaResponse obtenerMembresiaActualPorUsuario(int idUsuario) {
        Membresia m = this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario).orElse(null);
        if (m == null) {
            return null;
        }
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
        Membresia m = this.membresiaRepository.findById(idMembresia)
                .orElseThrow(() -> new RuntimeException("Membresia no encontrada"));
        m.setEstado(nuevoEstado);
        this.membresiaRepository.save(m);
    }

    /**
     * Calcula la fecha de finalizacion segun el periodo del plan elegido.
     * Si el plan no tiene periodo definido, se asume mensual por defecto.
     */
    public LocalDateTime calcularFechaFin(Plan plan, LocalDateTime fechaInicio) {
        if (plan == null) {
            throw new IllegalArgumentException("El plan es requerido para calcular la vigencia");
        }
        LocalDateTime inicio = fechaInicio != null ? fechaInicio : LocalDateTime.now();
        String periodo = plan.getPeriodo() != null ? plan.getPeriodo().trim().toUpperCase() : "";
        ChronoUnit unidad = switch (periodo) {
            case "DIARIO" -> ChronoUnit.DAYS;
            case "SEMANAL" -> ChronoUnit.WEEKS;
            case "ANUAL" -> ChronoUnit.YEARS;
            case "MENSUAL" -> ChronoUnit.MONTHS;
            default -> ChronoUnit.MONTHS;
        };
        return inicio.plus(1, unidad);
    }
}
