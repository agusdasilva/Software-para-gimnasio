package com.example.gymweb.dto.Request;

import lombok.Generated;

public class NotificacionRequest {
    private int idUsuario;
    private String mensaje;

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NotificacionRequest)) {
            return false;
        } else {
            NotificacionRequest other = (NotificacionRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getIdUsuario() != other.getIdUsuario()) {
                return false;
            } else {
                Object this$mensaje = this.getMensaje();
                Object other$mensaje = other.getMensaje();
                if (this$mensaje == null) {
                    if (other$mensaje != null) {
                        return false;
                    }
                } else if (!this$mensaje.equals(other$mensaje)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof NotificacionRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIdUsuario();
        Object $mensaje = this.getMensaje();
        result = result * 59 + ($mensaje == null ? 43 : $mensaje.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        int var10000 = this.getIdUsuario();
        return "NotificacionRequest(idUsuario=" + var10000 + ", mensaje=" + this.getMensaje() + ")";
    }
}