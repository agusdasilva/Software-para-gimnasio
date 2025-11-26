package com.example.gymweb.dto.Request;

import java.math.BigDecimal;

public class PagoRequest {
    private int idMembresia;
    private BigDecimal monto;
    private String comprobanteUrl;

    public int getIdMembresia() {
        return this.idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getComprobanteUrl() {
        return this.comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }
}