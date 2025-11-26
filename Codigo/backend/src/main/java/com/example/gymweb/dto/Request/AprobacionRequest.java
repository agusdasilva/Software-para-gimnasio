package com.example.gymweb.dto.Request;


public class AprobacionRequest {
    private boolean aprobar;

    public AprobacionRequest(boolean aprobar) {
        this.aprobar = aprobar;
    }

    public AprobacionRequest() {
    }

    public boolean isAprobar() {
        return this.aprobar;
    }

    public void setAprobar(boolean aprobar) {
        this.aprobar = aprobar;
    }
}

