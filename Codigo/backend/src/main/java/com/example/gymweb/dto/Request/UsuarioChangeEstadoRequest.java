package com.example.gymweb.dto.Request;

import com.example.gymweb.model.Estado;

public class UsuarioChangeEstadoRequest {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    private Integer idUsuario;
    private Estado nuevoEstado;

    public Integer getIdUsuario() {
<<<<<<< Updated upstream
        return idUsuario;
=======
        return this.idUsuario;
>>>>>>> Stashed changes
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Estado getNuevoEstado() {
<<<<<<< Updated upstream
        return nuevoEstado;
=======
        return this.nuevoEstado;
>>>>>>> Stashed changes
    }

    public void setNuevoEstado(Estado nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
