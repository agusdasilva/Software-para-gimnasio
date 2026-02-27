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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

@RestController
@RequestMapping({"/api/pagos"})
public class PagoController {
    private PagoService pagoService;
    private MercadoPagoService mercadoPagoService;
    @Value("${pagos.webhook-token:}")
    private String webhookToken;
    @Value("${mercadopago.webhook-secret:}")
    private String webhookSecret;

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
        if (plan == null || !plan.matches("\\d+")) {
            return ResponseEntity.badRequest().build();
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
                                                           @org.springframework.web.bind.annotation.RequestHeader(name = "X-Webhook-Token", required = false) String headerToken,
                                                           @org.springframework.web.bind.annotation.RequestHeader(name = "X-Signature", required = false) String signature,
                                                           @org.springframework.web.bind.annotation.RequestHeader(name = "X-Request-Id", required = false) String requestId,
                                                           @RequestBody(required = false) Map<String, Object> body) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PagoController.class);
        log.info("Confirmar pago request paymentId={} collection_id={} id={} topic={} bodyHasData={}",
                paymentId, collectionId, paymentIdAlias, topic, body != null && body.containsKey("data"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean tieneSesion = auth != null && auth.getPrincipal() instanceof Usuario;
        boolean debeValidarFirma = !tieneSesion; // solo webhooks

        String provided = headerToken != null ? headerToken : token;
        if (!tieneSesion) {
            if (webhookToken != null && !webhookToken.isBlank()) {
                if (provided == null || !webhookToken.equals(provided)) {
                    return ResponseEntity.status(401).build();
                }
            }
        }
        Long bodyPaymentId = extraerPaymentIdDeBody(body);
        Long effectiveId = paymentId != null ? paymentId : (collectionId != null ? collectionId : paymentIdAlias);
        if (effectiveId == null) {
            effectiveId = bodyPaymentId;
        }
        // Webhook de merchant_order: hay que traducir a payment id
        if (effectiveId == null && topic != null && topic.equalsIgnoreCase("merchant_order")) {
            Long merchantOrderId = bodyPaymentId != null ? bodyPaymentId : paymentIdAlias;
            Long found = this.mercadoPagoService.obtenerPaymentIdDesdeMerchantOrder(merchantOrderId);
            if (found != null) {
                effectiveId = found;
            }
        }

        if (debeValidarFirma && !firmaValida(signature, requestId, bodyPaymentId != null ? bodyPaymentId.toString() : (effectiveId != null ? effectiveId.toString() : null))) {
            log.warn("Webhook rechazado por firma no valida");
            return ResponseEntity.status(401).build();
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
        if (plan == null || !plan.matches("\\d+")) {
            return ResponseEntity.badRequest().build();
        }
        MembresiaResponse res = this.pagoService.reconciliarPagoPorUsuarioYPlan(usuario.getId(), plan);
        if (res == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }

    private Long extraerPaymentIdDeBody(Map<String, Object> body) {
        if (body == null) return null;
        Object data = body.get("data");
        if (data instanceof Map<?, ?> dataMap) {
            Object idObj = dataMap.get("id");
            Long parsed = parseLong(idObj);
            if (parsed != null) return parsed;
        }
        return parseLong(body.get("id"));
    }

    private Long parseLong(Object obj) {
        if (obj == null) return null;
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Valida la firma x-signature de Mercado Pago usando el secreto configurado.
     * Si el secreto esta vacio, no se valida (modo dev).
     */
    private boolean firmaValida(String signatureHeader, String requestId, String dataId) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            return true; // deshabilitado en local
        }
        if (signatureHeader == null || !signatureHeader.contains("v1=") || !signatureHeader.contains("ts=")) {
            return false;
        }
        String ts = extraerValor(signatureHeader, "ts");
        String v1 = extraerValor(signatureHeader, "v1");
        if (ts == null || v1 == null || v1.isBlank()) {
            return false;
        }
        String template = "id:" + (dataId != null ? dataId.toLowerCase() : "") +
                ";request-id:" + (requestId != null ? requestId : "") +
                ";ts:" + ts + ";";
        try {
            String calculada = hmacSha256(template, webhookSecret);
            return constantTimeEquals(v1, calculada);
        } catch (Exception e) {
            return false;
        }
    }

    private String extraerValor(String header, String key) {
        if (header == null) return null;
        String[] parts = header.split(",");
        for (String p : parts) {
            String[] kv = p.split("=");
            if (kv.length == 2 && kv[0].trim().equalsIgnoreCase(key)) {
                return kv[1].trim();
            }
        }
        return null;
    }

    private String hmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(raw);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] ab = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);
        if (ab.length != bb.length) return false;
        int res = 0;
        for (int i = 0; i < ab.length; i++) {
            res |= ab[i] ^ bb[i];
        }
        return res == 0;
    }
}
