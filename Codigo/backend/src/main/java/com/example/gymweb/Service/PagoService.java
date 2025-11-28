package com.example.gymweb.Service;

import com.example.gymweb.Repository.MembresiaRepository;
import com.example.gymweb.Repository.PlanRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.Repository.PagoRepository;
import com.example.gymweb.dto.Request.PagoRequest;
import com.example.gymweb.dto.Response.PagoResponse;
import com.example.gymweb.dto.Response.MembresiaResponse;
import com.example.gymweb.dto.Response.MercadoPagoPaymentInfo;
import com.example.gymweb.model.EstadoMembresia;
import com.example.gymweb.model.EstadoPago;
import com.example.gymweb.model.Membresia;
import com.example.gymweb.model.Plan;
import com.example.gymweb.model.Pago;
import com.example.gymweb.model.Usuario;
import com.example.gymweb.model.Estado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private MembresiaRepository membresiaRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MercadoPagoService mercadoPagoService;
    @Autowired
    private MembresiaService membresiaService;

    private PagoResponse convertirAResponse(Pago p) {
        PagoResponse res = new PagoResponse();
        res.setId(p.getId());
        res.setIdMembresia(p.getMembresia().getId());
        res.setMonto(p.getMonto());
        res.setEstado(p.getEstado().name());
        res.setComprobanteUrl(p.getComprobante_url());
        res.setFecha(p.getFecha());
        return res;
    }

    public PagoResponse registrarPago(PagoRequest request) {
        Membresia membresia = this.membresiaRepository.findById(request.getIdMembresia())
                .orElseThrow(() -> new RuntimeException("Membresia no encontrada"));
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        pago.setMonto(request.getMonto());
        pago.setComprobante_url(request.getComprobanteUrl());
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);
        if (membresia.getPlan() == null) {
            throw new RuntimeException("La membresia no tiene un plan asociado");
        }
        membresia.setEstado(EstadoMembresia.ACTIVA);
        LocalDateTime inicio = LocalDateTime.now();
        membresia.setFechaInicio(inicio);
        membresia.setFechaFin(this.membresiaService.calcularFechaFin(membresia.getPlan(), inicio));
        this.membresiaRepository.save(membresia);
        return this.convertirAResponse(pago);
    }

    public List<PagoResponse> listarPagosPorMembresia(int idMembresia) {
        return this.pagoRepository.findByMembresiaId(idMembresia).stream().map(this::convertirAResponse).toList();
    }

    public List<PagoResponse> listarPagosDeUsuario(int idUsuario) {
        return this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario)
                .stream()
                .flatMap((m) -> this.pagoRepository.findByMembresiaId(m.getId()).stream())
                .map(this::convertirAResponse)
                .toList();
    }

    public List<PagoResponse> listarTodos() {
        return this.pagoRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public MembresiaResponse procesarPagoMercadoPago(Long paymentId) {
        MercadoPagoPaymentInfo info = this.mercadoPagoService.obtenerPago(paymentId);
        if (!"approved".equalsIgnoreCase(info.getStatus())) {
            throw new RuntimeException("Pago no aprobado");
        }

        String externalRef = info.getExternalReference();
        if (externalRef == null || !externalRef.contains("user-") || !externalRef.contains("-plan-")) {
            throw new RuntimeException("Referencia externa invalida");
        }

        // Formato: user-{id}-plan-{code}, donde {code} puede incluir guiones
        Pattern refPattern = Pattern.compile("^user-(\\d+)-plan-(.+)$");
        Matcher matcher = refPattern.matcher(externalRef);
        if (!matcher.matches()) {
            throw new RuntimeException("Referencia externa invalida");
        }

        int userId = Integer.parseInt(matcher.group(1));
        String planCode = matcher.group(2);

        Usuario usuario = this.usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuario.getEstado() == null || Estado.PENDIENTE.equals(usuario.getEstado())) {
            usuario.setEstado(Estado.ACTIVO);
            this.usuarioRepository.save(usuario);
        }

        Plan plan = null;
        PlanInfo planInfo = null;
        if (planCode != null && planCode.matches("\\d+")) {
            int idPlan = Integer.parseInt(planCode);
            plan = this.planRepository.findById(idPlan).orElse(null);
        }
        if (plan == null) {
            planInfo = PLANES.get(planCode);
        }
        if (plan == null && planInfo == null) {
            planInfo = PLANES.get("mensual-full");
        }
        if (plan == null && planInfo != null) {
            final PlanInfo infoPlan = planInfo;
            plan = this.planRepository.findByNombreIgnoreCase(infoPlan.nombre())
                    .orElseGet(() -> {
                        Plan p = new Plan();
                        p.setNombre(infoPlan.nombre());
                        p.setPrecio(infoPlan.precio());
                        p.setPeriodo(infoPlan.periodo());
                        return this.planRepository.save(p);
                    });
        }
        if (plan == null) {
            throw new RuntimeException("Plan no encontrado");
        }
        if (planInfo != null) {
            boolean actualizar = false;
            if (plan.getPeriodo() == null || !plan.getPeriodo().equalsIgnoreCase(planInfo.periodo())) {
                plan.setPeriodo(planInfo.periodo());
                actualizar = true;
            }
            if (plan.getPrecio() == null) {
                plan.setPrecio(planInfo.precio());
                actualizar = true;
            }
            if (actualizar) {
                plan = this.planRepository.save(plan);
            }
        }

        // Buscar membresia vigente o ultima y extender segun plan elegido
        Membresia membresia = this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(userId)
                .orElse(new Membresia());
        membresia.setUsuario(usuario);
        membresia.setPlan(plan);
        LocalDateTime inicio = LocalDateTime.now();
        if (membresia.getFechaFin() != null && membresia.getFechaFin().isAfter(LocalDateTime.now())) {
            inicio = membresia.getFechaFin();
        }
        membresia.setFechaInicio(inicio);
        membresia.setFechaFin(this.membresiaService.calcularFechaFin(plan, inicio));
        membresia.setEstado(EstadoMembresia.ACTIVA);
        this.membresiaRepository.save(membresia);

        // Registrar pago
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        java.math.BigDecimal monto = info.getTransactionAmount();
        if (monto == null) {
            monto = plan.getPrecio();
            if (monto == null && planInfo != null) {
                monto = planInfo.precio();
            }
        }
        pago.setMonto(monto);
        pago.setComprobante_url("mercadopago:" + paymentId);
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);

        return this.membresiaService.obtenerMembresiaActualPorUsuario(userId);
    }

    private record PlanInfo(String nombre, java.math.BigDecimal precio, String periodo) {}

    private static final Map<String, PlanInfo> PLANES = Map.of(
            "dia", new PlanInfo("Plan por dia", new java.math.BigDecimal("10"), "DIARIO"),
            "mensual-3", new PlanInfo("Plan mensual - 3 dias", new java.math.BigDecimal("80"), "MENSUAL"),
            "mensual-full", new PlanInfo("Plan mensual - Full", new java.math.BigDecimal("120"), "MENSUAL")
    );
}
