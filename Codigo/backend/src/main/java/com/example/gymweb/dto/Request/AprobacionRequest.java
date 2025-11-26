package com.example.gymweb.dto.Request;

public class AprobacionRequest {
    private boolean aprobar;

    public AprobacionRequest(boolean aprobar) {
        this.aprobar = aprobar;
    }

    public AprobacionRequest() {
    }

    public boolean isAprobar() {
<<<<<<< Updated upstream
        return aprobar;
=======
        return this.aprobar;
>>>>>>> Stashed changes
    }

    public void setAprobar(boolean aprobar) {
        this.aprobar = aprobar;
    }
}
