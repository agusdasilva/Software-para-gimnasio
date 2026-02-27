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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PagoService {
    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
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
        log.info("Procesando pago MercadoPago paymentId={}", paymentId);
        MercadoPagoPaymentInfo info = this.mercadoPagoService.obtenerPago(paymentId);
        log.info("Estado MP status={} externalRef={} amount={}", info.getStatus(), info.getExternalReference(), info.getTransactionAmount());
        String status = info.getStatus() != null ? info.getStatus().toLowerCase() : "";
        if (!(status.equals("approved") || status.equals("authorized") || status.equals("in_process"))) {
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

        if (planCode == null || !planCode.matches("\\d+")) {
            // Intentar resolver por nombre (soportar referencias antiguas tipo "mensual-full")
            Plan planByName = resolverPlanPorNombre(planCode);
            if (planByName == null) {
                throw new RuntimeException("Plan invalido en referencia externa");
            }
            planCode = String.valueOf(planByName.getId());
        }
        int idPlan = Integer.parseInt(planCode);
        Plan plan = this.planRepository.findById(idPlan)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + idPlan));

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
        log.info("Membresia {} activada para usuario {} plan {} desde {} hasta {}",
                membresia.getId(), usuario.getId(), plan.getId(), membresia.getFechaInicio(), membresia.getFechaFin());

        // Registrar pago
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        java.math.BigDecimal monto = info.getTransactionAmount();
        if (monto == null) {
            monto = plan.getPrecio();
        }
        pago.setMonto(monto);
        pago.setComprobante_url("mercadopago:" + paymentId);
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);

        return this.membresiaService.obtenerMembresiaActualPorUsuario(userId);
    }

    /**
     * Reintenta activar la membresía buscando el ultimo pago por external_reference.
     * Se usa como fallback si el frontend no logro confirmar o el webhook no llego.
     */
    public MembresiaResponse reconciliarPagoPorUsuarioYPlan(int userId, String planCode) {
        if (planCode == null || !planCode.matches("\\d+")) {
            log.warn("Plan code invalido para reconciliar: {}", planCode);
            return null;
        }
        String extRef = "user-" + userId + "-plan-" + planCode;
        Long paymentId = this.mercadoPagoService.buscarPagoPorExternalReference(extRef);
        if (paymentId == null) {
            // Fallback: buscar el pago mas reciente del usuario (por si el plan code difiere o hubo cambios)
            paymentId = this.mercadoPagoService.buscarPagoMasRecientePorUsuario(userId);
            if (paymentId == null) {
                log.warn("No se encontro pago en MP con external_reference {} ni por prefijo user-{}-plan-*", extRef, userId);
                return null;
            }
            log.info("Fallback reconciliar: usando pago {} mas reciente para usuario {}", paymentId, userId);
        } else {
            log.info("Reconciliando pago {} para usuario {} plan {}", paymentId, userId, planCode);
        }
        return this.procesarPagoMercadoPago(paymentId);
    }

    private Plan resolverPlanPorNombre(String planCode) {
        if (planCode == null || planCode.isBlank()) return null;
        String normalized = planCode.toLowerCase().replace('-', ' ').trim();
        return this.planRepository.findAll()
                .stream()
                .filter(p -> {
                    String nombre = (p.getNombre() == null) ? "" : p.getNombre().toLowerCase();
                    return nombre.contains(normalized);
                })
                .findFirst()
                .orElse(null);
    }
}
