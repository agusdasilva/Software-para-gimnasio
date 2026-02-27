package com.example.gymweb.Controller;

import com.example.gymweb.Service.MercadoPagoService;
import com.example.gymweb.Service.PagoService;
import com.example.gymweb.dto.Request.PagoRequest;
import com.example.gymweb.dto.Response.MembresiaResponse;
import com.example.gymweb.dto.Response.MercadoPagoPreferenceResponse;
import com.example.gymweb.dto.Response.PagoResponse;
import com.example.gymweb.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/pagos"})
public class PagoController {
    private PagoService pagoService;
    private MercadoPagoService mercadoPagoService;
    @Value("${pagos.webhook-token:}")
    private String webhookToken;

    @Autowired
    public PagoController(PagoService pagoService, MercadoPagoService mercadoPagoService) {
        this.pagoService = pagoService;
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@RequestBody PagoRequest request) {
        return ResponseEntity.ok(this.pagoService.registrarPago(request));
    }

    @GetMapping({"/membresia/{idMembresia}"})
    public ResponseEntity<List<PagoResponse>> pagosPorMembresia(@PathVariable int idMembresia) {
        return ResponseEntity.ok(this.pagoService.listarPagosPorMembresia(idMembresia));
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public ResponseEntity<List<PagoResponse>> pagosDeUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.pagoService.listarPagosDeUsuario(idUsuario));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(this.pagoService.listarTodos());
    }

    @PostMapping({"/mercadopago/preferencia"})
    public ResponseEntity<MercadoPagoPreferenceResponse> crearPreferencia(@RequestParam String plan) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)) {
            return ResponseEntity.status(401).build();
        }
        Usuario usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.mercadoPagoService.crearPreferencia(plan, usuario));
    }

    @PostMapping({"/mercadopago/confirmar"})
    public ResponseEntity<MembresiaResponse> confirmarPago(@RequestParam(name = "paymentId", required = false) Long paymentId,
                                                           @RequestParam(name = "collection_id", required = false) Long collectionId,
                                                           @RequestParam(name = "id", required = false) Long paymentIdAlias,
                                                           @RequestParam(name = "topic", required = false) String topic,
                                                           @RequestParam(name = "token", required = false) String token,
                                                           @org.springframework.web.bind.annotation.RequestHeader(name = "X-Webhook-Token", required = false) String headerToken) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PagoController.class);
        log.info("Confirmar pago request paymentId={} collection_id={} id={} topic={}", paymentId, collectionId, paymentIdAlias, topic);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean tieneSesion = auth != null && auth.getPrincipal() instanceof Usuario;

        String provided = headerToken != null ? headerToken : token;
        if (!tieneSesion) {
            if (webhookToken != null && !webhookToken.isBlank()) {
                if (provided == null || !webhookToken.equals(provided)) {
                    return ResponseEntity.status(401).build();
                }
            }
        }
        Long effectiveId = paymentId != null ? paymentId : (collectionId != null ? collectionId : paymentIdAlias);
        // Webhook de merchant_order: hay que traducir a payment id
        if (effectiveId == null && topic != null && topic.equalsIgnoreCase("merchant_order")) {
            Long found = this.mercadoPagoService.obtenerPaymentIdDesdeMerchantOrder(paymentIdAlias);
            if (found != null) {
                effectiveId = found;
            }
        }
        if (effectiveId == null) {
            log.warn("No se recibio payment id valido en confirmacion");
            return ResponseEntity.badRequest().build();
        }
        log.info("Confirmando pago con id efectivo {}", effectiveId);
        return ResponseEntity.ok(this.pagoService.procesarPagoMercadoPago(effectiveId));
    }

    @GetMapping({"/mercadopago/reconciliar"})
    public ResponseEntity<MembresiaResponse> reconciliar(@RequestParam String plan) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario usuario)) {
            return ResponseEntity.status(401).build();
        }
        MembresiaResponse res = this.pagoService.reconciliarPagoPorUsuarioYPlan(usuario.getId(), plan);
        if (res == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }
}
