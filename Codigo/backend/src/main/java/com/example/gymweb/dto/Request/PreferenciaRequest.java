package com.example.gymweb.dto.Request;


import lombok.Generated;

public class PreferenciaRequest {
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
        } else if (!(o instanceof PreferenciaRequest)) {
            return false;
        } else {
            PreferenciaRequest other = (PreferenciaRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                return this.isSilencioso() == other.isSilencioso();
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof PreferenciaRequest;
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
        return "PreferenciaRequest(silencioso=" + this.isSilencioso() + ")";
    }
}