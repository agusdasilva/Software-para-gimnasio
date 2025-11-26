package com.example.gymweb.dto.Response;


import lombok.Generated;

public class PreferenciaResponse {
    private boolean silencioso;

    public PreferenciaResponse() {
    }

    public boolean isSilencioso() {
        return silencioso;
    }

    public void setSilencioso(boolean silencioso) {
        this.silencioso = silencioso;
    }
}