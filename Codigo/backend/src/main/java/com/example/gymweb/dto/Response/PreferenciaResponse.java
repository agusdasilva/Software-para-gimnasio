package com.example.gymweb.dto.Response;

import lombok.Generated;

public class PreferenciaResponse {
    private boolean silencioso;

    public boolean isSilencioso() {
        return this.silencioso;
    }

    public void setSilencioso(boolean silencioso) {
        this.silencioso = silencioso;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PreferenciaResponse)) {
            return false;
        } else {
            PreferenciaResponse other = (PreferenciaResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                return this.isSilencioso() == other.isSilencioso();
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof PreferenciaResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSilencioso() ? 79 : 97);
        return result;
    }

    @Generated
    public String toString() {
        return "PreferenciaResponse(silencioso=" + this.isSilencioso() + ")";
    }
}