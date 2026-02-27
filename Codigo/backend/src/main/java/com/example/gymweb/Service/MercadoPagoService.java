package com.example.gymweb.Service;

import com.example.gymweb.Repository.PlanRepository;
import com.example.gymweb.dto.Response.MercadoPagoPreferenceResponse;
import com.example.gymweb.dto.Response.MercadoPagoPaymentInfo;
import com.example.gymweb.model.Plan;
import com.example.gymweb.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
@Service
public class MercadoPagoService {

    private final PlanRepository planRepository;

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    @Value("${mercadopago.back-url:http://localhost:4200/membresias}")
    private String backUrl;

    @Value("${mercadopago.notification-url:http://localhost:8080/api/pagos/mercadopago/confirmar}")
    private String notificationUrl;

    @Value("${mercadopago.auto-return:approved}")
    private String autoReturn;

    public MercadoPagoService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public MercadoPagoPreferenceResponse crearPreferencia(String planCode, Usuario usuario) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }
        PlanInfo plan = obtenerPlanInfo(planCode);

        RestTemplate restTemplate = restTemplate();

        Map<String, Object> item = new HashMap<>();
        item.put("title", plan.nombre());
        item.put("quantity", 1);
        item.put("unit_price", plan.precio());
        item.put("currency_id", "ARS");

        Map<String, Object> backUrls = new HashMap<>();
        backUrls.put("success", backUrl);
        backUrls.put("failure", backUrl);
        backUrls.put("pending", backUrl);

        Map<String, Object> payload = new HashMap<>();
        payload.put("items", java.util.List.of(item));
        payload.put("back_urls", backUrls);
        // Enviamos auto_return solo cuando back_urls es https (requerido por MP)
        if (backUrl != null && backUrl.toLowerCase().startsWith("https://") && autoReturn != null && !autoReturn.isBlank()) {
            payload.put("auto_return", autoReturn);
        }
        payload.put("external_reference", "user-" + usuario.getId() + "-plan-" + planCode);
        payload.put("payer", Map.of("email", usuario.getEmail()));
        payload.put("notification_url", notificationUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        Map<?, ?> response = restTemplate.postForObject("https://api.mercadopago.com/checkout/preferences", request, Map.class);
        if (response == null || response.get("init_point") == null) {
            throw new RuntimeException("No se pudo crear la preferencia de pago");
        }

        MercadoPagoPreferenceResponse res = new MercadoPagoPreferenceResponse();
        res.setPreferenceId(response.get("id").toString());
        Object sandboxInit = response.get("sandbox_init_point");
        if (sandboxInit != null && !sandboxInit.toString().isBlank()) {
            res.setInitPoint(sandboxInit.toString());
        } else {
            res.setInitPoint(response.get("init_point").toString());
        }
        return res;
    }

    public MercadoPagoPaymentInfo obtenerPago(Long paymentId) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }

        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        Map<?, ?> response = restTemplate.exchange(
                "https://api.mercadopago.com/v1/payments/" + paymentId,
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        ).getBody();

        if (response == null) {
            throw new RuntimeException("No se pudo obtener el pago");
        }

        MercadoPagoPaymentInfo info = new MercadoPagoPaymentInfo();
        Object status = response.get("status");
        info.setStatus(status != null ? status.toString() : "");
        Object extRef = response.get("external_reference");
        info.setExternalReference(extRef != null ? extRef.toString() : "");
        Object amount = response.get("transaction_amount");
        if (amount != null) {
            info.setTransactionAmount(new BigDecimal(amount.toString()));
        }
        return info;
    }

    /**
     * Busca el pago mas reciente con una external_reference exacta.
     * Retorna el payment id o null si no se encuentra.
     */
    public Long buscarPagoPorExternalReference(String externalReference) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        Map<?, ?> response = restTemplate.exchange(
                "https://api.mercadopago.com/v1/payments/search?external_reference=" + externalReference + "&sort=date_created&criteria=desc&limit=1",
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        ).getBody();
        if (response == null) {
            return null;
        }
        Object results = response.get("results");
        if (results instanceof java.util.List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof Map<?, ?> map && map.get("id") != null) {
                try {
                    return Long.parseLong(map.get("id").toString());
                } catch (NumberFormatException ignored) { }
            }
        }
        return null;
    }

    /**
     * Devuelve el payment id mas reciente para un usuario, buscando por prefijo user-{id}-plan-*
     */
    public Long buscarPagoMasRecientePorUsuario(int userId) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        String prefix = "user-" + userId + "-plan-";
        // Intento 1: filtro por prefijo en external_reference
        Map<?, ?> response = restTemplate.exchange(
                "https://api.mercadopago.com/v1/payments/search?external_reference=" + prefix + "&sort=date_created&criteria=desc&limit=3",
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        ).getBody();
        Long found = extraerPrimerPaymentId(response);
        if (found != null) return found;

        // Intento 2: sin filtro, tomar el último pago cuyo external_reference contenga user-{id}
        Map<?, ?> responseAll = restTemplate.exchange(
                "https://api.mercadopago.com/v1/payments/search?sort=date_created&criteria=desc&limit=10",
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        ).getBody();
        if (responseAll == null) return null;
        Object resultsAll = responseAll.get("results");
        if (resultsAll instanceof java.util.List<?> list) {
            for (Object o : list) {
                if (o instanceof Map<?, ?> map) {
                    Object ext = map.get("external_reference");
                    if (ext != null && ext.toString().contains("user-" + userId)) {
                        Object idObj = map.get("id");
                        if (idObj != null) {
                            try {
                                return Long.parseLong(idObj.toString());
                            } catch (NumberFormatException ignored) { }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Long extraerPrimerPaymentId(Map<?, ?> response) {
        if (response == null) return null;
        Object results = response.get("results");
        if (results instanceof java.util.List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof Map<?, ?> map && map.get("id") != null) {
                try {
                    return Long.parseLong(map.get("id").toString());
                } catch (NumberFormatException ignored) { }
            }
        }
        return null;
    }

    /**
     * Obtiene el primer payment id asociado a una merchant_order enviada por webhook.
     * Devuelve null si no hay pagos asociados.
     */
    public Long obtenerPaymentIdDesdeMerchantOrder(Long merchantOrderId) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        Map<?, ?> response = restTemplate.exchange(
                "https://api.mercadopago.com/merchant_orders/" + merchantOrderId,
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        ).getBody();
        if (response == null) {
            return null;
        }
        Object payments = response.get("payments");
        if (payments instanceof java.util.List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof Map<?, ?> map && map.get("id") != null) {
                try {
                    return Long.parseLong(map.get("id").toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    private PlanInfo obtenerPlanInfo(String planCode) {
        if (planCode == null || !planCode.matches("\\d+")) {
            throw new RuntimeException("Plan invalido: se requiere ID numerico");
        }
        int idPlan = Integer.parseInt(planCode);
        Plan p = this.planRepository.findById(idPlan)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + idPlan));
        return new PlanInfo(p.getNombre(), p.getPrecio(), p.getPeriodo());
    }

    private record PlanInfo(String nombre, BigDecimal precio, String periodo) {}

    private RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}
