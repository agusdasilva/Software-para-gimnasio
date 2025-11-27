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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
        Membresia membresia = (Membresia)this.membresiaRepository.findById(request.getIdMembresia()).orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        pago.setMonto(request.getMonto());
        pago.setComprobante_url(request.getComprobanteUrl());
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);
        membresia.setEstado(EstadoMembresia.ACTIVA);
        membresia.setFechaInicio(LocalDateTime.now());
        membresia.setFechaFin(LocalDateTime.now().plusMonths(1L));
        this.membresiaRepository.save(membresia);
        return this.convertirAResponse(pago);
    }

    public List<PagoResponse> listarPagosPorMembresia(int idMembresia) {
        return this.pagoRepository.findByMembresiaId(idMembresia).stream().map(this::convertirAResponse).toList();
    }

    public List<PagoResponse> listarPagosDeUsuario(int idUsuario) {
        return this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario).stream().flatMap((m) -> this.pagoRepository.findByMembresiaId(m.getId()).stream()).map(this::convertirAResponse).toList();
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
            throw new RuntimeException("Referencia externa inválida");
        }

        // Formato: user-{id}-plan-{code}
        String[] parts = externalRef.split("-");
        int userId;
        String planCode;
        try {
            userId = Integer.parseInt(parts[1]);
            planCode = parts[3];
        } catch (Exception e) {
            throw new RuntimeException("Referencia externa inválida");
        }

        Usuario usuario = this.usuarioRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PlanInfo planInfo = PLANES.getOrDefault(planCode, PLANES.get("mensual-full"));
        Plan plan = this.planRepository.findByNombreIgnoreCase(planInfo.nombre())
                .orElseGet(() -> {
                    Plan p = new Plan();
                    p.setNombre(planInfo.nombre());
                    p.setPrecio(planInfo.precio());
                    p.setPeriodo("MENSUAL");
                    return this.planRepository.save(p);
                });

        // Buscar membresía vigente o última
        Membresia membresia = this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(userId).orElse(new Membresia());
        membresia.setUsuario(usuario);
        membresia.setPlan(plan);
        LocalDateTime inicio = LocalDateTime.now();
        if (membresia.getFechaFin() != null && membresia.getFechaFin().isAfter(LocalDateTime.now())) {
            inicio = membresia.getFechaFin();
        }
        membresia.setFechaInicio(inicio);
        membresia.setFechaFin(inicio.plusMonths(1));
        membresia.setEstado(EstadoMembresia.ACTIVA);
        this.membresiaRepository.save(membresia);

        // Registrar pago
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        pago.setMonto(info.getTransactionAmount() != null ? info.getTransactionAmount() : planInfo.precio());
        pago.setComprobante_url("mercadopago:" + paymentId);
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);

        return this.membresiaService.obtenerMembresiaActualPorUsuario(userId);
    }

    private record PlanInfo(String nombre, java.math.BigDecimal precio) {}

    private static final Map<String, PlanInfo> PLANES = Map.of(
            "dia", new PlanInfo("Plan por dia", new java.math.BigDecimal("10")),
            "mensual-3", new PlanInfo("Plan mensual - 3 dias", new java.math.BigDecimal("80")),
            "mensual-full", new PlanInfo("Plan mensual - Full", new java.math.BigDecimal("120"))
    );
}
