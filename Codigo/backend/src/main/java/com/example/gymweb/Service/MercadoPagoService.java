package com.example.gymweb.Service;

import com.example.gymweb.dto.Response.MercadoPagoPreferenceResponse;
import com.example.gymweb.dto.Response.MercadoPagoPaymentInfo;
import com.example.gymweb.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    @Value("${mercadopago.back-url:http://localhost:4200/membresias}")
    private String backUrl;

    private static final Map<String, PlanInfo> PLANES = new HashMap<>();
    static {
        PLANES.put("dia", new PlanInfo("Plan por día", new BigDecimal("10")));
        PLANES.put("mensual-3", new PlanInfo("Plan mensual - 3 días", new BigDecimal("80")));
        PLANES.put("mensual-full", new PlanInfo("Plan mensual - Full", new BigDecimal("120")));
    }

    public MercadoPagoPreferenceResponse crearPreferencia(String planCode, Usuario usuario) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }
        PlanInfo plan = PLANES.get(planCode);
        if (plan == null) {
            throw new RuntimeException("Plan inválido");
        }

        RestTemplate restTemplate = new RestTemplate();

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
        // No forzamos auto_return para evitar errores si MP requiere https en success
        payload.put("external_reference", "user-" + usuario.getId() + "-plan-" + planCode);
        payload.put("payer", Map.of("email", usuario.getEmail()));

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
        res.setInitPoint(response.get("init_point").toString());
        return res;
    }

    public MercadoPagoPaymentInfo obtenerPago(Long paymentId) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Falta configurar mercadopago.access-token");
        }

        RestTemplate restTemplate = new RestTemplate();
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

    private record PlanInfo(String nombre, BigDecimal precio) {}
}
