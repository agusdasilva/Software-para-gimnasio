package com.example.gymweb.dto.Response;

import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;
<<<<<<< Updated upstream

import java.util.Date;

public class UsuarioResponse {

=======
import java.util.Date;

public class UsuarioResponse {
>>>>>>> Stashed changes
    private Integer id;
    private String nombre;
    private String email;
    private Rol rol;
    private Estado estado;
    private Date fechaAlta;

    public Integer getId() {
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
<<<<<<< Updated upstream
        return nombre;
=======
        return this.nombre;
>>>>>>> Stashed changes
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
<<<<<<< Updated upstream
        return email;
=======
        return this.email;
>>>>>>> Stashed changes
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
<<<<<<< Updated upstream
        return rol;
=======
        return this.rol;
>>>>>>> Stashed changes
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
<<<<<<< Updated upstream
        return estado;
=======
        return this.estado;
>>>>>>> Stashed changes
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaAlta() {
<<<<<<< Updated upstream
        return fechaAlta;
=======
        return this.fechaAlta;
>>>>>>> Stashed changes
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
