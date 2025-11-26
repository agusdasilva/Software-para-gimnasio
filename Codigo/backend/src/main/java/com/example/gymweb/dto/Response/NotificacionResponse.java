package com.example.gymweb.dto.Response;

import java.time.LocalDateTime;
import lombok.Generated;

public class NotificacionResponse {
    private int id;
    private String mensaje;
    private boolean leida;
    private LocalDateTime fecha;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLeida() {
        return this.leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NotificacionResponse)) {
            return false;
        } else {
            NotificacionResponse other = (NotificacionResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getId() != other.getId()) {
                return false;
            } else if (this.isLeida() != other.isLeida()) {
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

                Object this$fecha = this.getFecha();
                Object other$fecha = other.getFecha();
                if (this$fecha == null) {
                    if (other$fecha != null) {
                        return false;
                    }
                } else if (!this$fecha.equals(other$fecha)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof NotificacionResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + (this.isLeida() ? 79 : 97);
        Object $mensaje = this.getMensaje();
        result = result * 59 + ($mensaje == null ? 43 : $mensaje.hashCode());
        Object $fecha = this.getFecha();
        result = result * 59 + ($fecha == null ? 43 : $fecha.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        int var10000 = this.getId();
        return "NotificacionResponse(id=" + var10000 + ", mensaje=" + this.getMensaje() + ", leida=" + this.isLeida() + ", fecha=" + String.valueOf(this.getFecha()) + ")";
    }
}