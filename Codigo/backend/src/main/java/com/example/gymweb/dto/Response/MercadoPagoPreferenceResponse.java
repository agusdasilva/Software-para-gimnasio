package com.example.gymweb.dto.Response;

public class MercadoPagoPreferenceResponse {
    private String preferenceId;
    private String initPoint;

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }
}
